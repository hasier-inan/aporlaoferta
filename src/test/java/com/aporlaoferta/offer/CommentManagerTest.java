package com.aporlaoferta.offer;

import com.aporlaoferta.dao.CommentDAO;
import com.aporlaoferta.data.CommentBuilderManager;
import com.aporlaoferta.model.OfferComment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class CommentManagerTest {

    private static final Long THE_ID = 2L;
    @InjectMocks
    CommentManager commentManager;

    @Mock
    CommentDAO commentDAO;

    OfferComment offerComment;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.offerComment = CommentBuilderManager.aBasicCommentWithId(THE_ID).build();
        Mockito.when(this.commentDAO.findOne(THE_ID)).thenReturn(offerComment);
        Mockito.when(this.commentDAO.getOneHundredCommentsAfterId(anyLong(), anyLong()))
                .thenReturn(new ArrayList<OfferComment>());
    }

    @Test
    public void testCommentDaoFindsOneUniqueCommentFromId() {
        assertNotNull(this.commentManager.getCommentFromId(THE_ID));
        verify(this.commentDAO).findOne(THE_ID);
    }

    @Test
    public void testCommentIsCreatedUsingDaos() {
        this.commentManager.createComment(this.offerComment);
        verify(this.commentDAO).save(this.offerComment);
    }

    @Test
    public void testHundredCommentsAreObtainedUsingCustomQuery() {
        this.commentManager.getFirstHundredCommentsForOffer(0L, 1L);
        verify(this.commentDAO).getOneHundredCommentsAfterId(0L, 1L);
    }

}

