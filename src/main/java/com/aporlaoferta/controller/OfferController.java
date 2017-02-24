package com.aporlaoferta.controller;

import com.aporlaoferta.model.DateRange;
import com.aporlaoferta.model.OfferCategory;
import com.aporlaoferta.model.OfferFilters;
import com.aporlaoferta.model.OfferListResponse;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.validators.ValidationException;
import com.aporlaoferta.service.InvalidOfferException;
import com.aporlaoferta.utils.DateUtils;
import com.aporlaoferta.utils.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by hasiermetal on 2/02/14.
 */
@Controller
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class OfferController extends OfferHelper {

    @RequestMapping(value = {"/offer"}, method = RequestMethod.GET)
    public ModelAndView start(@RequestParam(value = "sh", required = true) Long number) {
        ModelAndView model = new ModelAndView();
        model.addObject("specificOffer", number);
        try {
            TheOffer theOffer = getFixedOfferFromId(number);
            ObjectMapper mapper = new ObjectMapper();
            String offerMap = mapper.writeValueAsString(Arrays.asList(new TheOffer[]{theOffer}));
            model.addObject("specificOfferData", JSONUtils.escapeQuotes(offerMap));
            includeOfferMeta(model, theOffer);
        } catch (JsonProcessingException e) {
            LOG.error("Could not parse offer to json: ", e);
        } catch (InvalidOfferException e) {
            model.addObject("msg", ResultCode.INVALID_OFFER_ERROR.getResultDescriptionEsp());
            LOG.warn("Invalid offer retrieved: ", e);
        }
        model.setViewName("index");
        return model;
    }

    @RequestMapping(value = "/getOffers", method = RequestMethod.POST)
    @ResponseBody
    public OfferListResponse getOffers(@RequestParam(value = "number", required = false) Long number,
                                       @RequestParam(value = "dateRange", required = false) Integer dateRange) {
        OfferListResponse theOfferResponse = new OfferListResponse();
        List<TheOffer> hundredOffers = this.offerManager.getNextHundredOffers(number != null ? number + 1 : 0L,
                isEmpty(dateRange) ? DateRange.WEEK : DateUtils.getDateRange(dateRange));
        //Temporary hack  because Lazy initialisation comments are empty...
        createEmptyFields(hundredOffers);
        theOfferResponse.setTheOffers(hundredOffers);
        updateResponseWithSuccessCode(theOfferResponse);
        return theOfferResponse;
    }

    @RequestMapping(value = "/getFilteredOffers", method = RequestMethod.POST)
    @ResponseBody
    public OfferListResponse getFilteredOffers(
            @RequestBody OfferFilters offerFilters,
            @RequestParam(value = "number", required = false) Long number) {
        OfferListResponse theOfferResponse = new OfferListResponse();
        List<TheOffer> hundredOffers = this.offerManager.getFilteredNextHundredResults(offerFilters,
                number != null ? number + 1 : 0L);
        //Temporary hack  because Lazy initialisation comments are empty.
        createEmptyFields(hundredOffers);
        theOfferResponse.setTheOffers(hundredOffers);
        updateResponseWithSuccessCode(theOfferResponse);
        return theOfferResponse;
    }


    @RequestMapping(value = "/getHottestOffers", method = RequestMethod.POST)
    @ResponseBody
    public OfferListResponse getHottestOffers(@RequestParam(value = "number", required = false) Long number,
                                              @RequestParam(value = "dateRange", required = false) Integer dateRange) {
        OfferListResponse theOfferResponse = new OfferListResponse();
        List<TheOffer> hundredOffers = this.offerManager.getNextHundredHottestOffers(number != null ? number + 1 : 0L,
                isEmpty(dateRange) ? DateRange.WEEK : DateUtils.getDateRange(dateRange));
        //Temporary hack  because Lazy initialisation comments are empty.
        createEmptyFields(hundredOffers);
        theOfferResponse.setTheOffers(hundredOffers);
        updateResponseWithSuccessCode(theOfferResponse);
        return theOfferResponse;
    }

    @RequestMapping(value = "/getOffer", method = RequestMethod.POST)
    @ResponseBody
    public OfferListResponse getOfferById(@RequestParam(value = "id", required = true) Long number) {
        OfferListResponse theOfferResponse = new OfferListResponse();
        TheOffer theOffer = null;
        try {
            theOffer = getFixedOfferFromId(number);
            theOfferResponse.setTheOffers(Arrays.asList(new TheOffer[]{theOffer}));
            updateResponseWithSuccessCode(theOfferResponse);
        } catch (InvalidOfferException e) {
            LOG.warn("Invalid offer retrieved: ", e);
            theOfferResponse.setCode(ResultCode.INVALID_OFFER_ERROR.getCode());
            theOfferResponse.setDescriptionEsp(ResultCode.INVALID_OFFER_ERROR.getResultDescriptionEsp());
            theOfferResponse.setResponseResult(ResultCode.INVALID_OFFER_ERROR.getResponseResult());
        }
        return theOfferResponse;
    }

    @RequestMapping(value = "/createOffer", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse createOffer(@RequestBody TheOffer thatOffer,
                                   @RequestParam(value = "recaptcha", required = true) String reCaptcha) {
        if (!this.captchaHttpManager.validHuman(reCaptcha)) {
            return ResponseResultHelper.createInvalidCaptchaResponse();
        }

        if (this.userManager.userIsBanned()) {
            return ResponseResultHelper.createInvalidUserResponse();
        }

        return processOfferCreation(thatOffer);
    }

    @RequestMapping(value = "/expireOffer", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse expireOffer(@RequestParam(value = "id", required = true) Long offerId) {
        try {
            if (this.userManager.userIsBanned()) {
                return ResponseResultHelper.createInvalidUserResponse();
            }

            TheOffer originalOffer = this.offerManager.getOfferFromId(offerId);
            if (originalOffer == null) {
                throw new ValidationException("Invalid offer id");
            }
            TheResponse result = ResponseResultHelper.createDefaultResponse();
            if (!this.userManager.isUserAuthorised(originalOffer)) {
                return ResponseResultHelper.
                        responseResultWithResultCodeError(ResultCode.INVALID_OWNER_ERROR, result);
            }
            if (this.offerManager.expireOffer(originalOffer).isOfferExpired()) {
                result.assignResultCode(ResultCode.ALL_OK, ResponseResult.OK.value(), "La Oferta ha sido marcada como caducada");
            }
            return result;
        } catch (ValidationException | NumberFormatException e) {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.UPDATE_OFFER_VALIDATION_ERROR, new TheResponse());
        }
    }

    @RequestMapping(value = "/updateOffer", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse updateOffer(@RequestBody TheOffer theOffer,
                                   @RequestParam(value = "recaptcha", required = true) String reCaptcha) {
        if (!this.captchaHttpManager.validHuman(reCaptcha)) {
            return ResponseResultHelper.createInvalidCaptchaResponse();
        }

        if (this.userManager.userIsBanned()) {
            return ResponseResultHelper.createInvalidUserResponse();
        }

        return processOfferUpdate(theOffer);
    }

    @RequestMapping(value = "/removeOffer", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse removeOffer(@RequestParam(value = "id", required = true) Long id) {
        this.offerManager.deleteOffer(id);
        return ResponseResultHelper.offerUpdateResponse(id.toString());
    }

    @RequestMapping(value = "/getOfferCategories", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> getOfferCategories() {
        List<Map<String, String>> offerCategoriesList = new ArrayList<>();
        OfferCategory[] categoryValues = OfferCategory.values();
        for (OfferCategory offerCategory : categoryValues) {
            addCategoryIntoList(offerCategoriesList, offerCategory);
        }
        return offerCategoriesList;
    }

}
