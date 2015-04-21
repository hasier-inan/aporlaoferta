package com.aporlaoferta.model;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.util.Assert.notNull;

/**
 * Created with IntelliJ IDEA.
 * User: hinan
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

    @Column(name = "TO_DESCRIPTION", nullable = false, length = 1000)
    private String offerDescription;

    @Column(name = "TO_LINK", nullable = false)
    private String offerLink;

    @Column(name = "TO_EXPIRED", nullable = false)
    private boolean offerExpired;

    @Column(name = "TO_FINAL_PRICE", nullable = false)
    private BigDecimal finalPrice;

    @Column(name = "TO_ORIGINAL_PRICE", nullable = true)
    private BigDecimal originalPrice;

    @Column(name = "TO_CREATED_DATE", nullable = false)
    @CreatedDate
    private Date offerCreatedDate;

    @Column(name = "TO_POSITIVE", nullable = false)
    private Long offerPositiveVote;
    @Column(name = "TO_negative", nullable = false)
    private Long offerNegativeVote;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TO_COMPANY", nullable = false)
    private OfferCompany offerCompany;

    @Enumerated(EnumType.STRING)
    @Column(name = "TO_CATEGORY", nullable = false)
    private OfferCategory offerCategory;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TO_USER", nullable = false)
    private TheUser offerUser;

    @OneToMany(mappedBy = "commentsOffer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<OfferComment> offerComments = new HashSet<>();

    public Long getOfferPositiveVote() {
        return offerPositiveVote;
    }

    public void setOfferPositiveVote(Long offerPositiveVote) {
        this.offerPositiveVote = offerPositiveVote;
    }

    public Long getOfferNegativeVote() {
        return offerNegativeVote;
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

    public Set<OfferComment> getOfferComments() {
        return offerComments;
    }

    public void setOfferComments(Set<OfferComment> offerComments) {
        this.offerComments = offerComments;
    }

    public void addComment(OfferComment offerComment) {
        notNull(offerComment, "Attempting to add null comment object to offer.");
        offerComment.setCommentsOffer(this);
        if (this.offerComments == null) {
            this.offerComments = new HashSet<>();
        }
        this.offerComments.add(offerComment);
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
}
