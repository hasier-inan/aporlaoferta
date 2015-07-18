package com.aporlaoferta.offer;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.endsWith;

/**
 * Created by hasiermetal on 22/01/15.
 */
public class CompanyManagerAmazonTestIntegration extends CompanyManagerTestIntegration {

    @Test
    public void testAmazonAffiliationIdIsIncludedInTheFinalUrk() {
        //http://www.amazon.es/dp/B00VIA4N6S/?tag=aporlaoferta-21
        String link = "http://www.amazon.es/dp/B00VIA4N6S/";
        this.offerCompany.setCompanyName("Amazon");
        String amazonAffiliateId = "aporlaoferta-21";
        this.offerCompany.setCompanyAffiliateId(amazonAffiliateId);
        Assert.assertThat("Expected affiliateId to be part of the final Link", this.companyManager.createAffiliationLink(this.offerCompany, link), endsWith(amazonAffiliateId));
    }

    @Test
    public void testAmazonAffiliationIdHasBeenReplacedIfItWasAlreadyThere() throws Exception {


    }

    @Test
    public void testAmazonItemIdIsExtractedFromRawLinkAndCustomLinkIsProvided() throws Exception {


    }


}

