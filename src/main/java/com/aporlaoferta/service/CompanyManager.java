package com.aporlaoferta.service;

import com.aporlaoferta.affiliations.GenericAffiliation;
import com.aporlaoferta.model.OfferCompany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by hasiermetal on 15/01/15.
 */
@Service
public class CompanyManager {
    private final Logger LOG = LoggerFactory.getLogger(CompanyManager.class);

    TransactionalManager transactionalManager;

    public OfferCompany getCompanyFromId(Long id) {
        try {
            return this.transactionalManager.getCompanyFromId(id);
        } catch (IllegalArgumentException e) {
            //null id
            LOG.error("Got a null id while looking for a company ", e);
        }
        return null;
    }

    public String createAffiliationLink(OfferCompany offerCompany, String rawLink) {
        if (offerCompany == null || isEmpty(offerCompany.getCompanyAffiliateId()) || isEmpty(rawLink)) {
            LOG.info("No affiliation found for the given company");
            return null;
        }
        //Need to parse company to know which affiliate manager we should use.
        //TODO: meanwhile use the main affiliation manager
        GenericAffiliation genericAffiliation = new GenericAffiliation("&affiliate=" +
                offerCompany.getCompanyAffiliateId());
        return genericAffiliation.addAffiliationIdToLink(rawLink);
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
}
