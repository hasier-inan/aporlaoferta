package com.aporlaoferta.affiliations;

import com.aporlaoferta.model.CompanyAffiliateType;
import com.aporlaoferta.model.OfferCompany;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 19/07/15
 * Time: 17:41
 */
public class BelboonAffiliationManagerTest extends AffiliationManagerTest {

    //http://www1.belboon.de/adtracking/0395c2093bd10450020052f1.html/&deeplink=http://www.opirata.com/es/innjoo-fire-gris-p-39959.html"
    private static final String url = "http://www.opirata.com/es/innjoo-fire-gris-p-39959.html";
    private static final String companyId = "0395c2093bd10450020052f1";

    @Test
    public void testOnlyTradedoublerAffiliationsAreRecognised() throws Exception {
        String affiliatedLink = this.affiliationManager.constructAffiliatedLink(createBelboonCompany(), url);
        assertTrue("Expected Belboon formatted link to have url",
                affiliatedLink.indexOf(String.format("&deeplink=%s", url)) > 0);
        assertTrue("Expected Tradedoubler formatted link to have company id",
                affiliatedLink.indexOf(String.format("www1.belboon.de/adtracking/%s.html", companyId)) > 0);
    }

    private OfferCompany createBelboonCompany() {
        OfferCompany tradedoublerAffiliatedCompany = new OfferCompany();
        tradedoublerAffiliatedCompany.setCompanyName("CoolAffiliatedCompany");
        tradedoublerAffiliatedCompany.setCompanyAffiliateType(CompanyAffiliateType.BELBOON);
        tradedoublerAffiliatedCompany.setCompanyAffiliateId(companyId);
        return tradedoublerAffiliatedCompany;
    }
}
