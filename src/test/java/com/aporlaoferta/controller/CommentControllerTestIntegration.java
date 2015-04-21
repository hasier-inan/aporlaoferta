package com.aporlaoferta.controller;


import com.aporlaoferta.data.CommentBuilderManager;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.offer.OfferManager;
import com.aporlaoferta.offer.UserManager;
import com.aporlaoferta.rawmap.RequestMap;
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
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

import static com.aporlaoferta.controller.SecurityRequestPostProcessors.userDeatilsService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ContextConfiguration(
        loader = WebContextLoader.class,
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
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private TheOffer theOffer;

    @Before
    public void setup() {
        TheUser theUser;
        if (!this.userManagerTest.doesUserExist(REGULAR_USER)) {
            theUser = this.userManagerTest.createUser(UserBuilderManager.aRegularUserWithNickname(REGULAR_USER).build());
        } else {
            theUser = this.userManagerTest.getUserFromNickname(REGULAR_USER);
        }
        TheOffer offer = OfferBuilderManager.aBasicOfferWithoutId()
                .build();
        theUser.addOffer(offer);
        theUser=this.userManagerTest.saveUser(theUser);
        List<TheOffer> theOfferList= (List<TheOffer>) theUser.getUserOffers();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilters(this.springSecurityFilterChain).build();
    }

    @Test
    public void testCommentCreationIsOnlyAllowedIfIdentified() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(CommentBuilderManager.aBasicCommentWithId(2L).build());
        String offerId = String.valueOf(this.theOffer.getId());
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
        String jsonRequest = RequestMap.getJsonFromMap(CommentBuilderManager.aBasicCommentWithIdAndOffer(6L, 7L).build());
        String offerId = String.valueOf(this.theOffer.getId());
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