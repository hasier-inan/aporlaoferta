package com.aporlaoferta.affiliations;

import com.aporlaoferta.model.OfferCompany;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 08/01/17
 * Time: 13:12
 */
public class ZanoxManager implements LinkProvider {

    private final String PREFIX = "https://ad.zanox.com/ppc/?%s&ulp=[[%s]]";

    @Override
    public String createLinkAffiliation(OfferCompany offerCompany, String rawLink) throws MalformedURLException, UnsupportedEncodingException {
        String companyId = offerCompany.getCompanyAffiliateId();
        return String.format(PREFIX, companyId, rawLink);
    }
}
