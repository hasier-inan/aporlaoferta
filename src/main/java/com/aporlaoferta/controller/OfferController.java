package com.aporlaoferta.controller;

import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.validators.ValidationException;
import com.aporlaoferta.service.UserManager;
import com.aporlaoferta.utils.OfferValidatorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "/findOffer", method = RequestMethod.POST)
    @ResponseBody
    public List<TheOffer> findOfferByText(@RequestParam(value = "input", required = true) String inputText,
                                          ModelMap model) {
        return new ArrayList<>();
    }

    /**
     * Will return a list of offers sorted by creation date.
     * By default front-end should only show not expired offers, and should allow to check/uncheck different categories
     * to sort them out.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/lastOfferList", method = RequestMethod.POST)
    @ResponseBody
    public List<TheOffer> lastOfferList(ModelMap model) {
        return new ArrayList<>();
    }

    /**
     * Creates a brand new offer from the input mapped object
     *
     * @param thatOffer
     * @param model
     */
    @RequestMapping(value = "/createOffer", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> createOffer(@RequestParam(required = true) Map<String, String> thatOffer,
                                           ModelMap model) {
        //TODO: add front-end validation so that mandatory fields can be already there. In any case, add validation
        // process in the backend as well.
        Map<String, String> result = new HashMap<>();
        try {
            TheOffer theOffer = null;/*this.requestParameterParser.parseOfferRawMap(thatOffer,
                    this.userManager.getUserNickNameFromSession());*/
            this.offerValidatorHelper.validateOffer(theOffer);

        } catch (ValidationException e) {
            String resultDescription = ResultCode.CREATE_OFFER_VALIDATION_ERROR.getResultDescription();
            LOG.warn(resultDescription, e);
            result.put(ResultCode.CREATE_OFFER_VALIDATION_ERROR.getCodeAndResultKey(), resultDescription);
        }
        return result;
    }

}
