package com.aporlaoferta.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.util.Assert.notNull;

/**
 * Created with IntelliJ IDEA.
 * User: hinan
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
    private String companyAffiliateId;

    @OneToMany(mappedBy = "offerCompany", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfferCompany)) return false;

        OfferCompany that = (OfferCompany) o;

        if (companyAffiliateId != null ? !companyAffiliateId.equals(that.companyAffiliateId)
                : that.companyAffiliateId != null)
            return false;
        if (companyLogoUrl != null ? !companyLogoUrl.equals(that.companyLogoUrl) : that.companyLogoUrl != null)
            return false;
        if (companyName != null ? !companyName.equals(that.companyName) : that.companyName != null) return false;
        if (companyUrl != null ? !companyUrl.equals(that.companyUrl) : that.companyUrl != null) return false;
        if (companyOffers != null ? !companyOffers.equals(that.companyOffers) : that.companyOffers != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = companyName != null ? companyName.hashCode() : 0;
        result = 31 * result + (companyUrl != null ? companyUrl.hashCode() : 0);
        result = 31 * result + (companyLogoUrl != null ? companyLogoUrl.hashCode() : 0);
        result = 31 * result + (companyAffiliateId != null ? companyAffiliateId.hashCode() : 0);
        result = 31 * result + (companyOffers != null ? companyOffers.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OfferCompany{");
        sb.append("id=").append(id);
        sb.append(", version=").append(version);
        sb.append(", companyName='").append(companyName).append('\'');
        sb.append(", companyUrl='").append(companyUrl).append('\'');
        sb.append(", companyLogoUrl='").append(companyLogoUrl).append('\'');
        sb.append(", companyAffiliateId='").append(companyAffiliateId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
