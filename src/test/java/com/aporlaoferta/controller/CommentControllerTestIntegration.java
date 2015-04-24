package com.aporlaoferta.controller;


import com.aporlaoferta.data.CommentBuilderManager;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.rawmap.RequestMap;
import com.aporlaoferta.service.OfferManager;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.aporlaoferta.controller.SecurityRequestPostProcessors.userDeatilsService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ContextConfiguration(
        value = {
                "classpath:mvc-dispatcher-test-servlet.xml",
                "classpath:aporlaoferta-controller-test-context.xml"
        })
@RunWith(SpringJUnit4ClassRunner.class)
public class CommentControllerTestIntegration {

    private static final String REGULAR_USER = "regularUser";

    @Autowired
    UserManager userManagerTest;

    @Autowired
    OfferManager offerManagerTest;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private TheUser theUser;

    @Before
    public void setup() {
        if (!this.userManagerTest.doesUserExist(REGULAR_USER)) {
            this.theUser = this.userManagerTest.createUser(UserBuilderManager.aRegularUserWithNickname(REGULAR_USER).build());
        } else {
            this.theUser = this.userManagerTest.getUserFromNickname(REGULAR_USER);
        }
        this.theUser.addOffer(OfferBuilderManager.aBasicOfferWithoutId().build());
        this.theUser = this.userManagerTest.saveUser(theUser);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilters(this.springSecurityFilterChain).build();
    }

    @Test
    public void testCommentCreationIsOnlyAllowedIfIdentified() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(CommentBuilderManager.aBasicCommentWithId(2L).build());
        TheOffer theOffer = theUser.obtainLatestOffer();
        String offerId = String.valueOf(theOffer.getId());
        this.mockMvc.perform(post("/createComment")
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("offer", offerId)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testValidationExceptionOccursIfOfferIsNotFound() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(CommentBuilderManager.aBasicCommentWithoutId().build());
        TheOffer theOffer = theUser.obtainLatestOffer();
        String offerId = String.valueOf(theOffer.getId());
        this.mockMvc.perform(post("/createComment")
                .with(userDeatilsService(REGULAR_USER))
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("offer", offerId)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testValidationExceptionOccursIfMandatoryFieldsAreNotIncluded() {

    }

    @Test
    public void testCommentIsCorrectlyCreatedForRegularUser() {

    }

    //////Update

}