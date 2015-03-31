package com.aporlaoferta.dao;

import com.aporlaoferta.model.OfferComment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hasiermetal on 26/07/14.
 */
public interface CommentDAO extends CrudRepository<OfferComment, Long> {
    public final String COMMENT_QUERY = "SELECT * FROM thatoffer a, thatcomment b WHERE a.TO_ID=:offerId ORDER BY " +
            "b.TCM_CREATION_DATE desc LIMIT :commentId, 100";

    @Query(value = COMMENT_QUERY, nativeQuery = true)
    List<OfferComment> getOneHundredCommentsAfterId(@Param("commentId") Long commentId, @Param("offerId") Long offerId);
}
