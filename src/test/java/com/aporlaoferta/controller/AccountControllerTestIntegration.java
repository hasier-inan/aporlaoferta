package com.aporlaoferta.controller;


import com.aporlaoferta.data.UserBuilderManager;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

import static com.aporlaoferta.controller.SecurityRequestPostProcessors.userDeatilsService;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ContextConfiguration(
        loader = WebContextLoader.class,
        value = {
                "classpath:mvc-dispatcher-test-servlet.xml",
                "classpath:aporlaoferta-controller-test-context.xml"
        })
@RunWith(SpringJUnit4ClassRunner.class)
public class AccountControllerTestIntegration {

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

    ////// root

    @Test
    public void testAccessToRootPageIsGrantedToAnyUser() throws Exception {
        this.mockMvc.perform(get("/welcome"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(forwardedUrl("/view/hello.jsp"));
    }

    ////// admin

    @Test
    public void testForbiddenAccessToRegularUser() throws Exception {
        this.mockMvc.perform(get("/admin").with(userDeatilsService(REGULAR_USER)))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/403"));
    }

    @Test
    public void testForbiddenAccessToAnonymousUser() throws Exception {
        this.mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
        ;
    }

    @Test
    public void testAccessGrantedForAdminUser() throws Exception {
        this.mockMvc.perform(get("/admin").with(userDeatilsService(ADMIN_USER)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(forwardedUrl("/view/admin.jsp"));
    }

    ////// login

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

    @Test
    public void testLogoutMethodWorksAsExpected() {
        //TODO: add deploy and run project, test logout
    }

    ////// 403

    @Test
    public void testForbiddenEndpointAddsLoggedUserDataInModel() throws Exception {
        this.mockMvc.perform(get("/403").with(userDeatilsService(ADMIN_USER)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(forwardedUrl("/view/403.jsp"))
                .andExpect(model().attributeExists("username"));
    }

    @Test
    public void testForbiddenEndpointAddsNoUserDataInModelIfAnonymous() throws Exception {
        this.mockMvc.perform(get("/403"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(forwardedUrl("/view/403.jsp"))
                .andExpect(model().attributeDoesNotExist("username"));
    }

    ////// create user
    @Test
    public void testCreateUserIsNotRestrictedToAnonymousUsers() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(UserBuilderManager.aRegularUserWithNickname("moko").build());
        ResultActions result = this.mockMvc.perform(post("/createUser")
                //.with(userDeatilsService(REGULAR_USER))
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be ok", jsonResult.get("description"), startsWith("User successfully created"));
    }

    @Test
    public void testUpdateUserIsRestrictedToIdentifiedUsers() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(UserBuilderManager.aRegularUserWithNickname("moko").build());
        this.mockMvc.perform(post("/updateUser")
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
    public void testUpdateUserIsCorrectlyPerformedForRegularUser() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(UserBuilderManager.aRegularUserWithNickname(REGULAR_USER).build());
        ResultActions result = this.mockMvc.perform(post("/updateUser")
                .with(userDeatilsService(REGULAR_USER))
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be ok", jsonResult.get("description"), startsWith("User successfully updated"));
    }

}