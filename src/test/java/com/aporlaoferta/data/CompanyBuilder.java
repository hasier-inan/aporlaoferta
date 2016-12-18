package com.aporlaoferta.data;

import com.aporlaoferta.model.CompanyAffiliateType;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.model.TheOffer;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 14/11/2014
 * Time: 16:00
 */
public class CompanyBuilder {

    private String name;
    private String url;
    private String logo;
    private String affiliateId;
    private String affiliateIdKey;
    private Long id;
    private String watermarks;
    private CompanyAffiliateType companyAffiliateType;

    public static CompanyBuilder aCompany() {
        return new CompanyBuilder();
    }

    public CompanyBuilder() {
        super();
    }

    public CompanyBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CompanyBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CompanyBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    public CompanyBuilder withLogo(String logo) {
        this.logo = logo;
        return this;
    }

    public CompanyBuilder withAffiliateId(String affiliateId) {
        this.affiliateId = affiliateId;
        return this;
    }

    public CompanyBuilder withAffiliateIdKey(String affiliateIdKey) {
        this.affiliateIdKey = affiliateIdKey;
        return this;
    }

    public CompanyBuilder withWatermarks(String watermarks) {
        this.watermarks = watermarks;
        return this;
    }

    public CompanyBuilder withCompanyAffiliateType(CompanyAffiliateType companyAffiliateType) {
        this.companyAffiliateType = companyAffiliateType;
        return this;
    }

    public OfferCompany build() {
        OfferCompany offerCompany = new OfferCompany();
        offerCompany.setCompanyAffiliateId(this.affiliateId);
        offerCompany.setCompanyAffiliateIdKey(this.affiliateIdKey);
        offerCompany.setCompanyLogoUrl(this.logo);
        offerCompany.setCompanyUrl(this.url);
        offerCompany.setCompanyName(this.name);
        offerCompany.setId(this.id);
        offerCompany.setCompanyWatermarks(this.watermarks);
        if (this.companyAffiliateType != null) {
            offerCompany.setCompanyAffiliateType(this.companyAffiliateType);
        }
        return offerCompany;
    }
}
