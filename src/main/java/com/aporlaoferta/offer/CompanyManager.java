package com.aporlaoferta.offer;

import com.aporlaoferta.affiliations.GenericAffiliation;
import com.aporlaoferta.dao.CompanyDAO;
import com.aporlaoferta.model.OfferCompany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by hasiermetal on 15/01/15.
 */
@Service
@Transactional
public class CompanyManager {
    private final Logger LOG = LoggerFactory.getLogger(CompanyManager.class);

    @Autowired
    CompanyDAO companyDAO;

    public OfferCompany getCompanyFromId(Long id) {
        try {
            return this.companyDAO.findOne(id);
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
        return this.companyDAO.getListOfPersistedCompanies();
    }

    public OfferCompany createCompany(OfferCompany offerCompany) {
        return this.companyDAO.save(offerCompany);
    }

}
