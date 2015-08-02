package com.aporlaoferta.model;

import com.aporlaoferta.utils.CommentComparator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.util.Assert.notNull;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 14/01/2015
 * Time: 17:52
 */
@Entity
@Table(name = "thatoffer")
public class TheOffer implements Serializable {

    private static final long serialVersionUID = 6130253929570775380L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TO_ID")
    private Long id;

    @Version
    @Column(name = "TO_VERSION_ID")
    private Long version;

    @Column(name = "TO_TITLE", nullable = false)
    private String offerTitle;

    @Column(name = "TO_IMAGE", nullable = false)
    private String offerImage;

    @Column(name = "TO_DESCRIPTION", nullable = false, length = 2000)
    private String offerDescription;

    @Column(name = "TO_LINK", nullable = false,length = 2000)
    private String offerLink;

    @Column(name = "TO_EXPIRED", nullable = false)
    private boolean offerExpired;

    @Column(name = "TO_FINAL_PRICE", nullable = false)
    private BigDecimal finalPrice;

    @Column(name = "TO_ORIGINAL_PRICE", nullable = true)
    private BigDecimal originalPrice;

    @Column(name = "TO_CREATED_DATE", nullable = true)
    @CreatedDate
    private Date offerCreatedDate;

    @Column(name = "TO_POSITIVE", nullable = true)
    private Long offerPositiveVote;

    @Column(name = "TO_NEGATIVE", nullable = true)
    private Long offerNegativeVote;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TO_COMPANY", nullable = false)
    private OfferCompany offerCompany;

    @Enumerated(EnumType.STRING)
    @Column(name = "TO_CATEGORY", nullable = false)
    private OfferCategory offerCategory;

    @ManyToOne
    @JoinColumn(name = "TO_USER", nullable = false)
    private TheUser offerUser;

    @OneToMany(mappedBy = "commentsOffer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@OneToMany(mappedBy = "commentsOffer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OfferComment> offerComments = new ArrayList<>();

    //@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "OFFER_HAS_POSITIVE", joinColumns = {@JoinColumn(name = "TO_ID")}, inverseJoinColumns = {@JoinColumn(name = "TU_ID")})
    private Set<TheUser> offerPositives = new HashSet<>();

    //@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "OFFER_HAS_NEGATIVE", joinColumns = {@JoinColumn(name = "TO_ID")}, inverseJoinColumns = {@JoinColumn(name = "TU_ID")})
    private Set<TheUser> offerNegatives = new HashSet<>();

    public void incrementPositiveFeedback() {
        setOfferPositiveVote(getOfferPositiveVote() + 1);
    }

    public void incrementNegativeFeedback() {
        setOfferNegativeVote(getOfferNegativeVote() + 1);
    }

    public void addPositiveUser(TheUser theUser) {
        notNull(theUser, "Atempting to add null user object to positive feedback list");
        if (!this.offerPositives.contains(theUser)) {
            this.offerPositives.add(theUser);
        }
    }

    public void setOfferPositives(Set<TheUser> offerPositives) {
        this.offerPositives = offerPositives;
    }

    public void setOfferNegatives(Set<TheUser> offerNegatives) {
        this.offerNegatives = offerNegatives;
    }

    public void addNegativeUser(TheUser theUser) {
        notNull(theUser, "Atempting to add null user object to negative feedback list");
        if (!this.offerNegatives.contains(theUser)) {
            this.offerNegatives.add(theUser);
        }
    }

    public Long getOfferPositiveVote() {
        return offerPositiveVote != null ? offerPositiveVote : 0L;
    }

    public void setOfferPositiveVote(Long offerPositiveVote) {
        this.offerPositiveVote = offerPositiveVote;
    }

    public Long getOfferNegativeVote() {
        return offerNegativeVote != null ? offerNegativeVote : 0L;
    }

    public void setOfferNegativeVote(Long offerNegativeVote) {
        this.offerNegativeVote = offerNegativeVote;
    }

    public OfferCategory getOfferCategory() {
        return offerCategory;
    }

    public void setOfferCategory(OfferCategory offerCategory) {
        this.offerCategory = offerCategory;
    }

    public boolean isOfferExpired() {
        return offerExpired;
    }

    public void setOfferExpired(boolean offerExpired) {
        this.offerExpired = offerExpired;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }

    public Date getOfferCreatedDate() {
        return offerCreatedDate;
    }

    public void setOfferCreatedDate(Date offerCreatedDate) {
        this.offerCreatedDate = offerCreatedDate;
    }

    public String getOfferImage() {
        return offerImage;
    }

    public void setOfferImage(String offerImage) {
        this.offerImage = offerImage;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public String getOfferLink() {
        return offerLink;
    }

    public void setOfferLink(String offerLink) {
        this.offerLink = offerLink;
    }

    public OfferCompany getOfferCompany() {
        return offerCompany;
    }

    public void setOfferCompany(OfferCompany offerCompany) {
        this.offerCompany = offerCompany;
    }

    public TheUser getOfferUser() {
        return offerUser;
    }

    public void setOfferUser(TheUser offerUser) {
        this.offerUser = offerUser;
    }

    public List<OfferComment> getOfferComments() {
        return offerComments;
    }

    public void setOfferComments(List<OfferComment> offerComments) {
        this.offerComments = offerComments;
    }

    public void addComment(OfferComment offerComment) {
        notNull(offerComment, "Attempting to add null comment object to offer.");
        offerComment.setCommentsOffer(this);
        if (this.offerComments == null) {
            this.offerComments = new ArrayList<>();
        }
        this.offerComments.add(offerComment);
    }

    public void sortOfferComments() {
        Collections.sort(this.offerComments, new CommentComparator());
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TheOffer)) return false;

        TheOffer theOffer = (TheOffer) o;

        if (id != null ? !id.equals(theOffer.id) : theOffer.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
