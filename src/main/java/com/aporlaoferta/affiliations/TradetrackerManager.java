package com.aporlaoferta.affiliations;

import com.aporlaoferta.model.OfferCompany;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 19/07/15
 * Time: 17:13
 */
public class TradetrackerManager implements LinkProvider {

    private final String PREFIX = "http://tc.tradetracker.net/";
    private final String POSTFIX = "&u=%s";

    @Override
    public String createLinkAffiliation(OfferCompany offerCompany, String rawLink) throws MalformedURLException, UnsupportedEncodingException {
        String[] parameterNames = offerCompany.getCompanyAffiliateIdKey().split(" ");
        String[] parameterValues = offerCompany.getCompanyAffiliateId().split(" ");
        String affiliatedLink = PREFIX;
        for (int id = 0; id < parameterValues.length; id++) {
            affiliatedLink += String.format("%s%s=%s", getParameterDivider(id), parameterNames[id], parameterValues[id]);
        }
        try {
            affiliatedLink += buildUrl(rawLink);
        } catch (UnsupportedEncodingException e) {
            throw new MalformedURLException(e.getMessage());
        }
        return affiliatedLink;
    }

    private String getParameterDivider(int id) {
        return id == 0 ? "?" : "&";
    }

    private String buildUrl(String rawLink) throws UnsupportedEncodingException {
        return String.format(POSTFIX, URLEncoder.encode(rawLink, "UTF-8"));
    }
}
