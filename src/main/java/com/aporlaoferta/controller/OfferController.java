package com.aporlaoferta.controller;

import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheOfferResponse;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.validators.ValidationException;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.UserManager;
import com.aporlaoferta.utils.OfferValidatorHelper;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hasiermetal on 2/02/14.
 */
@Controller
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
//TODO: Move @Transaction to a service
public class OfferController {

    private final Logger LOG = LoggerFactory.getLogger(OfferController.class);

    @Autowired
    OfferValidatorHelper offerValidatorHelper;

    @Autowired
    UserManager userManager;

    @Autowired
    OfferManager offerManager;

    @RequestMapping(value = "/getOffers", method = RequestMethod.POST)
    @ResponseBody
    public TheOfferResponse getOffers(@RequestParam(value = "number", required = false) Long number) {

        TheOfferResponse theOfferResponse = new TheOfferResponse();
        List<TheOffer> hundredOffers = this.offerManager.getNextHundredOffers(number != null ? number : 0L);
        //Temporary hack as because of the Lazy initialisation comments are empty...
        createEmptyFields(hundredOffers);
        theOfferResponse.setTheOffers(hundredOffers);
        updateResponseWithSuccessCode(theOfferResponse);
        return theOfferResponse;
    }

    private void createEmptyFields(List<TheOffer> hundredOffers) {
        for (TheOffer theOffer : hundredOffers) {
            theOffer.setOfferComments(new HashSet<OfferComment>());
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

    @RequestMapping(value = "/getOffer", method = RequestMethod.GET)
    @ResponseBody
    public TheOfferResponse getOfferById(@RequestParam(value = "id", required = true) Long number) {
        TheOfferResponse theOfferResponse = new TheOfferResponse();
        TheOffer theOffer = this.offerManager.getOfferFromId(number);
        resetUserSensibleDataFromOffer(theOffer);
        Set<OfferComment> offerComments = theOffer.getOfferComments();
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
    public TheResponse createOffer(@RequestBody TheOffer thatOffer) {
        includeOfferInUser(thatOffer);
        return validateAndPersistOffer(thatOffer);
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

    private void updateResponseWithSuccessCode(TheOfferResponse theOfferResponse) {
        theOfferResponse.setCode(ResultCode.ALL_OK.getCode());
        theOfferResponse.setDescription(ResultCode.ALL_OK.getResultDescription());
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
        originalOffer.setOfferCompany(theUpdatedOffer.getOfferCompany());
        originalOffer.setOfferCategory(theUpdatedOffer.getOfferCategory());
        originalOffer.setOfferDescription(theUpdatedOffer.getOfferDescription());
        originalOffer.setOfferExpired(theUpdatedOffer.isOfferExpired());
        originalOffer.setOfferImage(theUpdatedOffer.getOfferImage());
        originalOffer.setOfferLink(theUpdatedOffer.getOfferLink());
        originalOffer.setOriginalPrice(theUpdatedOffer.getOriginalPrice());
        return originalOffer;
    }

    private TheResponse validateAndPersistOffer(TheOffer thatOffer) {
        TheResponse result = new TheResponse();
        try {
            this.offerValidatorHelper.validateOffer(thatOffer);
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
            result.assignResultCode(ResultCode.ALL_OK, okMessage);
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
