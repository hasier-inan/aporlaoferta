package com.aporlaoferta.utils;

import com.aporlaoferta.model.*;
import com.aporlaoferta.offer.CommentManager;
import com.aporlaoferta.offer.CompanyManager;
import com.aporlaoferta.offer.OfferManager;
import com.aporlaoferta.offer.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by hasiermetal on 15/01/15.
 */
@Service
public class RequestParameterParser {

    private final Logger LOG = LoggerFactory.getLogger(RequestParameterParser.class);

    private OfferManager offerManager;

    private UserManager userManager;

    private CommentManager commentManager;

    private CompanyManager companyManager;

    public RequestParameterParser() {
    }


    /**
     * Create a domain comment object from the map received from the front-end
     *
     * @param rawComment : map with all comment parameters
     * @return a comment domain object with parsed valies
     */
    /**
     * Update the domain object with given parameters
     *
     * @param rawComment: domain object that should have minimum data on it
     * @param offerId:    offer id related to the comment
     * @param nickname:   user that created the offer
     * @return
     */
    public void parseOfferCommentRawMap(OfferComment rawComment,
                                        String offerId,
                                        String nickname) {
        TheUser theUser = this.userManager.getUserFromNickname(nickname);
        rawComment.setCommentOwner(theUser);
        rawComment.setCommentCreationDate(new Date());
        try {
            TheOffer theOffer = this.offerManager.getOfferFromId(Long.valueOf(offerId));
            rawComment.setCommentsOffer(theOffer);
        } catch (NumberFormatException e) {
            LOG.warn(String.format("Could not parse offer id %s", offerId));
        }
    }

    public void parseOfferQuoteRawMap(OfferComment thatQuotedComment, String quotedCommentId, String nickName) {
        TheUser theUser = this.userManager.getUserFromNickname(nickName);
        thatQuotedComment.setCommentOwner(theUser);
        thatQuotedComment.setCommentCreationDate(new Date());
        try {
            OfferComment originalQuotedComment = this.commentManager.getCommentFromId(Long.valueOf(quotedCommentId));
            thatQuotedComment.setCommentsQuotedComment(originalQuotedComment);
            thatQuotedComment.setCommentsOffer(originalQuotedComment.getCommentsOffer());
        } catch (NumberFormatException e) {
            LOG.warn(String.format("Could not parse quote id %s / %s", quotedCommentId));
        }
    }

    public TheOffer parseOfferRawMap(Map<String, String> thatOffer, String nickname) {
        TheOffer theOffer = new TheOffer();
        TheUser theUser = this.userManager.getUserFromNickname(nickname);
        theOffer.setOfferUser(theUser);
        String category = thatOffer.get(MappingEntries.CATEGORY.getCode());
        try {
            theOffer.setOfferCategory(OfferCategory.fromValue(category));
        } catch (IllegalArgumentException e) {
            LOG.warn(String.format("Could not parse category from enum ", category));
        }
        String companyCode = thatOffer.get(MappingEntries.COMPANY.getCode());
        try {
            OfferCompany offerCompany = this.companyManager.getCompanyFromId(Long.parseLong(companyCode));
            theOffer.setOfferCompany(offerCompany);
        } catch (NumberFormatException e) {
            LOG.warn(String.format("Could not parse company id %s", companyCode));
        }
        theOffer.setOfferCreatedDate(new Date());
        theOffer.setOfferDescription(thatOffer.get(MappingEntries.DESCRIPTION.getCode()));
        theOffer.setOfferExpired(false);
        //TODO: After selecting image it will be uploaded to server, as result url will be provided
        theOffer.setOfferImage(thatOffer.get(MappingEntries.IMAGE.getCode()));
        //TODO: After adding the url, there will be a http get process to see if it exists!
        //This link will be changed to include affiliate id
        //this depends in the company. so, create an abstract companyAffiliateAdder, and extend for each specific
        // company
        theOffer.setOfferLink(this.companyManager.createAffiliationLink(theOffer.getOfferCompany(),
                thatOffer.get(MappingEntries.LINK.getCode())));
        theOffer.setOfferNegativeVote(0L);
        theOffer.setOfferPositiveVote(0L);
        theOffer.setOfferTitle(thatOffer.get(MappingEntries.TITLE.getCode()));
        String originalPrice = thatOffer.get(MappingEntries.ORIGINAL_PRICE.getCode());
        if (!isEmpty(originalPrice)) {
            try {
                theOffer.setOriginalPrice(new BigDecimal(originalPrice));
            } catch (NumberFormatException e) {
                LOG.warn(String.format("Could not parse BigDecimal from original price: %s", originalPrice));
            }
        }
        String finalPrice = thatOffer.get(MappingEntries.FINAL_PRICE.getCode());
        if (!isEmpty(finalPrice)) {
            try {
                theOffer.setFinalPrice(new BigDecimal(finalPrice));
            } catch (NumberFormatException e) {
                LOG.warn(String.format("Could not parse BigDecimal from final price: %s", finalPrice));
            }
        }
        return theOffer;
    }

    private String encodePassword(String rawPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(rawPassword);
    }

    @Autowired
    public void setOfferManager(OfferManager offerManager) {
        this.offerManager = offerManager;
    }

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @Autowired
    public void setCommentManager(CommentManager commentManager) {
        this.commentManager = commentManager;
    }

    @Autowired
    public void setCompanyManager(CompanyManager companyManager) {
        this.companyManager = companyManager;
    }
}
