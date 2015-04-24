package com.aporlaoferta.service;

import com.aporlaoferta.dao.CommentDAO;
import com.aporlaoferta.dao.CompanyDAO;
import com.aporlaoferta.dao.OfferDAO;
import com.aporlaoferta.dao.UserDAO;
import com.aporlaoferta.dao.UserRolesDAO;
import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.TheUserRoles;
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
public class TransactionalManager {
    private final Logger LOG = LoggerFactory.getLogger(TransactionalManager.class);

    @Autowired
    CommentDAO commentDAO;

    @Autowired
    CompanyDAO companyDAO;

    @Autowired
    OfferDAO offerDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserRolesDAO userRolesDAO;

    ///Comment
    public OfferComment getCommentFromId(Long id) {
        return this.commentDAO.findOne(id);
    }

    public OfferComment saveComment(OfferComment offerComment) {
        return this.commentDAO.save(offerComment);

    }

    public List<OfferComment> getFirstHundredCommentsForOffer(Long latestShownId, Long offerId) {
        return this.commentDAO.getOneHundredCommentsAfterId(latestShownId, offerId);
    }

    ///Company
    public OfferCompany getCompanyFromId(Long id) {
        return this.companyDAO.findOne(id);
    }

    public List<OfferCompany> getAllCompanies() {
        return this.companyDAO.getListOfPersistedCompanies();
    }

    public OfferCompany saveCompany(OfferCompany offerCompany) {
        return this.companyDAO.save(offerCompany);
    }

    ///Offer
    public TheOffer saveOffer(TheOffer theOffer) {
        return this.offerDAO.save(theOffer);
    }

    public TheOffer getOfferFromId(Long id) {
        return this.offerDAO.findOne(id);
    }

    public List<TheOffer> getNextHundredOffers(Long lastShownId) {
        return this.offerDAO.getOneHundredOffersAfterId(lastShownId);
    }


    public Long countAllOffers() {
        return this.offerDAO.count();
    }

    ///USER
    public TheUserRoles saveUserRole(TheUserRoles theUserRoles) {
        return this.userRolesDAO.save(theUserRoles);
    }

    public TheUser saveUser(TheUser theUser) {
        return this.userDAO.save(theUser);
    }

    public TheUser getUserFromNickname(String nickname) {
        return this.userDAO.findByUserNickname(nickname);
    }
}
