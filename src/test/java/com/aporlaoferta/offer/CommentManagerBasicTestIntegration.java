package com.aporlaoferta.offer;

import com.aporlaoferta.dao.CommentDAO;
import com.aporlaoferta.data.CommentBuilderManager;
import com.aporlaoferta.model.OfferComment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    public void testCommentIsObtainedFromDB() {
        OfferComment offerComment = CommentBuilderManager.aBasicCommentWithId(THE_ID).build();
        this.commentManager.createComment(offerComment);
        assertThat("Expected only one comment to be in the db", this.commentDAO.count(), is(1L));
        OfferComment comment = this.commentManager.getCommentFromId(THE_ID);
        assertNotNull(comment);
        assertThat("Expected the comment id to be 1", comment.getId(), is(1L));
        assertThat("Expected same comment", comment.getCommentText(), is(offerComment.getCommentText()));
    }

}

