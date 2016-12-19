package com.aporlaoferta.affiliations;

import com.aporlaoferta.model.OfferCompany;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 19/07/15
 * Time: 17:13
 */
public class BelboonManager implements LinkProvider {

    private final String PREFIX = "http://www1.belboon.de/adtracking/%s.html/&deeplink=%s";

    @Override
    public String createLinkAffiliation(OfferCompany offerCompany, String rawLink) throws MalformedURLException, UnsupportedEncodingException {
        String companyId = offerCompany.getCompanyAffiliateId();
        return String.format(PREFIX, companyId, rawLink);
    }
}
