package com.aporlaoferta.controller;

import com.aporlaoferta.controller.helpers.DatabaseHelper;
import com.aporlaoferta.model.OfferCategory;
import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.model.OfferListResponse;
import com.aporlaoferta.model.TheDefaultOffer;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.validators.ValidationException;
import com.aporlaoferta.service.CaptchaHTTPManager;
import com.aporlaoferta.service.CompanyManager;
import com.aporlaoferta.service.InvalidOfferException;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.UserManager;
import com.aporlaoferta.utils.LevenshteinDistance;
import com.aporlaoferta.utils.OfferValidatorHelper;
import com.aporlaoferta.utils.UnhealthyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by hasiermetal on 2/02/14.
 */
public class OfferHelper {

    protected final Logger LOG = LoggerFactory.getLogger(OfferHelper.class);

    @Autowired
    protected OfferValidatorHelper offerValidatorHelper;

    @Autowired
    protected UserManager userManager;

    @Autowired
    protected OfferManager offerManager;

    @Autowired
    protected CompanyManager companyManager;

    @Autowired
    protected CaptchaHTTPManager captchaHttpManager;


    protected void includeOfferMeta(ModelAndView model, TheOffer theOffer) {
        model.addObject("offerId", theOffer.getId());
        model.addObject("offerKeywords", theOffer.getOfferTitle());
        model.addObject("offerTitle", String.format("%s: %s", theOffer.getOfferTitle(), theOffer.getFinalPrice().toString()));
        model.addObject("offerDescription", theOffer.getOfferDescription());
        String offerImage = theOffer.getOfferImage();
        model.addObject("offerImage", offerImage.indexOf("offer.png") >= 0 ?
                String.format("https://www.aporlaoferta.com%s", offerImage) : offerImage);
    }

    protected TheResponse processOfferCreation(TheOffer thatOffer) {
        includeInitialValues(thatOffer);
        updateCompany(thatOffer);
        try {
            includeAffiliationLink(thatOffer);
            return validateAndPersistOffer(thatOffer);
        } catch (MalformedURLException e) {
            LOG.error("Could not add custom affiliation id to offer url because url is invalid: ", e);
        } catch (UnsupportedEncodingException e) {
            LOG.error("Could not add custom affiliation id to offer url because url is wrongly encoded: ", e);
        } catch (UnhealthyException e) {
            LOG.error("Could not add custom affiliation id to offer url because url is unhealthy: ", e);
            return ResponseResultHelper.createUnhealthyResponse();
        }
        return ResponseResultHelper.createDefaultResponse();
    }

    protected void includeInitialValues(TheOffer thatOffer) {
        includeDefaultImage(thatOffer);
        includeOfferInUser(thatOffer);
        thatOffer.setOfferPositiveVote(0L);
        thatOffer.setOfferNegativeVote(0L);
        thatOffer.setOfferCreatedDate(new Date());
    }

    protected void includeDefaultImage(TheOffer thatOffer) {
        if (isEmpty(thatOffer.getOfferImage())) {
            thatOffer.setOfferImage(TheDefaultOffer.OFFER_IMAGE_URL.getCode());
        }
    }

    protected void includeAffiliationLink(TheOffer thatOffer) throws MalformedURLException, UnsupportedEncodingException, UnhealthyException {
        thatOffer.setOfferLinkUser(thatOffer.getOfferLink());
        thatOffer.setOfferLink(this.companyManager.createAffiliationLink(
                thatOffer.getOfferCompany(), thatOffer.getOfferLink()));
    }

    protected void updateCompany(TheOffer thatOffer) {
        if (thatOffer.getOfferCompany() != null) {
            String watermarkedCompany = this.companyManager.getWatermarkedCompany(thatOffer.getOfferLink().toLowerCase());
            if (!isEmpty(watermarkedCompany)) {
                updateCompanyFromWatermark(thatOffer, watermarkedCompany);
            } else {
                updateCompanyFromName(thatOffer);
            }
        }
    }

    protected void updateCompanyFromWatermark(TheOffer thatOffer, String watermarkedCompany) {
        thatOffer.setOfferCompany(this.companyManager.getCompanyFromName(watermarkedCompany));
    }

    protected void updateCompanyFromName(TheOffer thatOffer) {
        String usedName = thatOffer.getOfferCompany().getCompanyName();
        OfferCompany offerCompany = this.companyManager.getCompanyFromName(usedName);
        if (offerCompany != null) {
            thatOffer.setOfferCompany(offerCompany);
        } else {
            updateCompanyFromLevenshtein(thatOffer, usedName);
        }
    }

    protected void updateCompanyFromLevenshtein(TheOffer thatOffer, String usedName) {
        List<OfferCompany> allCompanies = this.companyManager.getAllCompanies();
        for (OfferCompany company : allCompanies) {
            if (LevenshteinDistance.process(usedName, company.getCompanyName(), 3)) {
                thatOffer.setOfferCompany(company);
                return;
            }
        }
    }

    protected TheResponse processOfferUpdate(TheOffer theOffer) {
        try {
            Long offerId = isEmpty(theOffer.getId()) ? -1L : theOffer.getId();
            TheOffer originalOffer = this.offerManager.getOfferFromId(offerId);
            if (originalOffer == null) {
                throw new ValidationException("Invalid offer id");
            }
            this.offerValidatorHelper.validateOffer(theOffer);
            return updateOfferFromOriginal(theOffer, originalOffer);
        } catch (ValidationException | NumberFormatException e) {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.UPDATE_OFFER_VALIDATION_ERROR, new TheResponse());
        } catch (MalformedURLException e) {
            LOG.error("Could not add custom affiliation id to offer url because url is invalid: ", e);
        } catch (UnsupportedEncodingException e) {
            LOG.error("Could not add custom affiliation id to offer url because url is wrongly encoded: ", e);
        } catch (UnhealthyException e) {
            LOG.error("Could not add custom affiliation id to offer url because url is unhealthy: ", e);
            return ResponseResultHelper.createUnhealthyResponse();
        }
        return ResponseResultHelper.createDefaultResponse();
    }

    protected void addCategoryIntoList(List<Map<String, String>> offerCategoriesList, OfferCategory offerCategory) {
        Map<String, String> offerCategories = new HashMap<>();
        offerCategories.put("text", offerCategory.name());
        offerCategories.put("value", offerCategory.getCode());
        offerCategories.put("display", offerCategory.getText());
        offerCategoriesList.add(offerCategories);
    }

    protected void createEmptyFields(List<TheOffer> hundredOffers) {
        for (TheOffer theOffer : hundredOffers) {
            theOffer.setOfferComments(new ArrayList<OfferComment>());
        }
    }

    protected void updateResponseWithSuccessCode(OfferListResponse theOfferResponse) {
        theOfferResponse.setCode(ResultCode.ALL_OK.getCode());
        theOfferResponse.setDescription(ResultCode.ALL_OK.getResultDescriptionEsp());
        theOfferResponse.setResponseResult(ResultCode.ALL_OK.getResponseResult());
    }

    protected TheResponse updateOfferFromOriginal(TheOffer theOffer, TheOffer originalOffer) throws UnsupportedEncodingException, UnhealthyException, MalformedURLException {
        TheResponse result = new TheResponse();
        if (!this.userManager.isUserAuthorised(theOffer)) {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.INVALID_OWNER_ERROR, result);
        }
        TheOffer updatedOffer = updateOfferWithLastDetails(theOffer, originalOffer);
        includeAffiliationLink(updatedOffer);
        return validateAndPersistOffer(updatedOffer);
    }

    protected TheOffer updateOfferWithLastDetails(TheOffer theUpdatedOffer, TheOffer originalOffer) throws UnsupportedEncodingException, UnhealthyException, MalformedURLException {
        originalOffer.setFinalPrice(theUpdatedOffer.getFinalPrice());
        originalOffer.setOfferTitle(theUpdatedOffer.getOfferTitle());
        if (!originalOffer.getOfferCompany().getCompanyName().equals(
                theUpdatedOffer.getOfferCompany().getCompanyName())) {
            originalOffer.setOfferCompany(theUpdatedOffer.getOfferCompany());
            updateCompany(originalOffer);
        }
        originalOffer.setOfferCategory(theUpdatedOffer.getOfferCategory());
        originalOffer.setOfferDescription(theUpdatedOffer.getOfferDescription());
        originalOffer.setOfferExpired(theUpdatedOffer.isOfferExpired());
        originalOffer.setOfferImage(theUpdatedOffer.getOfferImage());
        originalOffer.setOfferLink(theUpdatedOffer.getOfferLink());
        originalOffer.setOriginalPrice(theUpdatedOffer.getOriginalPrice());
        return originalOffer;
    }

    protected TheResponse validateAndPersistOffer(TheOffer thatOffer) {
        try {
            this.offerValidatorHelper.validateOffer(thatOffer);
            return saveOfferAndUpdateResult(thatOffer);
        } catch (ValidationException e) {
            LOG.warn(ResultCode.CREATE_OFFER_VALIDATION_ERROR.getResultDescription(), e);
            return ResponseResultHelper.offerValidationErrorResponse();
        }
    }

    protected TheResponse saveOfferAndUpdateResult(TheOffer thatOffer) {
        TheUser theUser = this.userManager.saveUser(thatOffer.getOfferUser());
        TheOffer theOffer = theUser.obtainLatestOffer();
        TheResponse result = new TheResponse();
        if (theOffer == null) {
            DatabaseHelper.addEmptyDatabaseObjectMessage(result, LOG);
        } else {
            String okMessage = String.format("Offer successfully updated. Id: %s", theOffer.getId());
            LOG.info(okMessage);
            Long updatedOfferId = isEmpty(thatOffer.getId()) ? theOffer.getId() : thatOffer.getId();
            result = ResponseResultHelper.offerUpdateResponse(updatedOfferId.toString());
        }
        return result;
    }

    protected void includeOfferInUser(TheOffer thatOffer) {
        String nickName = this.userManager.getUserNickNameFromSession();
        TheUser theUser = this.userManager.getUserFromNickname(nickName);
        if (theUser != null) {
            theUser.addOffer(thatOffer);
        }
    }

    protected TheOffer getFixedOfferFromId(Long number) throws InvalidOfferException {
        TheOffer theOffer = this.offerManager.getOfferFromId(number);
        if (isEmpty(theOffer)) {
            throw new InvalidOfferException(String.format("Offer %s not found.", number.toString()));
        }
        resetCompanySensibleDataFromOffer(theOffer);
        theOffer.sortOfferComments();
        return theOffer;
    }

    protected void resetCompanySensibleDataFromOffer(TheOffer theOffer) {
        OfferCompany offerCompany = theOffer.getOfferCompany();
        offerCompany.setCompanyOffers(new HashSet<TheOffer>());
        theOffer.setOfferCompany(offerCompany);
    }
}
