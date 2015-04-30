package com.aporlaoferta.controller;

import com.aporlaoferta.data.CompanyBuilder;
import com.aporlaoferta.data.CompanyBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.rawmap.RequestMap;
import com.aporlaoferta.service.UserManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static com.aporlaoferta.controller.SecurityRequestPostProcessors.userDeatilsService;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
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