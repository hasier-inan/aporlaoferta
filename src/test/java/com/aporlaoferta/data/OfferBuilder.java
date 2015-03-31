package com.aporlaoferta.data;

import com.aporlaoferta.model.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: hinan
 * Date: 14/11/2014
 * Time: 16:00
 */
public class OfferBuilder {

    private Long id;
    private String title;
    private String image;
    private String description;
    private String link;
    private boolean expired;
    private Date createdOn;
    private Long positives;
    private Long negatives;
    private BigDecimal finalPrice;
    private BigDecimal originalPrice;

    private OfferCompany company;

    private OfferCategory category;

    private TheUser owner;

    private Set<OfferComment> comments;

    public static OfferBuilder anOffer() {
        return new OfferBuilder();
    }

    public OfferBuilder() {
        super();
    }

    public OfferBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OfferBuilder withOriginalPrice(BigDecimal price) {
        this.originalPrice = price;
        return this;
    }

    public OfferBuilder withFinalPrice(BigDecimal price) {
        this.finalPrice = price;
        return this;
    }

    public OfferBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public OfferBuilder withImage(String image) {
        this.image = image;
        return this;
    }

    public OfferBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public OfferBuilder withLink(String link) {
        this.link = link;
        return this;
    }

    public OfferBuilder isExpired(boolean expired) {
        this.expired = expired;
        return this;
    }

    public OfferBuilder createdOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public OfferBuilder withPositives(Long pos) {
        this.positives = pos;
        return this;
    }

    public OfferBuilder withNegatives(Long neg) {
        this.negatives = neg;
        return this;
    }

    public OfferBuilder withCompany(OfferCompany company) {
        this.company = company;
        return this;
    }

    public OfferBuilder withUser(TheUser user) {
        this.owner = user;
        return this;
    }

    public OfferBuilder withCategory(OfferCategory cat) {
        this.category = cat;
        return this;
    }

    public OfferBuilder withComments(Set<OfferComment> comments) {
        this.comments = comments;
        return this;
    }

    public TheOffer build() {
        TheOffer theOffer = new TheOffer();
        theOffer.setId(this.id);
        theOffer.setOfferCategory(this.category);
        theOffer.setOfferComments(this.comments);
        theOffer.setOfferCompany(this.company);
        theOffer.setOfferCreatedDate(this.createdOn);
        theOffer.setOfferDescription(this.description);
        theOffer.setOfferExpired(this.expired);
        theOffer.setOfferImage(this.image);
        theOffer.setOfferLink(this.link);
        theOffer.setOfferTitle(this.title);
        theOffer.setOfferUser(this.owner);
        theOffer.setOriginalPrice(this.originalPrice);
        theOffer.setFinalPrice(this.finalPrice);
        theOffer.setOfferPositiveVote(this.positives);
        theOffer.setOfferNegativeVote(this.negatives);
        return theOffer;
    }
}
