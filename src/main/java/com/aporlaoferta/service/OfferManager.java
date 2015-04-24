package com.aporlaoferta.service;

import com.aporlaoferta.model.TheOffer;
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

    TransactionalManager transactionalManager;

    //@Secured({"IS_AUTHENTICATED_ANONYMOUSLY"})
    public TheOffer createOffer(TheOffer theOffer) {
        return this.transactionalManager.saveOffer(theOffer);
    }

    //@Secured({"ROLE_ADMIN"})
    public TheOffer expireOffer(TheOffer theOffer) {
        theOffer.setOfferExpired(true);
        return this.transactionalManager.saveOffer(theOffer);
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

    public List<TheOffer> getNextHundredOffers(Long lastShownId) {
        return this.transactionalManager.getNextHundredOffers(lastShownId);
    }

    public Long countAllOffers() {
        return this.transactionalManager.countAllOffers();
    }

    @Autowired
    public void setTransactionalManager(TransactionalManager transactionalManager) {
        this.transactionalManager = transactionalManager;
    }
}
