package com.aporlaoferta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by hasiermetal on 18/01/15.
 */
@Controller
public class informationController {

    @RequestMapping("favicon.ico")
    String favicon() {
        return "forward:/resources/images/favicon.ico";
    }

    @RequestMapping(value = {"/tc**"}, method = RequestMethod.GET)
    public ModelAndView termsAndConditions() {
        return getFixedView("termsAndConditions");
    }

    @RequestMapping(value = {"/cp"}, method = RequestMethod.GET)
    public ModelAndView cookiePolicies() {
        return getFixedView("cookieTerms");
    }

    @RequestMapping(value = {"/faq**"}, method = RequestMethod.GET)
    public ModelAndView frequentlyAskedQuestions() {
        return getFixedView("faq");
    }

    @RequestMapping(value = {"/", "/start**", "/index**"}, method = RequestMethod.GET)
    public ModelAndView start() {
        return getFixedView("index");
    }

    private ModelAndView getFixedView(String templateName) {
        ModelAndView model = new ModelAndView();
        model.setViewName(templateName);
        return model;
    }
}
