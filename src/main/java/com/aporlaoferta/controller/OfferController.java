package com.aporlaoferta.controller;

import com.aporlaoferta.model.TheOffer;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hasiermetal on 2/02/14.
 */
@Controller
public class OfferController {

    private final Logger LOG = LoggerFactory.getLogger(OfferController.class);

    @Autowired
    OfferValidatorHelper offerValidatorHelper;

    @Autowired
    UserManager userManager;

    @Autowired
    OfferManager offerManager;

    @RequestMapping(value = "/createOffer", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse createOffer(@RequestBody TheOffer thatOffer) {
        includeOfferInUser(thatOffer);
        return validateAndPersistOffer(thatOffer);
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
            String okMessage = String.format("Offer successfully created. Id: %s", theOffer.getId());
            LOG.info(okMessage);
            result.assignResultCode(ResultCode.ALL_OK, okMessage);
        }

    }

    private void includeOfferInUser(TheOffer thatOffer) {
        String nickName = this.userManager.getUserNickNameFromSession();
        TheUser theUser = this.userManager.getUserFromNickname(nickName);
        theUser.addOffer(thatOffer);
    }

}
