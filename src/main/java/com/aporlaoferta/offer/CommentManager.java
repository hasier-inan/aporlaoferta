package com.aporlaoferta.offer;

import com.aporlaoferta.dao.CommentDAO;
import com.aporlaoferta.model.OfferComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by hasiermetal on 15/01/15.
 */
@Service
@Transactional
public class CommentManager {
    private final Logger LOG = LoggerFactory.getLogger(CommentManager.class);

    @Autowired
    CommentDAO commentDAO;

    public OfferComment getCommentFromId(Long id) {
        try {
            return this.commentDAO.findOne(id);
        } catch (IllegalArgumentException e) {
            //null id
            LOG.error("Got a null id while looking for an offer ", e);
        }
        return null;
    }

    public OfferComment createComment(OfferComment offerComment) {
        return this.commentDAO.save(offerComment);

    }

    public List<OfferComment> getFirstHundredCommentsForOffer(Long latestShownId, Long offerId) {
        return this.commentDAO.getOneHundredCommentsAfterId(latestShownId, offerId);
    }
}
