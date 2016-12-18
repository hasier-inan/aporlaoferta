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
public class TradedoublerManager implements LinkProvider {

    private final String PREFIX = "http://clk.tradedoubler.com/click?";

    @Override
    public String createLinkAffiliation(OfferCompany offerCompany, String rawLink) throws MalformedURLException, UnsupportedEncodingException {
        String[] parameterNames = offerCompany.getCompanyAffiliateIdKey().split(" ");
        String[] parameterValues = offerCompany.getCompanyAffiliateId().split(" ");
        String affiliatedLink = PREFIX;
        for (int id = 0; id < parameterValues.length; id++) {
            affiliatedLink += String.format("%s(%s)", parameterNames[id], parameterValues[id]);
        }
        affiliatedLink += buildExceptionCompanyUrl(offerCompany, rawLink);
        return affiliatedLink;
    }

    /**
     * Certain companies may have specific requirements in the url build, i.e. lastminute
     *
     * @param offerCompany
     * @param rawLink
     * @return
     */
    private String buildExceptionCompanyUrl(OfferCompany offerCompany, String rawLink) {
        TradedoublerPostfix tradedoublerPostfix = TradedoublerPostfix.fromValue(offerCompany.getCompanyName());
        return String.format(tradedoublerPostfix.getText(), rawLink);
    }
}
