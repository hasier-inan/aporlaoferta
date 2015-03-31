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

    @OneToMany(mappedBy = "commentsOffer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TheOffer)) return false;

        TheOffer theOffer = (TheOffer) o;

        if (offerExpired != theOffer.offerExpired) return false;
        if (finalPrice != null ? !finalPrice.equals(theOffer.finalPrice) : theOffer.finalPrice != null) return false;
        if (id != null ? !id.equals(theOffer.id) : theOffer.id != null) return false;
        if (offerCategory != theOffer.offerCategory) return false;
        if (offerCompany != null ? !offerCompany.equals(theOffer.offerCompany) : theOffer.offerCompany != null)
            return false;
        if (offerCreatedDate != null ? !offerCreatedDate.equals(theOffer.offerCreatedDate)
                : theOffer.offerCreatedDate != null)
            return false;
        if (offerDescription != null ? !offerDescription.equals(theOffer.offerDescription)
                : theOffer.offerDescription != null)
            return false;
        if (offerImage != null ? !offerImage.equals(theOffer.offerImage) : theOffer.offerImage != null) return false;
        if (offerLink != null ? !offerLink.equals(theOffer.offerLink) : theOffer.offerLink != null) return false;
        if (offerNegativeVote != null ? !offerNegativeVote.equals(theOffer.offerNegativeVote)
                : theOffer.offerNegativeVote != null)
            return false;
        if (offerPositiveVote != null ? !offerPositiveVote.equals(theOffer.offerPositiveVote)
                : theOffer.offerPositiveVote != null)
            return false;
        if (offerTitle != null ? !offerTitle.equals(theOffer.offerTitle) : theOffer.offerTitle != null) return false;
        if (offerUser != null ? !offerUser.equals(theOffer.offerUser) : theOffer.offerUser != null) return false;
        if (originalPrice != null ? !originalPrice.equals(theOffer.originalPrice) : theOffer.originalPrice != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (offerTitle != null ? offerTitle.hashCode() : 0);
        result = 31 * result + (offerImage != null ? offerImage.hashCode() : 0);
        result = 31 * result + (offerDescription != null ? offerDescription.hashCode() : 0);
        result = 31 * result + (offerLink != null ? offerLink.hashCode() : 0);
        result = 31 * result + (offerExpired ? 1 : 0);
        result = 31 * result + (finalPrice != null ? finalPrice.hashCode() : 0);
        result = 31 * result + (originalPrice != null ? originalPrice.hashCode() : 0);
        result = 31 * result + (offerCreatedDate != null ? offerCreatedDate.hashCode() : 0);
        result = 31 * result + (offerPositiveVote != null ? offerPositiveVote.hashCode() : 0);
        result = 31 * result + (offerNegativeVote != null ? offerNegativeVote.hashCode() : 0);
        result = 31 * result + (offerCompany != null ? offerCompany.hashCode() : 0);
        result = 31 * result + (offerCategory != null ? offerCategory.hashCode() : 0);
        result = 31 * result + (offerUser != null ? offerUser.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TheOffer{");
        sb.append("id=").append(id);
        sb.append(", version=").append(version);
        sb.append(", offerTitle='").append(offerTitle).append('\'');
        sb.append(", offerImage='").append(offerImage).append('\'');
        sb.append(", offerDescription='").append(offerDescription).append('\'');
        sb.append(", offerLink='").append(offerLink).append('\'');
        sb.append(", offerExpired=").append(offerExpired);
        sb.append(", finalPrice=").append(finalPrice);
        sb.append(", originalPrice=").append(originalPrice);
        sb.append(", offerCreatedDate=").append(offerCreatedDate);
        sb.append(", offerPositiveVote=").append(offerPositiveVote);
        sb.append(", offerNegativeVote=").append(offerNegativeVote);
        sb.append(", offerCompany=").append(offerCompany);
        sb.append(", offerCategory=").append(offerCategory);
        sb.append(", offerUser=").append(offerUser);
        sb.append('}');
        return sb.toString();
    }
}
