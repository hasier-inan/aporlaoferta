package com.aporlaoferta.affiliations;

import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.service.CompanyAffiliations;
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

        String companyName = offerCompany.getCompanyName();
        if (companyIsNotAffiliatedButHasAffiliatedId(offerCompany, companyName)
                || companyIsAffiliatedButHasNoAffiliateId(offerCompany, companyName)) {
            throw new InvalidAffiliatedCompany(companyName);
        }
        if (!companyIsAffiliated(companyName)) {
            return rawLink;
        }
        return parseDependingOnCompany(offerCompany, rawLink, companyName);
    }

    private String parseDependingOnCompany(OfferCompany offerCompany, String rawLink, String companyName) throws InvalidAffiliatedCompany, MalformedURLException, UnsupportedEncodingException {
        switch (CompanyAffiliations.fromCode(companyName)) {
            case AMAZON:
                return obtainAmazonAffiliationUrl(offerCompany, rawLink);
            default:
                throw new InvalidAffiliatedCompany(companyName);
        }
    }

    private String obtainAmazonAffiliationUrl(OfferCompany offerCompany, String rawLink) throws MalformedURLException, UnsupportedEncodingException {
        String parameterName = "tag";
        Map urlParameters = this.urlParser.extractParameters(rawLink);
        //remove previous existing tags
        urlParameters.remove(parameterName);
        urlParameters.put(parameterName, createCompanyAffiliateIdList(offerCompany.getCompanyAffiliateId()));
        return urlParser.createLinkWithParameters(rawLink, getParameterList(urlParameters));
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

    private boolean companyIsAffiliated(String companyName) {
        return CompanyAffiliations.isCompanyAffiliated(companyName);
    }

    private boolean companyIsAffiliatedButHasNoAffiliateId(OfferCompany offerCompany, String companyName) {
        return companyIsAffiliated(companyName)
                && isEmpty(offerCompany.getCompanyAffiliateId());
    }

    private boolean companyIsNotAffiliatedButHasAffiliatedId(OfferCompany offerCompany, String companyName) {
        return !companyIsAffiliated(companyName)
                && !isEmpty(offerCompany.getCompanyAffiliateId());
    }

    public String isUrlAlive(String rawLink) throws UnhealthyException {
        return this.urlParser.isAlive(rawLink);
    }

    @Autowired
    public void setUrlParser(UrlParser urlParser) {
        this.urlParser = urlParser;
    }
}
