package com.aporlaoferta.offer;

import com.aporlaoferta.data.CommentBuilderManager;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.service.CommentManager;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.UserManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
public class CommentManagerTestIntegration {

    private static final Long THE_ID = 1L;

    @Autowired
    OfferManager offerManager;
    @Autowired
    UserManager userManager;

    @Autowired
    private CommentManager commentManager;

    @Test
    public void testAllCommentsForAnOfferAreObtained() {
        TheUser theUser = this.userManager.createUser(
                UserBuilderManager.aRegularUserWithNickname("sdljf")
                        .build()
        );
        TheOffer theOffer = (
                OfferBuilderManager.aBasicOfferWithoutUserOrId()
                        .withId(THE_ID)
                        .build()
        );
        theUser.addOffer(theOffer);
        this.offerManager.createOffer(theOffer);

        addSomeDummyCommentsToSameOffer(theOffer, 200, 20);
        assertMultipleCommentsAreCorrectlyManaged();
    }

    private void assertMultipleCommentsAreCorrectlyManaged() {
        List<OfferComment> offerComments = this.commentManager.getFirstHundredCommentsForOffer(1L, THE_ID);
        assertThat("Expected the newest comment id to be 200", offerComments.get(0).getId(), is(199L));
        //get older 100 comments (after last 'shown' id)
        List<OfferComment> nextOfferComments = this.commentManager.getFirstHundredCommentsForOffer(101L, THE_ID);
        assertThat("Expected the newest comment id to be 100", nextOfferComments.get(0).getId(), is(99L));

        List<OfferComment> emptyOfferComments = this.commentManager.getFirstHundredCommentsForOffer(201L, THE_ID);
        assertThat("Expected empty lsit as all comments have been displayed", emptyOfferComments.size(), is(0));
    }

    private void addSomeDummyCommentsToSameOffer(TheOffer theOffer, int howManyComments, int sleepTime) {
        for (int i = 0; i < howManyComments; i++) {
            addCommentToDB(theOffer);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void addCommentToDB(TheOffer theOffer) {
        OfferComment aOfferComment = CommentBuilderManager.aBasicCommentWithoutId().build();
        TheUser offerUser = theOffer.getOfferUser();
        offerUser.addComment(aOfferComment);
        theOffer.addComment(aOfferComment);
        this.commentManager.saveComment(aOfferComment);
    }
}

