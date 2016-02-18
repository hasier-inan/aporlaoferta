package com.aporlaoferta.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.util.Assert.notNull;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 14/01/2015
 * Time: 17:59
 */
@Entity
@Table(name = "thatcompany")
@SequenceGenerator(name = "GEN_THATCOMPANY", sequenceName = "SEQ_THATCOMPANY")
public class OfferCompany implements Serializable {

    private static final long serialVersionUID = -1541978513479794276L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TC_ID")
    private Long id;

    @Version
    @Column(name = "TC_VERSION_ID")
    private Long version;

    @Column(name = "TC_NAME", nullable = false)
    private String companyName;

    @Column(name = "TC_URL", nullable = true)
    private String companyUrl;

    @Column(name = "TC_LOGO_URL", nullable = true)
    private String companyLogoUrl;

    @Column(name = "TC_AFFILIATE_ID", nullable = true)
    @JsonIgnore
    private String companyAffiliateId;

    @Column(name = "TC_WATERMARKS", nullable = true)
    @JsonIgnore
    private String companyWatermarks;

    @OneToMany(mappedBy = "offerCompany", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<TheOffer> companyOffers = new HashSet<TheOffer>();

    public void setCompanyOffers(Set<TheOffer> companyOffers) {
        this.companyOffers = companyOffers;
    }

    public Set<TheOffer> getCompanyOffers() {
        return companyOffers;
    }

    public void addOffer(TheOffer theOffer) {
        notNull(theOffer, "Attempting to add null offer object to company.");
        theOffer.setOfferCompany(this);
        if (this.companyOffers == null) {
            this.companyOffers = new HashSet<>();
        }
        this.companyOffers.add(theOffer);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public void setCompanyLogoUrl(String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }

    public String getCompanyAffiliateId() {
        return companyAffiliateId;
    }

    public void setCompanyAffiliateId(String companyAffiliateId) {
        this.companyAffiliateId = companyAffiliateId;
    }

    public String getCompanyWatermarks() {
        return companyWatermarks;
    }

    public void setCompanyWatermarks(String companyWatermarks) {
        this.companyWatermarks = companyWatermarks;
    }
}
