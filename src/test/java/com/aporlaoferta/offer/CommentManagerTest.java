package com.aporlaoferta.offer;

import com.aporlaoferta.data.CommentBuilderManager;
import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.service.CommentManager;
import com.aporlaoferta.service.TransactionalManager;
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
    TransactionalManager transactionalManager;

    OfferComment offerComment;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.offerComment = CommentBuilderManager.aBasicCommentWithId(THE_ID).build();
        Mockito.when(this.transactionalManager.getCommentFromId(THE_ID)).thenReturn(offerComment);
        Mockito.when(this.transactionalManager.getFirstHundredCommentsForOffer(anyLong(), anyLong()))
                .thenReturn(new ArrayList<OfferComment>());
    }

    @Test
    public void testCommentDaoFindsOneUniqueCommentFromId() {
        assertNotNull(this.commentManager.getCommentFromId(THE_ID));
        verify(this.transactionalManager).getCommentFromId(THE_ID);
    }

    @Test
    public void testCommentIsCreatedUsingDaos() {
        this.commentManager.saveComment(this.offerComment);
        verify(this.transactionalManager).saveComment(this.offerComment);
    }

    @Test
    public void testHundredCommentsAreObtainedUsingCustomQuery() {
        this.commentManager.getFirstHundredCommentsForOffer(0L, 1L);
        verify(this.transactionalManager).getFirstHundredCommentsForOffer(0L, 1L);
    }

}

