package com.aporlaoferta.affiliations;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 19/07/15
 * Time: 17:41
 */
public class AmazonAffiliationManagerTest extends AffiliationManagerTest {

    private static final String rawUrl = "http://www.amazon.es/dp/B00VIA4N6S/";

    private static final String tooComplexUrl = "http://www.amazon.es/gp/product/B00K690INE/" +
            "ref=s9_simh_gw_p23_d12_i4?pf_rd_m=A1AT7YVPFBWXBL&pf_rd_s=desktop-1&pf_rd_r=0YP4M8XZTZQRA7VN6Y28" +
            "&pf_rd_t=36701&pf_rd_p=579624687&pf_rd_i=desktop";

    private static final String tagAlreadyAppliedUrlMiddle = "http://www.amazon.es/gp/product/B00K690INE/" +
            "ref=s9_simh_gw_p23_d12_i4?pf_rd_m=A1AT7YVPFBWXBL&tag=soyunlistillo-21&pf_rd_s=desktop-1&pf_rd_r=0YP4M8XZTZQRA7VN6Y28" +
            "&pf_rd_t=36701&pf_rd_p=579624687&pf_rd_i=desktop";

    private static final String tagAlreadyAppliedUrlBeginning = "http://www.amazon.es/gp/product/B00K690INE/" +
            "?tag=listillon2&ref=s9_simh_gw_p23_d12_i4?pf_rd_m=A1AT7YVPFBWXBL&tag=soyunlistillo-21&pf_rd_s=desktop-1&pf_rd_r=0YP4M8XZTZQRA7VN6Y28" +
            "&pf_rd_t=36701&pf_rd_p=579624687&pf_rd_i=desktop";

    private static final String tagAlreadyAppliedUrlEnding = "http://www.amazon.es/dp/B00VIA4N6S/?tag=listillofinal-1";

    @Test
    public void testCustomUrlIsReceivedIfCompanyIsAffiliatedAndIdIsIncluded() throws Exception {
        assertAddedRawUrlContainsAffiliateId(this.rawUrl);
        assertAddedRawUrlContainsAffiliateId(this.tooComplexUrl);
        assertAddedRawUrlContainsAffiliateId(this.tagAlreadyAppliedUrlMiddle);
        assertAddedRawUrlContainsAffiliateId(this.tagAlreadyAppliedUrlBeginning);
        assertAddedRawUrlContainsAffiliateId(this.tagAlreadyAppliedUrlEnding);
    }

    private void assertAddedRawUrlContainsAffiliateId(String url) throws InvalidAffiliatedCompany, MalformedURLException, UnsupportedEncodingException {
        String parsedUrl = this.affiliationManager.constructAffiliatedLink(createAmazonAffiliationCompany(), url);
        assertFalse(rawUrl.equals(parsedUrl));
        //unique tag=affiliate-id parameter
        assertEquals("Expected unique affiliate id tag in the url", parsedUrl.split("tag=").length, 2);
    }
}
