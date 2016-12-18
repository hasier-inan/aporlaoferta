package com.aporlaoferta.affiliations;

import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.utils.UnhealthyException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 19/07/15
 * Time: 17:13
 */
public class AffiliationManager {

    private LinkManager linkManager;
    private TradedoublerManager tradedoublerManager;

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
        try {
            switch (offerCompany.getCompanyAffiliateType()) {
                case LINK:
                    return this.linkManager.createLinkAffiliation(offerCompany, rawLink);
                case TRADEDOUBLER:
                    return this.tradedoublerManager.createLinkAffiliation(offerCompany, rawLink);
                default:
                    throw new InvalidAffiliatedCompany(offerCompany.getCompanyName());
            }
        } catch (Exception e) {
            throw new InvalidAffiliatedCompany(offerCompany.getCompanyName());
        }
    }

    private boolean companyIsAffiliated(OfferCompany offerCompany) {
        return !isEmpty(offerCompany.getCompanyAffiliateType());
    }

    public String isUrlAlive(String rawLink) throws UnhealthyException {
        return this.linkManager.isUrlAlive(rawLink);
    }

    @Autowired
    public void setLinkManager(LinkManager linkManager) {
        this.linkManager = linkManager;
    }

    @Autowired
    public void setTradedoublerManager(TradedoublerManager tradedoublerManager) {
        this.tradedoublerManager = tradedoublerManager;
    }
}
