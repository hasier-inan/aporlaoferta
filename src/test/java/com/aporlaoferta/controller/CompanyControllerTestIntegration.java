package com.aporlaoferta.controller;

import com.aporlaoferta.data.CompanyBuilder;
import com.aporlaoferta.data.CompanyBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.offer.UserManager;
import com.aporlaoferta.rawmap.RequestMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

import static com.aporlaoferta.controller.SecurityRequestPostProcessors.userDeatilsService;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(
        loader = WebContextLoader.class,
        value = {
                "classpath:mvc-dispatcher-test-servlet.xml",
                "classpath:aporlaoferta-controller-test-context.xml"
        })
@RunWith(SpringJUnit4ClassRunner.class)
public class CompanyControllerTestIntegration {

    private static final String REGULAR_USER = "regularUser";
    private static final String ADMIN_USER = "imtheboss";

    @Autowired
    UserManager userManagerTest;

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        if (!this.userManagerTest.doesUserExist(REGULAR_USER)) {
            this.userManagerTest.createUser(UserBuilderManager.aRegularUserWithNickname(REGULAR_USER).build());
        }
        if (!this.userManagerTest.doesUserExist(ADMIN_USER)) {
            this.userManagerTest.createAdminUser(UserBuilderManager.aRegularUserWithNickname(ADMIN_USER).build());
        }
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilters(this.springSecurityFilterChain).build();
    }

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

    @Test
    public void testCompanyCreationIsRestrictedToAdminUsers() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(CompanyBuilder.aCompany().build());
        this.mockMvc.perform(post("/createCompany")
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testCompanyCreationIsRestrictedToAdminUsersNoRegular() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(CompanyBuilder.aCompany().build());
        this.mockMvc.perform(post("/createCompany")
                .with(userDeatilsService(REGULAR_USER))
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCompanyCreationIsCorrectlyCreatedIfAdmin() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(CompanyBuilderManager.aRegularCompanyWithName("sdfasdf").build());
        ResultActions respose = this.mockMvc.perform(post("/createCompany")
                .with(userDeatilsService(ADMIN_USER))
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is2xxSuccessful());
        String rawResult = respose.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(rawResult);
        assertThat("Expected unique company ", jsonResult.get("description"), startsWith("Company successfully created"));
    }

    @Test
    public void testNoNeedToBeIdentifiedToGetCompanyList() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        this.mockMvc.perform(post("/companyList")
                .sessionAttr("_csrf", csrfToken)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is2xxSuccessful());
    }

}