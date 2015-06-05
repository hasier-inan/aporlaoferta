package com.aporlaoferta.offer;

import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.TransactionalManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class OfferManagerTest {

    private static final Long THE_ID = 1L;

    @InjectMocks
    OfferManager offerManager;

    @Mock
    TransactionalManager transactionalManager;

    TheOffer theOffer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.theOffer = OfferBuilderManager.aBasicOfferWithId(THE_ID).build();
        Mockito.when(this.transactionalManager.getOfferFromId(THE_ID)).thenReturn(this.theOffer);
        Mockito.when(this.transactionalManager.saveOffer(any(TheOffer.class))).thenReturn(this.theOffer);
    }

    @Test
    public void testOfferDaoFindsOneUniqueUserFromNickname() {
        assertNotNull(this.offerManager.getOfferFromId(THE_ID));
        verify(this.transactionalManager).getOfferFromId(THE_ID);
    }

    @Test
    public void testInvalidIdThrowsExpectedExceptionAndThusReturnsNull() {
        assertNull(this.offerManager.getOfferFromId(null));
    }

    @Test
    public void testOfferIsCreatedUsingDaos() {
        this.offerManager.createOffer(this.theOffer);
        verify(this.transactionalManager).saveOffer(this.theOffer);
    }

    @Test
    public void testOfferExpireUpdatesDomainObject() {
        TheOffer theOffer1 = this.offerManager.expireOffer(this.theOffer);
        assertTrue("Expected offer to be expired: ", theOffer1.isOfferExpired());
    }

    @Test
    public void testOneHundredOffersIsCalledInTheManager() {
        this.offerManager.getNextHundredOffers(THE_ID);
        verify(this.transactionalManager).getNextHundredOffers(THE_ID);
    }

}


