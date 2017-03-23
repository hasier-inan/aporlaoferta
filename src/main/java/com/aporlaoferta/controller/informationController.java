package com.aporlaoferta.controller;

import com.aporlaoferta.model.TheResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    public ModelAndView start(@RequestParam(value = "cookies-accepted", required = false) Boolean acceptedCookies) {
        ModelAndView model = new ModelAndView();
        model.setViewName("index");
        model.addObject("cookiesAccepted", acceptedCookies);
        return model;
    }

    @RequestMapping(value = "/healthcheck**", method = RequestMethod.GET)
    public ModelAndView healthCheck() {
        return getFixedView("healthcheck");
    }

    private ModelAndView getFixedView(String templateName) {
        ModelAndView model = new ModelAndView();
        model.setViewName(templateName);
        return model;
    }
}
