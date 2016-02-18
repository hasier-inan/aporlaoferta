package com.aporlaoferta.service;

import com.aporlaoferta.affiliations.AffiliationManager;
import com.aporlaoferta.affiliations.InvalidAffiliatedCompany;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.utils.UnhealthyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by hasiermetal on 15/01/15.
 */
@Service
public class CompanyManager {
    private final Logger LOG = LoggerFactory.getLogger(CompanyManager.class);

    private TransactionalManager transactionalManager;

    private AffiliationManager affiliationManager;

    public OfferCompany getCompanyFromId(Long id) {
        try {
            return this.transactionalManager.getCompanyFromId(id);
        } catch (IllegalArgumentException e) {
            //null id
            LOG.error("Got a null id while looking for a company ", e);
        }
        return null;
    }

    public OfferCompany getCompanyFromName(String companyName) {
        return this.transactionalManager.getCompanyFromName(companyName);
    }

    public String createAffiliationLink(OfferCompany offerCompany, String rawLink) throws MalformedURLException, UnsupportedEncodingException, UnhealthyException {
        this.affiliationManager.isUrlAlive(rawLink);

        if (offerCompany == null || isEmpty(offerCompany.getCompanyAffiliateId()) || isEmpty(rawLink)) {
            LOG.info("No affiliation found for the given company");
            return rawLink;
        }
        try {
            return this.affiliationManager.constructAffiliatedLink(offerCompany, rawLink);
        } catch (InvalidAffiliatedCompany e) {
            LOG.error("Could not add custom affiliation id to offer url: ", e);
        }
        return rawLink;
    }

    public String getWatermarkedCompany(String uri) {
        List<OfferCompany> allCompanies = getAllCompanies();
        for (OfferCompany offerCompany : allCompanies) {
            if (isEmpty(offerCompany.getCompanyWatermarks())) {
                continue;
            }
            for (String watermark : offerCompany.getCompanyWatermarks().split(" ")) {
                if (uri.indexOf(watermark) >= 0) {
                    return offerCompany.getCompanyName();
                }
            }
        }
        return "";
    }

    public List<OfferCompany> getAllCompanies() {
        return this.transactionalManager.getAllCompanies();
    }

    public OfferCompany createCompany(OfferCompany offerCompany) {
        return this.transactionalManager.saveCompany(offerCompany);
    }

    @Autowired
    public void setTransactionalManager(TransactionalManager transactionalManager) {
        this.transactionalManager = transactionalManager;
    }

    @Autowired
    public void setAffiliationManager(AffiliationManager affiliationManager) {
        this.affiliationManager = affiliationManager;
    }
}
