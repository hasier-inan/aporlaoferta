package com.aporlaoferta.affiliations;

import com.aporlaoferta.model.OfferCompany;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import static org.springframework.util.StringUtils.isEmpty;

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
    public String createLinkAffiliation(OfferCompany offerCompany, String rawLink) throws MalformedURLException {
        try {
            if (isEmpty(offerCompany.getCompanyAffiliateIdKey())) {
                return createIdLink(offerCompany, rawLink);
            } else {
                return createRegularLink(offerCompany, rawLink);
            }
        } catch (UnsupportedEncodingException e) {
            throw new MalformedURLException(e.getMessage());
        }
    }

    private String createIdLink(OfferCompany offerCompany, String rawLink) throws UnsupportedEncodingException {
        return String.format("%s%s", offerCompany.getCompanyAffiliateId(), encodeUrl(rawLink));
    }

    private String createRegularLink(OfferCompany offerCompany, String rawLink) throws UnsupportedEncodingException {
        String[] parameterNames = offerCompany.getCompanyAffiliateIdKey().split(" ");
        String[] parameterValues = offerCompany.getCompanyAffiliateId().split(" ");
        String affiliatedLink = PREFIX;
        for (int id = 0; id < parameterValues.length; id++) {
            affiliatedLink += String.format("%s%s=%s", getParameterDivider(id), parameterNames[id], parameterValues[id]);
        }
        affiliatedLink += buildUrl(rawLink);
        return affiliatedLink;
    }

    private String getParameterDivider(int id) {
        return id == 0 ? "?" : "&";
    }

    private String buildUrl(String rawLink) throws UnsupportedEncodingException {
        return String.format(POSTFIX, encodeUrl(rawLink));
    }

    private String encodeUrl(String rawLink) throws UnsupportedEncodingException {
        return URLEncoder.encode(rawLink, "UTF-8");
    }
}
