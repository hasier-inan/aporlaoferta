package com.aporlaoferta.utils;

import com.aporlaoferta.data.CommentBuilderManager;
import com.aporlaoferta.data.CompanyBuilderManager;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.offer.CommentManager;
import com.aporlaoferta.offer.CompanyManager;
import com.aporlaoferta.offer.OfferManager;
import com.aporlaoferta.offer.UserManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

/**
 * Created by hasiermetal on 15/01/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestParameterParserTest {

    private static final String NICKNAME = "duckU";
    private static final Long THE_OFFER_ID = 2L;
    private static final Long THE_QUOTE_ID = 1L;
    private static final Long THE_QUOTED_QUOTE_ID = 5L;
    private static final Long THE_COMPANY_ID = 1L;
    private static final Long THE_QUOTED_COMMENT_OFFER_ID = 1L;

    RequestParameterParser requestParameterParser;

    @Mock
    OfferManager offerManager;
    @Mock
    UserManager userManager;
    @Mock
    CommentManager commentManager;
    @Mock
    CompanyManager companyManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.requestParameterParser = new RequestParameterParser();
        this.requestParameterParser.setOfferManager(this.offerManager);
        this.requestParameterParser.setUserManager(this.userManager);
        this.requestParameterParser.setCommentManager(this.commentManager);
        this.requestParameterParser.setCompanyManager(this.companyManager);
        Mockito.when(this.userManager.getUserFromNickname(NICKNAME)).thenReturn(UserBuilderManager.aRegularUserWithNickname(NICKNAME).build());
        Mockito.when(this.offerManager.getOfferFromId(THE_OFFER_ID)).thenReturn(OfferBuilderManager.aBasicOfferWithId(THE_OFFER_ID).build());
        Mockito.when(this.commentManager.getCommentFromId(THE_QUOTE_ID)).thenReturn(CommentBuilderManager.aCommentWithQuotedCommentAndId(CommentBuilderManager.aBasicCommentWithIdAndOffer(THE_QUOTED_COMMENT_OFFER_ID, THE_QUOTED_QUOTE_ID).build(), THE_QUOTE_ID).build());
        Mockito.when(this.companyManager.getCompanyFromId(THE_COMPANY_ID)).thenReturn(CompanyBuilderManager.aRegularCompanyWithId(THE_COMPANY_ID).build());
        Mockito.when(this.companyManager.createAffiliationLink(any(OfferCompany.class), any(String.class))).thenReturn("mockedaffiliationlink");
    }

    ///////COMMENT
    @Test
    public void testCommentRawMapIsCorrectlyParsed() {
        OfferComment offerComment = CommentBuilderManager.aBasicCommentWithoutId().build();
        this.requestParameterParser.parseOfferCommentRawMap(offerComment, String.valueOf(THE_OFFER_ID), NICKNAME);

        assertThat("Expected to be same nickname", offerComment.getCommentOwner().getUserNickname(),
                is(NICKNAME));
        assertThat("Expected to be same offer id", offerComment.getCommentsOffer().getId(), is(THE_OFFER_ID));
        assertThat("Expected to be same text value", offerComment.getCommentText(),
                is(CommentBuilderManager.aBasicCommentWithoutId().build().getCommentText()));
        assertNotNull(offerComment.getCommentCreationDate());
        //id still not assigned
        assertNull(offerComment.getId());
    }

    @Test
    public void testCommentQuoteRawMapIsCorrectlyParsed() {
        OfferComment offerComment = CommentBuilderManager.aBasicCommentWithoutId().build();
        this.requestParameterParser.parseOfferQuoteRawMap(offerComment, String.valueOf(THE_QUOTE_ID), NICKNAME);

        assertThat("Expected to be same nickname", offerComment.getCommentOwner().getUserNickname(),
                is(NICKNAME));
        assertThat("Expected to be same offer id", offerComment.getCommentsOffer().getId(), is(THE_QUOTED_COMMENT_OFFER_ID));
        assertThat("Expected to be same text value", offerComment.getCommentText(),
                is(CommentBuilderManager.aBasicCommentWithoutId().build().getCommentText()));
        assertNotNull(offerComment.getCommentCreationDate());
        //id still not assigned
        assertNull(offerComment.getId());
    }

    ///////OFFER
    @Test
    public void testOfferRawMapIsCorrectlyParsed() {
        /*Map<String, String> basicOfferMap = RequestMap.createBasicOfferMap();
        TheOffer theOffer = this.requestParameterParser.parseOfferRawMap(basicOfferMap, NICKNAME);
        assertThat("Expected to be electronics category", theOffer.getOfferCategory(), is(OfferCategory.ELECTRONICS));
        assertThat("Expected to be same user owner", theOffer.getOfferUser().getUserNickname(), is(NICKNAME));
        assertThat("Expected same company", theOffer.getOfferCompany().getId(), is(THE_COMPANY_ID));
        assertThat("Expected same description", theOffer.getOfferDescription(), is("This is the best deal in da world"));
        assertFalse(theOffer.isOfferExpired());
        assertThat("Expected same image url", theOffer.getOfferImage(), is("this.image.has/been/previously/uploaded.to.the.server.jpg"));
        assertThat("Expected same raw url with affiliation id", theOffer.getOfferLink(), is("mockedaffiliationlink"));//not http://this.isthedirecturl.without/affiliateId
        assertThat("Expected same title", theOffer.getOfferTitle(), is("The Amazing deal"));
        assertTrue(theOffer.getOfferPositiveVote() == 0);
        assertTrue(theOffer.getOfferNegativeVote() == 0);
        assertNotNull(theOffer.getOfferCreatedDate());
        assertThat("Expected same original price", new BigDecimal(theOffer.getOriginalPrice().toString()), is(new BigDecimal(30)));
        assertThat("Expected same final price", new BigDecimal(theOffer.getFinalPrice().toString()), is(new BigDecimal(20)));*/
    }

    @Test
    public void testIllegalArgumetsAreSetToNull() {
        Map<String, String> basicOfferMap = new HashMap<>();
        basicOfferMap.put(MappingEntries.CATEGORY.getCode(), "unexistingcategory");
        TheOffer theOffer = this.requestParameterParser.parseOfferRawMap(basicOfferMap, null);
        assertNull(theOffer.getOfferCategory());
        assertNull(theOffer.getOfferUser());
        assertNull(theOffer.getOriginalPrice());
        assertNull(theOffer.getFinalPrice());
        assertNull(theOffer.getOfferCompany());
    }

    @Test
    public void testAffiliationIdIsIncludedInFinalLink() {
        String affiliateID = "aporlaoferta";
        OfferCompany genericCompanyWithGetAffiliate = CompanyBuilderManager.aRegularCompanyWithName("muttta").build();
        genericCompanyWithGetAffiliate.setCompanyAffiliateId(affiliateID);
        CompanyManager companyManager = new CompanyManager();
        String rawLink = "this//is.the.generic.link/with.somefucking.shit?like=this?and?this";
        String affiliateId = companyManager.createAffiliationLink(genericCompanyWithGetAffiliate, rawLink);
        assertThat("Expected link to include affiliate id", affiliateId, is(rawLink + "&affiliate=" + affiliateID));
    }

}
