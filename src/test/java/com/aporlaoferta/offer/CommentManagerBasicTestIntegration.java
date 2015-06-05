package com.aporlaoferta.offer;

import com.aporlaoferta.dao.CommentDAO;
import com.aporlaoferta.data.CommentBuilderManager;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.service.CommentManager;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.TransactionalManager;
import com.aporlaoferta.service.UserManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:aporlaoferta-inmemory-test-context.xml",
        "classpath:aporlaoferta-managers-test-context.xml"})
@Transactional
public class CommentManagerBasicTestIntegration {

    private static final Long THE_ID = 1L;

    @Autowired
    CommentDAO commentDAO;

    @Autowired
    OfferManager offerManager;
    @Autowired
    UserManager userManager;

    @Autowired
    private CommentManager commentManager;

    @Autowired
    private TransactionalManager transactionalManager;

    private TheUser theUser;

    @Before
    public void setUp() {
        TheUser theUserLocal = this.transactionalManager.saveUser(UserBuilderManager.aRegularUserWithNickname(UUID.randomUUID().toString()).build());
        TheOffer theOfferLocal = OfferBuilderManager.aBasicOfferWithoutUserOrId().build();
        theUserLocal.addOffer(theOfferLocal);
        this.theUser = this.transactionalManager.saveUser(theUserLocal);
    }

    @Test
    public void testCommentIsObtainedFromDB() {
        OfferComment offerComment = CommentBuilderManager.aBasicCommentWithId(THE_ID).build();
        TheOffer theOffer = OfferBuilderManager.aBasicOfferWithoutUserOrId().build();
        theOffer.addComment(offerComment);
        this.theUser.addOffer(theOffer);
        this.theUser.addComment(offerComment);
        this.offerManager.createOffer(theOffer);
        this.commentManager.saveComment(offerComment);
        assertThat("Expected only one comment to be in the db", this.commentDAO.count(), is(1L));
        OfferComment comment = this.commentManager.getCommentFromId(THE_ID);
        assertNotNull(comment);
        assertThat("Expected the comment id to be 1", comment.getId(), is(1L));
        assertThat("Expected same comment", comment.getCommentText(), is(offerComment.getCommentText()));
    }

    @Test
    public void testANewCommentIsCorrectlyAssignedToItsOwner() {
        TheUser theUser = this.userManager.createUser(
                UserBuilderManager.aRegularUserWithNickname("iddqd")
                        .build()
        );
        TheOffer theOffer = this.offerManager.createOffer(
                OfferBuilderManager.aBasicOfferWithoutId().withUser(theUser)
                        .build()
        );
        OfferComment offerComment = this.commentManager.saveComment(
                CommentBuilderManager.aBasicCommentWithoutId()
                        .withOwner(theUser)
                        .withCommentedOffer(theOffer)
                        .build()
        );
        assertNotNull(offerComment.getCommentOwner());
    }


}

