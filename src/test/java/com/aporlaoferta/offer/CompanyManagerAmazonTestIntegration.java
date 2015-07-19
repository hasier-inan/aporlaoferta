package com.aporlaoferta.offer;

import com.aporlaoferta.data.CompanyBuilderManager;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.utils.UnhealthyException;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * Created by hasiermetal on 22/01/15.
 */
public class CompanyManagerAmazonTestIntegration extends CompanyManagerTestIntegration {

    private final String amazonAffiliateId = "aporlaoferta-21";
    private final String amazonName = "Amazon";

    @Test
    public void testAmazonAffiliationIdIsIncludedInTheFinalUrl() throws MalformedURLException, UnsupportedEncodingException, UnhealthyException {
        //http://www.amazon.es/dp/B00VIA4N6S/?tag=aporlaoferta-21
        String link = "http://www.amazon.es/dp/B00VIA4N6S/";
        OfferCompany offerCompany = createAmazonCompanyWithAffiliateId();
        assertThat("Expected affiliateId to be part of the final Link", this.companyManager.createAffiliationLink(offerCompany, link), endsWith(amazonAffiliateId));
    }

    @Test
    public void testAmazonAffiliationIdHasBeenReplacedIfItWasAlreadyThere() throws Exception {
        String unwantedAffiliationId = "yomutha-21";
        String link = "http://www.amazon.es/dp/B00VIA4N6S/?tag=" + unwantedAffiliationId;
        OfferCompany offerCompany = createAmazonCompanyWithAffiliateId();
        String theFinalLink = this.companyManager.createAffiliationLink(offerCompany, link);
        assertThat("Expected personal affiliateId to be present instead of existing one", theFinalLink, endsWith(amazonAffiliateId));
        assertFalse("No other affiliate id should be included in the url", theFinalLink.indexOf(unwantedAffiliationId) >= 0);
    }

    /**
     * It will be unhealthy because of the redirection
     *
     * @throws Exception
     */
    @Test(expected = UnhealthyException.class)
    public void testFinalRawLinkIsObtainedFromShortenerUrl() throws Exception {
        String theHiddenLink = "http://www.amazon.es/gp/product/B00VIA4N6S?&th=1";
        String theShortenerUrl = "http://goo.gl/MnQf11";
        Assert.assertEquals(theHiddenLink, this.companyManager.createAffiliationLink(this.offerCompany, theShortenerUrl));
    }


    private OfferCompany createAmazonCompanyWithAffiliateId() {
        return CompanyBuilderManager.aRegularCompanyWithName(this.amazonName)
                .withAffiliateId(this.amazonAffiliateId)
                .build();
    }


}

