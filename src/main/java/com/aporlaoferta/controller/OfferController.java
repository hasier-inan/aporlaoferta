package com.aporlaoferta.controller;

import com.aporlaoferta.model.DateRange;
import com.aporlaoferta.model.OfferCategory;
import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.model.OfferFilters;
import com.aporlaoferta.model.OfferListResponse;
import com.aporlaoferta.model.TheDefaultOffer;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.validators.ValidationException;
import com.aporlaoferta.service.CaptchaHTTPManager;
import com.aporlaoferta.service.CompanyManager;
import com.aporlaoferta.service.InvalidOfferException;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.UserManager;
import com.aporlaoferta.utils.DateUtils;
import com.aporlaoferta.utils.JSONUtils;
import com.aporlaoferta.utils.LevenshteinDistance;
import com.aporlaoferta.utils.OfferValidatorHelper;
import com.aporlaoferta.utils.UnhealthyException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by hasiermetal on 2/02/14.
 */
@Controller
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class OfferController {

    private final Logger LOG = LoggerFactory.getLogger(OfferController.class);

    @Autowired
    private OfferValidatorHelper offerValidatorHelper;

    @Autowired
    private UserManager userManager;

    @Autowired
    private OfferManager offerManager;

    @Autowired
    private CompanyManager companyManager;

    @Autowired
    private CaptchaHTTPManager captchaHttpManager;


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

    private void includeOfferMeta(ModelAndView model, TheOffer theOffer) {
        model.addObject("offerId", theOffer.getId());
        model.addObject("offerTitle", String.format("%s: %s", theOffer.getOfferTitle(), theOffer.getFinalPrice().toString()));
        model.addObject("offerDescription", theOffer.getOfferDescription());
        String offerImage = theOffer.getOfferImage();
        model.addObject("offerImage", offerImage.indexOf("offer.png") >= 0 ?
                String.format("http://www.aporlaoferta.com%s", offerImage) : offerImage);
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

    private TheResponse processOfferCreation(TheOffer thatOffer) {
        includeInitialValues(thatOffer);
        updateCompany(thatOffer);
        try {
            includeAffiliationLink(thatOffer);
            return validateAndPersistOffer(thatOffer);
        } catch (MalformedURLException e) {
            LOG.error("Could not add custom affiliation id to offer url because url is invalid: ", e);
        } catch (UnsupportedEncodingException e) {
            LOG.error("Could not add custom affiliation id to offer url because url is wrongly encoded: ", e);
        } catch (UnhealthyException e) {
            LOG.error("Could not add custom affiliation id to offer url because url is unhealthy: ", e);
            return ResponseResultHelper.createUnhealthyResponse();
        }
        return ResponseResultHelper.createDefaultResponse();
    }

    private void includeInitialValues(TheOffer thatOffer) {
        includeDefaultImage(thatOffer);
        includeOfferInUser(thatOffer);
        thatOffer.setOfferPositiveVote(0L);
        thatOffer.setOfferNegativeVote(0L);
        thatOffer.setOfferCreatedDate(new Date());
    }

    private void includeDefaultImage(TheOffer thatOffer) {
        if (isEmpty(thatOffer.getOfferImage())) {
            thatOffer.setOfferImage(TheDefaultOffer.OFFER_IMAGE_URL.getCode());
        }
    }

    private void includeAffiliationLink(TheOffer thatOffer) throws MalformedURLException, UnsupportedEncodingException, UnhealthyException {
        thatOffer.setOfferLink(this.companyManager.createAffiliationLink(
                thatOffer.getOfferCompany(), thatOffer.getOfferLink()));
    }

    private void updateCompany(TheOffer thatOffer) {
        if (thatOffer.getOfferCompany() != null) {
            String watermarkedCompany = this.companyManager.getWatermarkedCompany(thatOffer.getOfferLink().toLowerCase());
            if (!isEmpty(watermarkedCompany)) {
                updateCompanyFromWatermark(thatOffer, watermarkedCompany);
            } else {
                updateCompanyFromName(thatOffer);
            }
        }
    }

    private void updateCompanyFromWatermark(TheOffer thatOffer, String watermarkedCompany) {
        thatOffer.setOfferCompany(this.companyManager.getCompanyFromName(watermarkedCompany));
    }

    private void updateCompanyFromName(TheOffer thatOffer) {
        String usedName = thatOffer.getOfferCompany().getCompanyName();
        OfferCompany offerCompany = this.companyManager.getCompanyFromName(usedName);
        if (offerCompany != null) {
            thatOffer.setOfferCompany(offerCompany);
        } else {
            updateCompanyFromLevenshtein(thatOffer, usedName);
        }
    }

    private void updateCompanyFromLevenshtein(TheOffer thatOffer, String usedName) {
        List<OfferCompany> allCompanies = this.companyManager.getAllCompanies();
        for (OfferCompany company : allCompanies) {
            if (LevenshteinDistance.process(usedName, company.getCompanyName(), 4)) {
                thatOffer.setOfferCompany(company);
                return;
            }
        }
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

    private TheResponse processOfferUpdate(TheOffer theOffer) {
        try {
            Long offerId = isEmpty(theOffer.getId()) ? -1L : theOffer.getId();
            TheOffer originalOffer = this.offerManager.getOfferFromId(offerId);
            if (originalOffer == null) {
                throw new ValidationException("Invalid offer id");
            }
            this.offerValidatorHelper.validateOffer(theOffer);
            return updateOfferFromOriginal(theOffer, originalOffer);
        } catch (ValidationException | NumberFormatException e) {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.UPDATE_OFFER_VALIDATION_ERROR, new TheResponse());
        } catch (MalformedURLException e) {
            LOG.error("Could not add custom affiliation id to offer url because url is invalid: ", e);
        } catch (UnsupportedEncodingException e) {
            LOG.error("Could not add custom affiliation id to offer url because url is wrongly encoded: ", e);
        } catch (UnhealthyException e) {
            LOG.error("Could not add custom affiliation id to offer url because url is unhealthy: ", e);
            return ResponseResultHelper.createUnhealthyResponse();
        }
        return ResponseResultHelper.createDefaultResponse();
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

    private void addCategoryIntoList(List<Map<String, String>> offerCategoriesList, OfferCategory offerCategory) {
        Map<String, String> offerCategories = new HashMap<>();
        offerCategories.put("text", offerCategory.name());
        offerCategories.put("value", offerCategory.getCode());
        offerCategories.put("display", offerCategory.getText());
        offerCategoriesList.add(offerCategories);
    }

    private void createEmptyFields(List<TheOffer> hundredOffers) {
        for (TheOffer theOffer : hundredOffers) {
            theOffer.setOfferComments(new ArrayList<OfferComment>());
            resetUserSensibleDataFromOffer(theOffer);
        }
    }

    private void updateResponseWithSuccessCode(OfferListResponse theOfferResponse) {
        theOfferResponse.setCode(ResultCode.ALL_OK.getCode());
        theOfferResponse.setDescription(ResultCode.ALL_OK.getResultDescriptionEsp());
        theOfferResponse.setResponseResult(ResultCode.ALL_OK.getResponseResult());
    }

    private TheResponse updateOfferFromOriginal(TheOffer theOffer, TheOffer originalOffer) throws UnsupportedEncodingException, UnhealthyException, MalformedURLException {
        TheResponse result = new TheResponse();
        if (!this.userManager.isUserAuthorised(theOffer)) {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.INVALID_OWNER_ERROR, result);
        }
        TheOffer updatedOffer = updateOfferWithLastDetails(theOffer, originalOffer);
        includeAffiliationLink(updatedOffer);
        return validateAndPersistOffer(updatedOffer);
    }

    private TheOffer updateOfferWithLastDetails(TheOffer theUpdatedOffer, TheOffer originalOffer) throws UnsupportedEncodingException, UnhealthyException, MalformedURLException {
        originalOffer.setFinalPrice(theUpdatedOffer.getFinalPrice());
        originalOffer.setOfferTitle(theUpdatedOffer.getOfferTitle());
        if (!originalOffer.getOfferCompany().getCompanyName().equals(
                theUpdatedOffer.getOfferCompany().getCompanyName())) {
            originalOffer.setOfferCompany(theUpdatedOffer.getOfferCompany());
            updateCompany(originalOffer);
        }
        originalOffer.setOfferCategory(theUpdatedOffer.getOfferCategory());
        originalOffer.setOfferDescription(theUpdatedOffer.getOfferDescription());
        originalOffer.setOfferExpired(theUpdatedOffer.isOfferExpired());
        originalOffer.setOfferImage(theUpdatedOffer.getOfferImage());
        originalOffer.setOfferLink(theUpdatedOffer.getOfferLink());
        originalOffer.setOriginalPrice(theUpdatedOffer.getOriginalPrice());
        return originalOffer;
    }

    private TheResponse validateAndPersistOffer(TheOffer thatOffer) {
        try {
            this.offerValidatorHelper.validateOffer(thatOffer);
            return saveOfferAndUpdateResult(thatOffer);
        } catch (ValidationException e) {
            LOG.warn(ResultCode.CREATE_OFFER_VALIDATION_ERROR.getResultDescription(), e);
            return ResponseResultHelper.offerValidationErrorResponse();
        }
    }

    private TheResponse saveOfferAndUpdateResult(TheOffer thatOffer) {
        TheUser theUser = this.userManager.saveUser(thatOffer.getOfferUser());
        TheOffer theOffer = theUser.obtainLatestOffer();
        TheResponse result = new TheResponse();
        if (theOffer == null) {
            ControllerHelper.addEmptyDatabaseObjectMessage(result, LOG);
        } else {
            String okMessage = String.format("Offer successfully updated. Id: %s", theOffer.getId());
            LOG.info(okMessage);
            result = ResponseResultHelper.offerUpdateResponse(theOffer.getId().toString());
        }
        return result;
    }

    private void includeOfferInUser(TheOffer thatOffer) {
        String nickName = this.userManager.getUserNickNameFromSession();
        TheUser theUser = this.userManager.getUserFromNickname(nickName);
        if (theUser != null) {
            theUser.addOffer(thatOffer);
        }
    }

    private TheOffer getFixedOfferFromId(Long number) throws InvalidOfferException {
        TheOffer theOffer = this.offerManager.getOfferFromId(number);
        if (isEmpty(theOffer)) {
            throw new InvalidOfferException(String.format("Offer %s not found.", number.toString()));
        }
        resetUserSensibleDataFromOffer(theOffer);
        resetCompanySensibleDataFromOffer(theOffer);
        theOffer.sortOfferComments();
        List<OfferComment> offerComments = theOffer.getOfferComments();

        if (offerComments != null && offerComments.size() >= 0) {
            //TODO do we need this hack for the lazy var?
            for (OfferComment offerComment : offerComments) {
                resetUserSensibleDataFromComment(offerComment);
            }
        }
        return theOffer;
    }

    private void resetCompanySensibleDataFromOffer(TheOffer theOffer) {
        OfferCompany offerCompany = theOffer.getOfferCompany();
        offerCompany.setCompanyOffers(new HashSet<TheOffer>());
        theOffer.setOfferCompany(offerCompany);
    }

    private void resetUserSensibleDataFromOffer(TheOffer theOffer) {
        TheUser offerUser = theOffer.getOfferUser();
        offerUser.setUserEmail(null);
        offerUser.setUserPassword(null);
        theOffer.setOfferUser(offerUser);
    }

    private void resetUserSensibleDataFromComment(OfferComment theComment) {
        TheUser offerUser = theComment.getCommentOwner();
        offerUser.setUserEmail(null);
        offerUser.setUserPassword(null);
        theComment.setCommentOwner(offerUser);
    }
}
