package com.aporlaoferta.controller.handler;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static org.junit.Assert.assertTrue;


@ContextConfiguration(
        loader = WebContextLoader.class,
        value = {
                "classpath:mvc-dispatcher-test-servlet.xml",
                "classpath:aporlaoferta-controller-test-context.xml"
        })
@RunWith(SpringJUnit4ClassRunner.class)
public class AccountControllerHandlerTestIntegration {

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Test
    public void testLoginRequestWithErrorAddsMessage() throws Exception {
        MockHttpServletRequest httpRequest = new MockHttpServletRequest("GET", "/login");
        httpRequest.addParameter("error", "");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = this.handlerMapping.getHandler(httpRequest).getHandler();
        ModelAndView modelAndView = handlerAdapter.handle(httpRequest, response, handler);
        assertTrue(modelAndView.getModel().size() == 1);
        assertTrue(modelAndView.getViewName().equals("login"));
        String error = (String) modelAndView.getModel().get("error");
        assertTrue(error.equals("Invalid username and password!"));
    }

    @Test
    public void testLoginRequestWithLogoutAddsMessage() throws Exception {
        MockHttpServletRequest httpRequest = new MockHttpServletRequest("GET", "/login");
        httpRequest.addParameter("logout", "");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = this.handlerMapping.getHandler(httpRequest).getHandler();
        ModelAndView modelAndView = handlerAdapter.handle(httpRequest, response, handler);
        assertTrue(modelAndView.getModel().size() == 1);
        assertTrue(modelAndView.getViewName().equals("login"));
        String error = (String) modelAndView.getModel().get("msg");
        assertTrue(error.equals("You've been logged out successfully."));
    }

}