package com.aporlaoferta.controller;

import com.aporlaoferta.model.OfferCategory;
import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.model.OfferFilters;
import com.aporlaoferta.model.TheDefaultOffer;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheOfferResponse;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.validators.ValidationException;
import com.aporlaoferta.service.CaptchaHTTPManager;
import com.aporlaoferta.service.CompanyManager;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.UserManager;
import com.aporlaoferta.utils.OfferValidatorHelper;
import com.aporlaoferta.utils.UnhealthyException;
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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by hasiermetal on 2/02/14.
 */
@Controller
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
//TODO: Move @Transaction to a service
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

    @RequestMapping(value = "/getOffers", method = RequestMethod.POST)
    @ResponseBody
    public TheOfferResponse getOffers(@RequestParam(value = "number", required = false) Long number) {
        TheOfferResponse theOfferResponse = new TheOfferResponse();
        List<TheOffer> hundredOffers = this.offerManager.getNextHundredOffers(number != null ? number : 0L);
        //Temporary hack  because Lazy initialisation comments are empty...
        createEmptyFields(hundredOffers);
        theOfferResponse.setTheOffers(hundredOffers);
        updateResponseWithSuccessCode(theOfferResponse);
        return theOfferResponse;
    }

    @RequestMapping(value = "/getFilteredOffers", method = RequestMethod.POST)
    @ResponseBody
    public TheOfferResponse getFilteredOffers(
            @RequestBody OfferFilters offerFilters) {
        TheOfferResponse theOfferResponse = new TheOfferResponse();
        List<TheOffer> hundredOffers = this.offerManager.getFilteredNextHundredResults(offerFilters);
        //Temporary hack  because Lazy initialisation comments are empty.
        createEmptyFields(hundredOffers);
        theOfferResponse.setTheOffers(hundredOffers);
        updateResponseWithSuccessCode(theOfferResponse);
        return theOfferResponse;
    }


    @RequestMapping(value = "/getHottestOffers", method = RequestMethod.POST)
    @ResponseBody
    public TheOfferResponse getHottestOffers(@RequestParam(value = "number", required = false) Long number) {
        TheOfferResponse theOfferResponse = new TheOfferResponse();
        List<TheOffer> hundredOffers = this.offerManager.getNextHundredHottestOffers(number != null ? number : 0L);
        //Temporary hack  because Lazy initialisation comments are empty.
        createEmptyFields(hundredOffers);
        theOfferResponse.setTheOffers(hundredOffers);
        updateResponseWithSuccessCode(theOfferResponse);
        return theOfferResponse;
    }

    @RequestMapping(value = "/getOffer", method = RequestMethod.POST)
    @ResponseBody
    public TheOfferResponse getOfferById(@RequestParam(value = "id", required = true) Long number) {
        TheOfferResponse theOfferResponse = new TheOfferResponse();
        TheOffer theOffer = this.offerManager.getOfferFromId(number);
        resetUserSensibleDataFromOffer(theOffer);
        theOffer.sortOfferComments();
        List<OfferComment> offerComments = theOffer.getOfferComments();

        if (offerComments != null && offerComments.size() >= 0) {
            //do we need this hack for the lazy var?
            for (OfferComment offerComment : offerComments) {
                resetUserSensibleDataFromComment(offerComment);
            }
        }
        theOfferResponse.setTheOffers(Arrays.asList(new TheOffer[]{theOffer}));
        updateResponseWithSuccessCode(theOfferResponse);
        return theOfferResponse;
    }

    @RequestMapping(value = "/createOffer", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse createOffer(@RequestBody TheOffer thatOffer,
                                   @RequestParam(value = "recaptcha", required = true) String reCaptcha) {
        if (this.captchaHttpManager.validHuman(reCaptcha)) {
            return processOfferCreation(thatOffer);
        } else {
            return ResponseResultHelper.createInvalidCaptchaResponse();
        }
    }

    private TheResponse processOfferCreation(TheOffer thatOffer) {
        includeDefaultImage(thatOffer);
        includeOfferInUser(thatOffer);
        thatOffer.setOfferCreatedDate(new Date());
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
            OfferCompany offerCompany = this.companyManager.getCompanyFromName(thatOffer.getOfferCompany().getCompanyName());
            if (offerCompany != null) {
                thatOffer.setOfferCompany(offerCompany);
            }
        }
    }

    @RequestMapping(value = "/updateOffer", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse updateOffer(@RequestBody TheOffer theOffer,
                                   @RequestParam(value = "offerId", required = true) String offerId) {
        TheOffer originalOffer = null;
        try {
            originalOffer = this.offerManager.getOfferFromId(Long.parseLong(offerId));
        } catch (NumberFormatException e) {
            LOG.error("Could not parse invalid offer id: ", e);
        }
        if (originalOffer == null) {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.UPDATE_OFFER_VALIDATION_ERROR, new TheResponse());
        }
        return updateOfferFromOriginal(theOffer, originalOffer);
    }

    @RequestMapping(value = "/getOfferCategories", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> getOfferCategories() {
        Map<String, String> offerCategories = new HashMap<>();
        OfferCategory[] categoryValues = OfferCategory.values();
        for (OfferCategory offerCategory : categoryValues) {
            offerCategories.put(offerCategory.name(), offerCategory.getCode());
        }
        return offerCategories;
    }

    private void createEmptyFields(List<TheOffer> hundredOffers) {
        for (TheOffer theOffer : hundredOffers) {
            theOffer.setOfferComments(new ArrayList<OfferComment>());
            resetUserSensibleDataFromOffer(theOffer);
        }
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

    private void updateResponseWithSuccessCode(TheOfferResponse theOfferResponse) {
        theOfferResponse.setCode(ResultCode.ALL_OK.getCode());
        theOfferResponse.setDescription(ResultCode.ALL_OK.getResultDescriptionEsp());
        theOfferResponse.setResponseResult(ResultCode.ALL_OK.getResponseResult());
    }

    private TheResponse updateOfferFromOriginal(TheOffer theOffer, TheOffer originalOffer) {
        String nickName = this.userManager.getUserNickNameFromSession();
        TheResponse result = new TheResponse();
        if (!originalOffer.getOfferUser().getUserNickname().equals(nickName)) {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.INVALID_OWNER_ERROR, result);
        }
        return validateAndPersistOffer(updateOfferWithLastDetails(theOffer, originalOffer));
    }

    private TheOffer updateOfferWithLastDetails(TheOffer theUpdatedOffer, TheOffer originalOffer) {
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
        originalOffer.setOfferCreatedDate(new Date());
        return originalOffer;
    }

    private TheResponse validateAndPersistOffer(TheOffer thatOffer) {
        TheResponse result = new TheResponse();
        try {
            this.offerValidatorHelper.validateOffer(thatOffer);
            thatOffer.setOfferPositiveVote(0L);
            thatOffer.setOfferNegativeVote(0L);
            saveOfferAndUpdateResult(thatOffer, result);
        } catch (ValidationException e) {
            String resultDescription = ResultCode.CREATE_OFFER_VALIDATION_ERROR.getResultDescription();
            LOG.warn(resultDescription, e);
            result.assignResultCode(ResultCode.CREATE_OFFER_VALIDATION_ERROR);
        }
        return result;
    }

    private void saveOfferAndUpdateResult(TheOffer thatOffer, TheResponse result) {
        TheUser theUser = this.userManager.saveUser(thatOffer.getOfferUser());
        TheOffer theOffer = theUser.obtainLatestOffer();
        if (theOffer == null) {
            ControllerHelper.addEmptyDatabaseObjectMessage(result, LOG);
        } else {
            String okMessage = String.format("Offer successfully updated. Id: %s", theOffer.getId());
            LOG.info(okMessage);
            result.assignResultCode(ResultCode.ALL_OK, okMessage, "Oferta actualizada satisfactoriamente");
        }

    }


    private void includeOfferInUser(TheOffer thatOffer) {
        String nickName = this.userManager.getUserNickNameFromSession();
        TheUser theUser = this.userManager.getUserFromNickname(nickName);
        if (theUser != null) {
            theUser.addOffer(thatOffer);
        }
    }
}
