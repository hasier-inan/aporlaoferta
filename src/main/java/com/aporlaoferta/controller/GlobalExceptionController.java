package com.aporlaoferta.controller;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 23/03/2017
 * Time: 18:25
 */
@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView handleError405(HttpServletRequest request, Exception e)   {
        ModelAndView model = new ModelAndView();
        model.setViewName("404");
        return model;
    }
}
