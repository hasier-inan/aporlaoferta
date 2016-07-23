package com.aporlaoferta.offer;

import com.aporlaoferta.data.FilterBuilderManager;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.model.DateRange;
import com.aporlaoferta.model.OfferCategory;
import com.aporlaoferta.model.OfferFilters;
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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
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
    public void setUp() throws ParseException {
        MockitoAnnotations.initMocks(this);
        List<TheOffer> offerList = new ArrayList<>();
        offerList.add(OfferBuilderManager.aBasicOfferWithId(1L).build());
        when(this.transactionalManager.getNextHundredFilteredOffers(
                anyString(), anyString(), anyBoolean(), anyBoolean(), any(Date.class), anyLong())
        ).thenReturn(offerList);
    }

    @Test
    public void testTransactionalManagerReturnsFilteredResults() {
        OfferFilters dummyFilters = createDummyOfferFilter();
        this.offerManager.getFilteredNextHundredResults(dummyFilters, 0L);
        verify(this.transactionalManager).getNextHundredFilteredOffers(
                Mockito.eq(dummyFilters.getSelectedcategory()),
                Mockito.eq(dummyFilters.getText()),
                Mockito.eq(dummyFilters.isExpired()),
                Mockito.eq(false),
                Mockito.any(Date.class),
                Mockito.eq(0L));
    }

    @Test
    public void testIfNoFiltersAreIncludedNewestOffersAreReturned() throws Exception {
        ArrayList<TheOffer> newestOffersOnly = new ArrayList<>();
        when(this.transactionalManager.getNextHundredOffers(0L, new Date(0))).thenReturn(newestOffersOnly);
        List<TheOffer> filteredResults = this.offerManager.getFilteredNextHundredResults(new OfferFilters(), 0L);
        assertEquals(newestOffersOnly, filteredResults);
    }

    @Test
    public void testCategoryOnlyFilterReturnsAllOffersNoMatterWhatTitleTheyHave() throws Exception {
        OfferFilters categoryOnlyFilter = createCategoryOfferFilter();
        this.offerManager.getFilteredNextHundredResults(categoryOnlyFilter, 0L);
        verify(this.transactionalManager).getNextHundredCategoryFilteredOffers(
                Mockito.eq(categoryOnlyFilter.getSelectedcategory()),
                Mockito.eq(categoryOnlyFilter.isExpired()),
                Mockito.eq(false),
                Mockito.any(Date.class),
                Mockito.eq(0L));
    }

    @Test
    public void testTitleOnlyFilterReturnsAllOffersNoMatterTheirCategory() throws Exception {
        OfferFilters categoryOnlyFilter = createTextOfferFilter();
        this.offerManager.getFilteredNextHundredResults(categoryOnlyFilter, 0L);
        verify(this.transactionalManager).getNextHundredTextFilteredOffers(
                Mockito.eq(categoryOnlyFilter.getText()),
                Mockito.eq(categoryOnlyFilter.isExpired()),
                Mockito.eq(false),
                Mockito.any(Date.class),
                Mockito.eq(0L));
    }

    @Test
    public void testExpiredOnlyFilterReturnsAllOffers() throws Exception {
        OfferFilters expiredOnlyFilter = createExpiredOfferFilter();
        this.offerManager.getFilteredNextHundredResults(expiredOnlyFilter, 0L);
        verify(this.transactionalManager).getNextHundredExpiredFilteredOffers(
                Mockito.eq(expiredOnlyFilter.isExpired()),
                Mockito.eq(false),
                Mockito.any(Date.class),
                Mockito.eq(0L));
    }

    @Test
    public void testAWeekDateRangeIsSentAsDefault() throws Exception {
        Date dateRange = filterByDateAndReturnArgmuent(null);
        Assert.assertTrue("Expected date range to be a week by default",
                convertDate(dateRange).equals(convertDate(oneWeekBackwards())));
    }

    @Test
    public void testOneDayDateRangeIsSent() throws Exception {
        Date dateRange = filterByDateAndReturnArgmuent(DateRange.DAY);
        Assert.assertTrue("Expected date range to be a day",
                convertDate(dateRange).equals(convertDate(yesterday())));
    }

    @Test
    public void testOneWeekDateRangeIsSent() throws Exception {
        Date dateRange = filterByDateAndReturnArgmuent(DateRange.WEEK);
        Assert.assertTrue("Expected date range to be a week",
                convertDate(dateRange).equals(convertDate(oneWeekBackwards())));
    }

    @Test
    public void testOneMonthDateRangeIsSent() throws Exception {
        Date dateRange = filterByDateAndReturnArgmuent(DateRange.MONTH);
        Assert.assertTrue("Expected date range to be a month",
                convertDate(dateRange).equals(convertDate(oneMonthBackwards())));
    }

    @Test
    public void testAllTimeDateRangeIsSent() throws Exception {
        Date dateRange = filterByDateAndReturnArgmuent(DateRange.ALL);
        Assert.assertTrue("Expected date range to be 1970",
                convertDate(dateRange).equals(convertDate(allTimeBackwards())));
    }

    private Date filterByDateAndReturnArgmuent(DateRange wantedDateRange) {
        OfferFilters offerFilters = FilterBuilderManager.anExpiredOnlyFilter(true)
                .withDateRange(wantedDateRange).build();
        this.offerManager.getFilteredNextHundredResults(offerFilters, 0L);
        ArgumentCaptor<Date> dateRangeArgumentCaptor = ArgumentCaptor.forClass(Date.class);
        verify(this.transactionalManager).getNextHundredExpiredFilteredOffers(
                anyBoolean(),
                anyBoolean(),
                dateRangeArgumentCaptor.capture(),
                anyLong());
        return dateRangeArgumentCaptor.getValue();
    }

    private OfferFilters createExpiredOfferFilter() {
        return FilterBuilderManager.anExpiredOnlyFilter(true).build();
    }

    private OfferFilters createTextOfferFilter() {
        return FilterBuilderManager.aTextOnlyFilter("intel").build();
    }

    private OfferFilters createCategoryOfferFilter() {
        return FilterBuilderManager.anCategoryOnlyFilter(OfferCategory.ELECTRONICA).build();
    }

    private OfferFilters createDummyOfferFilter() {
        return FilterBuilderManager.anAllElectronicsFilterWithText("intel").build();
    }

    private String convertDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private Date oneWeekBackwards() {
        return parseDatesByNumberOfDays(7);
    }

    private Date yesterday() {
        return parseDatesByNumberOfDays(1);
    }

    private Date oneMonthBackwards() {
        return parseDatesByNumberOfDays(31);

    }

    private Date allTimeBackwards() {
        return new Date(0);
    }

    private Date parseDatesByNumberOfDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -days);
        return calendar.getTime();
    }

}


