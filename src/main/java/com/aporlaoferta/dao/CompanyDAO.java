package com.aporlaoferta.dao;

import com.aporlaoferta.model.OfferCompany;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by hasiermetal on 26/07/14.
 */
public interface CompanyDAO extends CrudRepository<OfferCompany, Long> {
    @Query(value = "SELECT * FROM thatcompany order by TC_NAME desc", nativeQuery = true)
    public List<OfferCompany> getListOfPersistedCompanies();

    OfferCompany findByCompanyName(String companyName);
}
