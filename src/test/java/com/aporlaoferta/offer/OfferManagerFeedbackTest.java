package com.aporlaoferta.offer;

import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.TransactionalManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class OfferManagerFeedbackTest {

    @InjectMocks
    OfferManager offerManager;

    @Mock
    TransactionalManager transactionalManager;

    TheOffer theOffer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(this.transactionalManager.getOfferFromId(anyLong())).thenReturn(this.theOffer);
        when(this.transactionalManager.saveOffer(any(TheOffer.class))).thenReturn(this.theOffer);
    }

    @Test
    public void testFeedbackIsInitialisedWithZero() throws Exception {
        TheOffer basicOffer = OfferBuilderManager.aBasicOfferWithoutId().build();
        Assert.assertThat("Expected positive feedback value to be zero", basicOffer.getOfferPositiveVote(), is(0L));
        Assert.assertThat("Expected negative feedback value to be zero", basicOffer.getOfferNegativeVote(), is(0L));
    }

    @Test
    public void testPositiveFeedbackIncreasesCounter() throws Exception {
        ArgumentCaptor<TheOffer> offerCapture = ArgumentCaptor.forClass(TheOffer.class);
        TheOffer basicOffer = OfferBuilderManager.aBasicOfferWithoutId().build();
        Long initialFeedback = basicOffer.getOfferPositiveVote();
        this.offerManager.positiveFeedback(basicOffer);
        verify(this.transactionalManager).saveOffer(offerCapture.capture());
        TheOffer persistedOffer = offerCapture.getValue();
        Assert.assertThat("Expected positive feedback to have been increased",
                persistedOffer.getOfferPositiveVote(),
                greaterThan(initialFeedback));
    }

    @Test
    public void testNegativeFeedbackIncreasesCounter() throws Exception {
        ArgumentCaptor<TheOffer> offerCapture = ArgumentCaptor.forClass(TheOffer.class);
        TheOffer basicOffer = OfferBuilderManager.aBasicOfferWithoutId().build();
        Long initialFeedback = basicOffer.getOfferNegativeVote();
        this.offerManager.negativeFeedback(basicOffer);
        verify(this.transactionalManager).saveOffer(offerCapture.capture());
        TheOffer persistedOffer = offerCapture.getValue();
        Assert.assertThat("Expected negative feedback to have been increased",
                persistedOffer.getOfferNegativeVote(),
                greaterThan(initialFeedback));
    }

}


