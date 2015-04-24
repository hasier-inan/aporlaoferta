package com.aporlaoferta.service;

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
public class CommentManager {
    private final Logger LOG = LoggerFactory.getLogger(CommentManager.class);

    TransactionalManager transactionalManager;

    public OfferComment getCommentFromId(Long id) {
        try {
            return this.transactionalManager.getCommentFromId(id);
        } catch (IllegalArgumentException e) {
            //null id
            LOG.error("Got a null id while looking for an offer ", e);
        }
        return null;
    }

    public OfferComment createComment(OfferComment offerComment) {
        return this.transactionalManager.saveComment(offerComment);
    }

    public List<OfferComment> getFirstHundredCommentsForOffer(Long latestShownId, Long offerId) {
        return this.transactionalManager.getFirstHundredCommentsForOffer(latestShownId, offerId);
    }

    @Autowired
    public void setTransactionalManager(TransactionalManager transactionalManager) {
        this.transactionalManager = transactionalManager;
    }
}
