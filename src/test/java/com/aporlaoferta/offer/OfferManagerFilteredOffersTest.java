package com.aporlaoferta.offer;

import com.aporlaoferta.data.FilterBuilderManager;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.model.OfferCategory;
import com.aporlaoferta.model.OfferFilters;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.TransactionalManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class OfferManagerFilteredOffersTest {

    @InjectMocks
    OfferManager offerManager;

    @Mock
    TransactionalManager transactionalManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        List<TheOffer> offerList = new ArrayList<>();
        offerList.add(OfferBuilderManager.aBasicOfferWithId(1L).build());
        when(this.transactionalManager.getNextHundredFilteredOffers(
                anyString(), anyString(), anyBoolean())
        ).thenReturn(offerList);
    }

    @Test
    public void testTransactionalManagerReturnsFilteredResults() {
        OfferFilters dummyFilters = createDummyOfferFilter();
        this.offerManager.getFilteredNextHundredResults(dummyFilters);
        verify(this.transactionalManager).getNextHundredFilteredOffers(
                dummyFilters.getSelectedcategory(),
                dummyFilters.getText(),
                dummyFilters.isExpired());
    }

    @Test
    public void testIfNoFiltersAreIncludedNewestOffersAreReturned() throws Exception {
        ArrayList<TheOffer> newestOffersOnly = new ArrayList<>();
        when(this.transactionalManager.getNextHundredOffers(0L)).thenReturn(newestOffersOnly);
        List<TheOffer> filteredResults = this.offerManager.getFilteredNextHundredResults(new OfferFilters());
        assertEquals(newestOffersOnly, filteredResults);
    }

    @Test
    public void testCategoryOnlyFilterReturnsAllOffersNoMatterWhatTitleTheyHave() throws Exception {
        OfferFilters categoryOnlyFilter = createCategoryOfferFilter();
        this.offerManager.getFilteredNextHundredResults(categoryOnlyFilter);
        verify(this.transactionalManager).getNextHundredCategoryFilteredOffers(
                categoryOnlyFilter.getSelectedcategory(), categoryOnlyFilter.isExpired());
    }

    @Test
    public void testTitleOnlyFilterReturnsAllOffersNoMatterTheirCategory() throws Exception {
        OfferFilters categoryOnlyFilter = createTextOfferFilter();
        this.offerManager.getFilteredNextHundredResults(categoryOnlyFilter);
        verify(this.transactionalManager).getNextHundredTextFilteredOffers(
                categoryOnlyFilter.getText(), categoryOnlyFilter.isExpired());
    }

    @Test
    public void testExpiredOnlyFilterReturnsAllOffers() throws Exception {
        OfferFilters expiredOnlyFilter = createExpiredOfferFilter();
        this.offerManager.getFilteredNextHundredResults(expiredOnlyFilter);
        verify(this.transactionalManager).getNextHundredExpiredFilteredOffers(
                expiredOnlyFilter.isExpired());
    }

    private OfferFilters createExpiredOfferFilter() {
        return FilterBuilderManager.anExpiredOnlyFilter(true).build();
    }

    private OfferFilters createTextOfferFilter() {
        return FilterBuilderManager.aTextOnlyFilter("intel").build();
    }

    private OfferFilters createCategoryOfferFilter() {
        return FilterBuilderManager.anCategoryOnlyFilter(OfferCategory.ELECTRONICS).build();
    }

    private OfferFilters createDummyOfferFilter() {
        return FilterBuilderManager.anAllElectronicsFilterWithText("intel").build();
    }

}


