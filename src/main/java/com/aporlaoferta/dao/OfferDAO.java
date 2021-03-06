package com.aporlaoferta.dao;

import com.aporlaoferta.model.TheOffer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by hasiermetal on 26/07/14.
 */
public interface OfferDAO extends CrudRepository<TheOffer, Long> {
    String OFFER_QUERY = "SELECT * FROM thatoffer a " +
            "WHERE a.TO_EXPIRED=false " +
            "AND a.TO_DISABLED=false " +
            "AND a.TO_CREATED_DATE>=:minimumDate " +
            "order by a.TO_ID desc LIMIT 30 OFFSET :number";
    String OFFER_QUERY_HOTTEST = "SELECT * FROM thatoffer a " +
            "WHERE a.TO_EXPIRED=false " +
            "AND a.TO_DISABLED=false " +
            "AND a.TO_CREATED_DATE>=:minimumDate " +
            "order by a.TO_POSITIVE-a.TO_NEGATIVE desc LIMIT 30 OFFSET :number";
    String OFFER_QUERY_CATEGORY = "SELECT * FROM thatoffer a " +
            "WHERE a.TO_CATEGORY=:category " +
            "AND a.TO_CREATED_DATE>=:minimumDate " +
            "order by a.TO_ID desc LIMIT 30 OFFSET :number";

    String OFFER_QUERY_FILTER = "SELECT * " +
            "FROM thatoffer a LEFT JOIN thatcompany c on a.TO_COMPANY=c.TC_ID " +
            "WHERE a.TO_CATEGORY =:category " +
            "AND a.TO_DISABLED=false " +
            "AND (lower(a.TO_DESCRIPTION) LIKE lower(:text) OR lower(a.TO_TITLE) LIKE lower(:text) OR lower(c.TC_NAME) LIKE lower(:text)) " +
            "AND (a.TO_EXPIRED=false OR a.TO_EXPIRED=:expired) " +
            "AND a.TO_CREATED_DATE>=:minimumDate " +
            "order by a.TO_ID desc LIMIT 30 OFFSET :number";
    String OFFER_QUERY_FILTER_HOT = "SELECT * " +
            "FROM thatoffer a LEFT JOIN thatcompany c on a.TO_COMPANY=c.TC_ID " +
            "WHERE a.TO_CATEGORY =:category " +
            "AND a.TO_DISABLED=false " +
            "AND (lower(a.TO_DESCRIPTION) LIKE lower(:text) OR lower(a.TO_TITLE) LIKE lower(:text) OR lower(c.TC_NAME) LIKE lower(:text)) " +
            "AND (a.TO_EXPIRED=false OR a.TO_EXPIRED=:expired) " +
            "AND a.TO_CREATED_DATE>=:minimumDate " +
            "order by a.TO_POSITIVE-a.TO_NEGATIVE desc LIMIT 30 OFFSET :number";
    String OFFER_QUERY_FILTER_CATEGORY = "SELECT * " +
            "FROM thatoffer a " +
            "WHERE a.TO_CATEGORY =:category AND (a.TO_EXPIRED=false OR a.TO_EXPIRED=:expired) " +
            "AND a.TO_DISABLED=false " +
            "AND a.TO_CREATED_DATE>=:minimumDate " +
            "order by a.TO_ID desc LIMIT 30 OFFSET :number";
    String OFFER_QUERY_FILTER_CATEGORY_HOT = "SELECT * " +
            "FROM thatoffer a " +
            "WHERE a.TO_CATEGORY =:category AND (a.TO_EXPIRED=false OR a.TO_EXPIRED=:expired) " +
            "AND a.TO_DISABLED=false " +
            "AND a.TO_CREATED_DATE>=:minimumDate " +
            "order by a.TO_POSITIVE-a.TO_NEGATIVE desc LIMIT 100 OFFSET :number";
    String OFFER_QUERY_FILTER_EXPIRED = "SELECT * " +
            "FROM thatoffer a " +
            "WHERE (a.TO_EXPIRED=false OR a.TO_EXPIRED=:expired) " +
            "AND a.TO_DISABLED=false " +
            "AND a.TO_CREATED_DATE>=:minimumDate " +
            "order by a.TO_ID desc LIMIT 30 OFFSET :number";
    String OFFER_QUERY_FILTER_EXPIRED_HOT = "SELECT * " +
            "FROM thatoffer a " +
            "WHERE (a.TO_EXPIRED=false OR a.TO_EXPIRED=:expired) " +
            "AND a.TO_DISABLED=false " +
            "AND a.TO_CREATED_DATE>=:minimumDate " +
            "order by a.TO_POSITIVE-a.TO_NEGATIVE desc LIMIT 30 OFFSET :number";
    String OFFER_QUERY_FILTER_TEXT = "SELECT * " +
            "FROM thatoffer a LEFT JOIN thatcompany c on a.TO_COMPANY=c.TC_ID " +
            "WHERE (lower(a.TO_DESCRIPTION) LIKE lower(:text) OR lower(a.TO_TITLE) LIKE lower(:text) OR lower(c.TC_NAME) LIKE lower(:text)) " +
            "AND a.TO_DISABLED=false " +
            "AND (a.TO_EXPIRED=false OR a.TO_EXPIRED=:expired) " +
            "AND a.TO_CREATED_DATE>=:minimumDate " +
            "order by a.TO_ID desc LIMIT 30 OFFSET :number";
    String OFFER_QUERY_FILTER_TEXT_HOT = "SELECT * " +
            "FROM thatoffer a LEFT JOIN thatcompany c on a.TO_COMPANY=c.TC_ID " +
            "WHERE (lower(a.TO_DESCRIPTION) LIKE lower(:text) OR lower(a.TO_TITLE) LIKE lower(:text) OR lower(c.TC_NAME) LIKE lower(:text)) " +
            "AND a.TO_DISABLED=false " +
            "AND (a.TO_EXPIRED=false OR a.TO_EXPIRED=:expired) " +
            "AND a.TO_CREATED_DATE>=:minimumDate " +
            "order by a.TO_POSITIVE-a.TO_NEGATIVE desc LIMIT 30 OFFSET :number";

    @Query(value = OFFER_QUERY, nativeQuery = true)
    List<TheOffer> getOneHundredOffersAfterId(@Param("number") Long number,
                                              @Param("minimumDate") Date minimumDate);

    @Query(value = OFFER_QUERY_CATEGORY, nativeQuery = true)
    List<TheOffer> getOneHundredOffersAfterIdWithCategory(@Param("number") Long lastShownNumber,
                                                          @Param("category") String offerCategory,
                                                          @Param("minimumDate") Date minimumDate);

    @Query(value = OFFER_QUERY_HOTTEST, nativeQuery = true)
    List<TheOffer> getOneHundredHottestOffers(@Param("number") Long number,
                                              @Param("minimumDate") Date minimumDate);

    @Query(value = OFFER_QUERY_FILTER, nativeQuery = true)
    List<TheOffer> getOneHundredFilteredOffers(@Param("category") String category,
                                               @Param("text") String text,
                                               @Param("expired") boolean expired,
                                               @Param("minimumDate") Date minimumDate,
                                               @Param("number") Long lastShownNumber);

    @Query(value = OFFER_QUERY_FILTER_HOT, nativeQuery = true)
    List<TheOffer> getOneHundredFilteredHotOffers(@Param("category") String category,
                                                  @Param("text") String text,
                                                  @Param("expired") boolean expired,
                                                  @Param("minimumDate") Date minimumDate,
                                                  @Param("number") Long lastShownNumber);

    @Query(value = OFFER_QUERY_FILTER_CATEGORY, nativeQuery = true)
    List<TheOffer> getOneHundredCategoryFilteredOffers(@Param("category") String selectedcategory,
                                                       @Param("expired") boolean expired,
                                                       @Param("minimumDate") Date minimumDate,
                                                       @Param("number") Long lastShownNumber);

    @Query(value = OFFER_QUERY_FILTER_CATEGORY_HOT, nativeQuery = true)
    List<TheOffer> getOneHundredCategoryFilteredHotOffers(@Param("category") String selectedcategory,
                                                          @Param("expired") boolean expired,
                                                          @Param("minimumDate") Date minimumDate,
                                                          @Param("number") Long lastShownNumber);

    @Query(value = OFFER_QUERY_FILTER_TEXT, nativeQuery = true)
    List<TheOffer> getOneHundredTextFilteredOffers(@Param("text") String text,
                                                   @Param("expired") boolean expired,
                                                   @Param("minimumDate") Date minimumDate,
                                                   @Param("number") Long lastShownNumber);

    @Query(value = OFFER_QUERY_FILTER_TEXT_HOT, nativeQuery = true)
    List<TheOffer> getOneHundredTextFilteredHotOffers(@Param("text") String text,
                                                      @Param("expired") boolean expired,
                                                      @Param("minimumDate") Date minimumDate,
                                                      @Param("number") Long lastShownNumber);

    @Query(value = OFFER_QUERY_FILTER_EXPIRED, nativeQuery = true)
    List<TheOffer> getOneHundredExpiredFilteredOffers(@Param("expired") boolean expired,
                                                      @Param("minimumDate") Date minimumDate,
                                                      @Param("number") Long lastShownNumber);

    @Query(value = OFFER_QUERY_FILTER_EXPIRED_HOT, nativeQuery = true)
    List<TheOffer> getOneHundredExpiredFilteredHotOffers(@Param("expired") boolean expired,
                                                         @Param("minimumDate") Date minimumDate,
                                                         @Param("number") Long lastShownNumber);

}

