package com.aporlaoferta.affiliations;

import com.aporlaoferta.model.CompanyAffiliateType;
import com.aporlaoferta.model.OfferCompany;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 19/07/15
 * Time: 17:41
 */
public class TradetrackerAffiliationManagerTest extends AffiliationManagerTest {

    //http://clk.tradedoubler.com/click?p(263037)a(2913754)g(22696148)url(http://)
    private static final String url = "https://es.aliexpress.com/item/Xiaomi-Redmi-4-Pro-Snapdragon-625-5-0-Inch-3GB-RAM-32GB-ROM-4100mAh-13-0MP/32780706200.html?spm=2114.11040108.esplaza.4.55vZXb";
    private static final String url_encoded = "https%3A%2F%2Fes.aliexpress.com%2Fitem%2FXiaomi-Redmi-4-Pro-Snapdragon-625-5-0-Inch-3GB-RAM-32GB-ROM-4100mAh-13-0MP%2F32780706200.html%3Fspm%3D2114.11040108.esplaza.4.55vZXb";
    private static final String c = "15716";
    private static final String m = "12";
    private static final String a = "265784";
    private static final String NO_KEY_PREFIX = "http://www.banggood.com/bang/?tt=16944_12_265784_&r=";

    @Test
    public void testOnlyTradedoublerAffiliationsAreRecognised() throws Exception {
        String affiliatedLink = this.affiliationManager.constructAffiliatedLink(createTradetrackerCompany(), url);
        assertTrue("Expected Tradetracker formatted link to have url",
                affiliatedLink.indexOf(String.format("u=%s", url_encoded)) > 0);
        assertTrue("Expected Tradetracker formatted link to have c parameter",
                affiliatedLink.indexOf(String.format("?c=%s", c)) > 0);
        assertTrue("Expected Tradetracker formatted link to have m parameter",
                affiliatedLink.indexOf(String.format("&m=%s", m)) > 0);
        assertTrue("Expected Tradetracker formatted link to have a parameter",
                affiliatedLink.indexOf(String.format("&a=%s", a)) > 0);
    }

    @Test
    public void testAffiliationWithNoIdKeyCreatesUrlBasedOnAffiliationID() throws Exception {
        String affiliatedLink = this.affiliationManager.constructAffiliatedLink(createTradetrackerCompanyWithNoId(), url);
        assertEquals("Expected no-key Tradetracker formatted link to have url",
                affiliatedLink, String.format("%s%s", NO_KEY_PREFIX, url_encoded));
    }

    private OfferCompany createTradetrackerCompanyWithNoId() {
        OfferCompany tradedoublerAffiliatedCompany = new OfferCompany();
        tradedoublerAffiliatedCompany.setCompanyName("CoolAffiliatedCompany");
        tradedoublerAffiliatedCompany.setCompanyAffiliateType(CompanyAffiliateType.TRADETRACKER);
        tradedoublerAffiliatedCompany.setCompanyAffiliateId(NO_KEY_PREFIX);
        tradedoublerAffiliatedCompany.setCompanyAffiliateIdKey("");
        return tradedoublerAffiliatedCompany;
    }

    private OfferCompany createTradetrackerCompany() {
        OfferCompany tradedoublerAffiliatedCompany = new OfferCompany();
        tradedoublerAffiliatedCompany.setCompanyName("CoolAffiliatedCompany");
        tradedoublerAffiliatedCompany.setCompanyAffiliateType(CompanyAffiliateType.TRADETRACKER);
        tradedoublerAffiliatedCompany.setCompanyAffiliateId(String.format("%s %s %s", c, m, a));
        tradedoublerAffiliatedCompany.setCompanyAffiliateIdKey("c m a");
        return tradedoublerAffiliatedCompany;
    }
}
