package com.aporlaoferta.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hinan
 * Date: 11/02/2015
 * Time: 18:23
 * Hitachi Capital (UK) PLC
 */
public class TheOfferResponse extends TheResponse {

    private List<TheOffer> theOffers;

    public List<TheOffer> getTheOffers() {
        return theOffers != null ? theOffers : new ArrayList<TheOffer>();
    }

    public void setTheOffers(List<TheOffer> theOffers) {
        this.theOffers = theOffers;
    }
}
