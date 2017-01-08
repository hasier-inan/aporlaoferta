package com.aporlaoferta.affiliations;

import com.aporlaoferta.model.CompanyAffiliateType;
import com.aporlaoferta.model.OfferCompany;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 08/01/17
 * Time: 13:05
 */
public class ZanoxAffiliationManagerTest extends AffiliationManagerTest {

    //https://ad.zanox.com/ppc/?41050400C1620134037&ulp=[[www.edreams.es/travel/dp/?_ga=1.128345540.936227762.1482174763&...]]
    private static final String url = "www.edreams.es/travel/dp/?_ga=1.128345540.936227762.1482174763";
    private static final String partner_id = "41050400C1620134037";

    @Test
    public void testOnlyTradedoublerAffiliationsAreRecognised() throws Exception {
        String affiliatedLink = this.affiliationManager.constructAffiliatedLink(createZanoxCompany(), url);
        assertTrue("Expected Tradedoubler formatted link to have url",
                affiliatedLink.indexOf(String.format("ulp=[[%s]]", url)) > 0);
        assertTrue("Expected Tradedoubler formatted link to have partner id",
                affiliatedLink.indexOf(String.format("ppc/?%s", partner_id)) > 0);
    }

    private OfferCompany createZanoxCompany() {
        OfferCompany tradedoublerAffiliatedCompany = new OfferCompany();
        tradedoublerAffiliatedCompany.setCompanyName("CoolAffiliatedCompany");
        tradedoublerAffiliatedCompany.setCompanyAffiliateType(CompanyAffiliateType.ZANOX);
        tradedoublerAffiliatedCompany.setCompanyAffiliateId(partner_id);
        return tradedoublerAffiliatedCompany;
    }
}
