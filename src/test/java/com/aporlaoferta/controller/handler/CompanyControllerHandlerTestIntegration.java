package com.aporlaoferta.controller.handler;

import com.aporlaoferta.controller.ResultCode;
import com.aporlaoferta.data.CompanyBuilder;
import com.aporlaoferta.data.CompanyBuilderManager;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.rawmap.RequestMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

import static org.junit.Assert.assertTrue;

@ContextConfiguration(
        value = {
                "classpath:mvc-dispatcher-test-servlet.xml",
                "classpath:aporlaoferta-controller-test-context.xml"
        })
@RunWith(SpringJUnit4ClassRunner.class)
public class CompanyControllerHandlerTestIntegration {

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;


    @Test
    public void testCreateCompanyWithBasicDataReturnsCorrectResult() throws Exception {
        MockHttpServletRequest httpRequest = new MockHttpServletRequest("POST", "/createCompany");
        httpRequest.setContent(RequestMap.getJsonFromMap(CompanyBuilderManager.aRegularCompanyWithName("sdf").build()).getBytes());
        httpRequest.setContentType("application/json");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = this.handlerMapping.getHandler(httpRequest).getHandler();
        handlerAdapter.handle(httpRequest, response, handler);
        Map<String, String> responseResult = RequestMap.getMapFromJsonString(response.getContentAsString());
        assertTrue(responseResult.get("code").equals("0"));
    }

    @Test
    public void testCreateCompanyWithMissingDataReturnsExpectedResultMap() throws Exception {
        MockHttpServletRequest httpRequest = new MockHttpServletRequest("POST", "/createCompany");
        OfferCompany basicCompanyMapWithMissingData = CompanyBuilder.aCompany().build();
        httpRequest.setContent(RequestMap.getJsonFromMap(basicCompanyMapWithMissingData).getBytes());
        httpRequest.setContentType("application/json");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = this.handlerMapping.getHandler(httpRequest).getHandler();
        handlerAdapter.handle(httpRequest, response, handler);
        Map<String, String> responseResult = RequestMap.getMapFromJsonString(response.getContentAsString());
        assertTrue(responseResult.get("code").equals(
                String.valueOf(ResultCode.CREATE_COMPANY_VALIDATION_ERROR.getCode())));
    }

}