package com.aporlaoferta.affiliations;

import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.utils.UnhealthyException;
import com.aporlaoferta.utils.UrlParser;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 19/07/15
 * Time: 17:13
 */
public class AffiliationManager {

    private UrlParser urlParser;

    /**
     * Checks different affiliated companies in the enum, and, if listed, will generate corresponding link
     *
     * @param offerCompany: company that may be affiliated
     * @param rawLink:      raw url that may already contain a 3rd party affiliation
     * @return updated url with corresponding affiliation it
     */
    public String constructAffiliatedLink(OfferCompany offerCompany, String rawLink) throws InvalidAffiliatedCompany, MalformedURLException, UnsupportedEncodingException {
        if (!companyIsAffiliated(offerCompany)) {
            return rawLink;
        }
        return obtainAffiliationUrl(offerCompany, rawLink);
    }

    private String obtainAffiliationUrl(OfferCompany offerCompany, String rawLink) throws InvalidAffiliatedCompany, MalformedURLException, UnsupportedEncodingException {
        String[] parameterNames = offerCompany.getCompanyAffiliateIdKey().split(" ");
        String[] parameterValues = offerCompany.getCompanyAffiliateId().split(" ");
        Map urlParameters = this.urlParser.extractParameters(rawLink);
        try {
            for (int id = 0; id < parameterValues.length; id++) {
                urlParameters.remove(parameterNames[id]);
                urlParameters.put(parameterNames[id], createCompanyAffiliateIdList(parameterValues[id]));
            }
            return urlParser.createLinkWithParameters(rawLink, getParameterList(urlParameters));
        } catch (Exception e) {
            throw new InvalidAffiliatedCompany(offerCompany.getCompanyName());
        }
    }

    private List<String> createCompanyAffiliateIdList(String companyAffiliateId) {
        return Arrays.asList(new String[]{companyAffiliateId});
    }

    private String getParameterList(Map<String, List<String>> urlParameters) {
        String theUrl = "";
        for (String parameter : urlParameters.keySet()) {
            List<String> values = urlParameters.get(parameter);
            for (String value : values) {
                theUrl += (isEmpty(theUrl)) ? "?" + parameter + "=" + value : "&" + parameter + "=" + value;
            }
        }
        return theUrl;
    }

    private boolean companyIsAffiliated(OfferCompany offerCompany) {
        return !isEmpty(offerCompany.getCompanyAffiliateId());
    }

    public String isUrlAlive(String rawLink) throws UnhealthyException {
        return this.urlParser.isAlive(rawLink);
    }

    @Autowired
    public void setUrlParser(UrlParser urlParser) {
        this.urlParser = urlParser;
    }
}
