package com.aporlaoferta.controller;

import com.aporlaoferta.utils.OfferValidatorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hasiermetal on 2/02/14.
 */
@Controller
public class FeedbackController {

    @Autowired
    OfferValidatorHelper offerValidatorHelper;

    @RequestMapping(value = "/votePositive", method = RequestMethod.POST)
    @ResponseBody
    public void votePositive(@RequestParam(value = "offer", required = true) String offerId, ModelMap model) {

    }

    @RequestMapping(value = "/voteNegative", method = RequestMethod.POST)
    @ResponseBody
    public void voteNegative(@RequestParam(value = "offer", required = true) String offerId, ModelMap model) {

    }

}
