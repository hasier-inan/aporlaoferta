package com.aporlaoferta.dao;

import com.aporlaoferta.model.TheOffer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hasiermetal on 26/07/14.
 */
public interface OfferDAO extends CrudRepository<TheOffer, Long> {
    public final String OFFER_QUERY="SELECT * FROM thatoffer a order by a.TO_ID desc LIMIT :number, 100";

    @Query(value = OFFER_QUERY, nativeQuery = true)
    public List<TheOffer> getOneHundredOffersAfterId(@Param("number")Long number);
}

