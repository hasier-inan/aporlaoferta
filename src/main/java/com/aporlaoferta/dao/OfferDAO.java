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
    public final String OFFER_QUERY_HOTTEST="SELECT * FROM thatoffer a WHERE a.TO_EXPIRED=false order by a.TO_POSITIVE-a.TO_NEGATIVE desc LIMIT :number, 100";
    public final String OFFER_QUERY_CATEGORY="SELECT * FROM thatoffer a WHERE a.TO_CATEGORY=:category order by a.TO_ID desc LIMIT :number, 100";

    @Query(value = OFFER_QUERY, nativeQuery = true)
    public List<TheOffer> getOneHundredOffersAfterId(@Param("number")Long number);

    @Query(value = OFFER_QUERY_CATEGORY, nativeQuery = true)
    public List<TheOffer> getOneHundredOffersAfterIdWithCategory(@Param("number")Long lastShownNumber,
                                                                 @Param("category")String offerCategory);

    @Query(value = OFFER_QUERY_HOTTEST, nativeQuery = true)
    List<TheOffer> getOneHundredHottestOffers(@Param("number")Long number);
}

