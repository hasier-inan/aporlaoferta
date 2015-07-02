package com.aporlaoferta.service;

import com.aporlaoferta.dao.CommentDAO;
import com.aporlaoferta.dao.CompanyDAO;
import com.aporlaoferta.dao.OfferDAO;
import com.aporlaoferta.dao.UserDAO;
import com.aporlaoferta.dao.UserRolesDAO;
import com.aporlaoferta.model.OfferCategory;
import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.TheUserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by hasiermetal on 15/01/15.
 */
@Service
public class TransactionalManager {

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private CompanyDAO companyDAO;

    @Autowired
    private OfferDAO offerDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserRolesDAO userRolesDAO;

    ///Comment
    @Transactional(readOnly = true)
    public OfferComment getCommentFromId(Long id) {
        return this.commentDAO.findOne(id);
    }

    @Transactional
    public OfferComment saveComment(OfferComment offerComment) {
        return this.commentDAO.save(offerComment);

    }

    @Transactional(readOnly = true)
    public List<OfferComment> getFirstHundredCommentsForOffer(Long latestShownId, Long offerId) {
        return this.commentDAO.getOneHundredCommentsAfterId(latestShownId, offerId);
    }

    ///Company
    @Transactional(readOnly = true)
    public OfferCompany getCompanyFromId(Long id) {
        return this.companyDAO.findOne(id);
    }

    @Transactional(readOnly = true)
    public OfferCompany getCompanyFromName(String companyName) {
        return this.companyDAO.findByCompanyName(companyName);
    }

    @Transactional(readOnly = true)
    public List<OfferCompany> getAllCompanies() {
        return this.companyDAO.getListOfPersistedCompanies();
    }

    @Transactional
    public OfferCompany saveCompany(OfferCompany offerCompany) {
        return this.companyDAO.save(offerCompany);
    }

    ///Offer
    @Transactional
    public TheOffer saveOffer(TheOffer theOffer) {
        return this.offerDAO.save(theOffer);
    }

    @Transactional(readOnly = true)
    public TheOffer getOfferFromId(Long id) {
        return this.offerDAO.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<TheOffer> getNextHundredOffers(Long lastShownNumber) {
        return this.offerDAO.getOneHundredOffersAfterId(lastShownNumber);
    }

    @Transactional(readOnly = true)
    public List<TheOffer> getNextHundredOffersByCategory(Long lastShownNumber, OfferCategory offerCategory) {
        return this.offerDAO.getOneHundredOffersAfterIdWithCategory(lastShownNumber, offerCategory.getCode());
    }

    @Transactional(readOnly = true)
    public List<TheOffer> getNextHottestHundredOffers(Long lastShownNumber) {
        return this.offerDAO.getOneHundredHottestOffers(lastShownNumber);
    }

    @Transactional(readOnly = true)
    public List<TheOffer> getNextHundredFilteredOffers(String category, String text, boolean expired) {
        return this.offerDAO.getOneHundredFilteredOffers(category, "%"+text+"%", expired);
    }

    @Transactional(readOnly = true)
    public Long countAllOffers() {
        return this.offerDAO.count();
    }

    ///USER
    @Transactional
    public TheUserRoles saveUserRole(TheUserRoles theUserRoles) {
        return this.userRolesDAO.save(theUserRoles);
    }

    @Transactional
    public TheUser saveUser(TheUser theUser) {
        return this.userDAO.save(theUser);
    }

    @Transactional(readOnly = true)
    public TheUser getUserFromNickname(String nickname) {
        return this.userDAO.findByUserNickname(nickname);
    }
}
