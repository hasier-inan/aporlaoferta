package com.aporlaoferta.service;

import com.aporlaoferta.model.DateRange;
import com.aporlaoferta.model.OfferCategory;
import com.aporlaoferta.model.OfferFilters;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hasiermetal on 15/01/15.
 */
@Service
public class OfferManager {
    private final Logger LOG = LoggerFactory.getLogger(OfferManager.class);

    private TransactionalManager transactionalManager;

    //@Secured({"IS_AUTHENTICATED_ANONYMOUSLY"})
    public TheOffer createOffer(TheOffer theOffer) {
        return this.transactionalManager.saveOffer(theOffer);
    }

    //@Secured({"ROLE_ADMIN"})
    public TheOffer expireOffer(TheOffer theOffer) {
        theOffer.setOfferExpired(true);
        return this.transactionalManager.saveOffer(theOffer);
    }

    public TheOffer positiveFeedback(TheOffer originalOffer) {
        originalOffer.incrementPositiveFeedback();
        return this.transactionalManager.saveOffer(originalOffer);
    }

    public TheOffer negativeFeedback(TheOffer originalOffer) {
        originalOffer.incrementNegativeFeedback();
        return this.transactionalManager.saveOffer(originalOffer);
    }

    public TheOffer getOfferFromId(Long id) {
        try {
            return this.transactionalManager.getOfferFromId(id);
        } catch (IllegalArgumentException e) {
            //null id
            LOG.error("Got a null id while looking for an offer ", e);
        }
        return null;
    }

    public void deleteOffer(Long id) {
        TheOffer theOffer = getOfferFromId(id);
        if (theOffer != null) {
            theOffer.setDisabled(true);
            this.transactionalManager.saveOffer(theOffer);
        }
    }

    public List<TheOffer> getNextHundredOffers(Long lastShownNumber, DateRange dateRange) {
        return this.transactionalManager.getNextHundredOffers(lastShownNumber, DateUtils.parseDateRangeOnDate(dateRange));
    }

    public List<TheOffer> getNextHundredOffersByCategory(Long lastShownNumber, OfferCategory offerCategory, DateRange dateRange) {
        return this.transactionalManager.getNextHundredOffersByCategory(lastShownNumber, offerCategory, DateUtils.parseDateRangeOnDate(dateRange));
    }

    public List<TheOffer> getNextHundredHottestOffers(Long lastShownNumber, DateRange dateRange) {
        return this.transactionalManager.getNextHottestHundredOffers(lastShownNumber, DateUtils.parseDateRangeOnDate(dateRange));
    }


    public List<TheOffer> getFilteredNextHundredResults(OfferFilters offerFilters, Long lastShownNumber) {
        if (offerFilters.containsCategoryOnlyFilter()) {
            return this.transactionalManager.getNextHundredCategoryFilteredOffers(
                    offerFilters.getSelectedcategory(),
                    offerFilters.isExpired(),
                    offerFilters.isHot(),
                    DateUtils.parseDateRangeOnDate(offerFilters.getDateRange()),
                    lastShownNumber
            );
        } else if (offerFilters.containsTextOnlyFilter()) {
            return this.transactionalManager.getNextHundredTextFilteredOffers(
                    offerFilters.getText(),
                    offerFilters.isExpired(),
                    offerFilters.isHot(),
                    DateUtils.parseDateRangeOnDate(offerFilters.getDateRange()),
                    lastShownNumber
            );
        } else if (offerFilters.containsExpiredOnlyFilter()) {
            return this.transactionalManager.getNextHundredExpiredFilteredOffers(
                    offerFilters.isExpired(),
                    offerFilters.isHot(),
                    DateUtils.parseDateRangeOnDate(offerFilters.getDateRange()),
                    lastShownNumber);
        } else {
            if (offerFilters.containsFilter())
                return this.transactionalManager.getNextHundredFilteredOffers(
                        offerFilters.getSelectedcategory(),
                        offerFilters.getText(),
                        offerFilters.isExpired(),
                        offerFilters.isHot(),
                        DateUtils.parseDateRangeOnDate(offerFilters.getDateRange()),
                        lastShownNumber);
            return (offerFilters.isHot()) ? this.transactionalManager.getNextHottestHundredOffers(lastShownNumber,
                    DateUtils.parseDateRangeOnDate(offerFilters.getDateRange()))
                    : this.transactionalManager.getNextHundredOffers(lastShownNumber, DateUtils.parseDateRangeOnDate(offerFilters.getDateRange()));
        }
    }

    public Long countAllOffers() {
        return this.transactionalManager.countAllOffers();
    }

    @Autowired
    public void setTransactionalManager(TransactionalManager transactionalManager) {
        this.transactionalManager = transactionalManager;
    }
}
