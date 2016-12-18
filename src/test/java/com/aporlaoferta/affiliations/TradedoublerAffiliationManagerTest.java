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
public class TradedoublerAffiliationManagerTest extends AffiliationManagerTest {

    //http://clk.tradedoubler.com/click?p(263037)a(2913754)g(22696148)url(http://)
    private static final String url = "http://www.something.com/";
    private static final String p = "263037";
    private static final String a = "2913754";
    private static final String g = "22696148";

    @Test
    public void testOnlyTradedoublerAffiliationsAreRecognised() throws Exception {
        String affiliatedLink = this.affiliationManager.constructAffiliatedLink(createTradedoublerCompany(), url);
        assertTrue("Expected Tradedoubler formatted link to have url",
                affiliatedLink.indexOf(String.format("url(%s)", url)) > 0);
        assertTrue("Expected Tradedoubler formatted link to have p parameter",
                affiliatedLink.indexOf(String.format("p(%s)", p)) > 0);
        assertTrue("Expected Tradedoubler formatted link to have a parameter",
                affiliatedLink.indexOf(String.format("a(%s)", a)) > 0);
        assertTrue("Expected Tradedoubler formatted link to have g parameter",
                affiliatedLink.indexOf(String.format("g(%s)", g)) > 0);
    }

    @Test
    public void testExceptionCompanyLastMinuteContainsExclamationMark() throws Exception {
        String affiliatedLink = this.affiliationManager.constructAffiliatedLink(createLastMinuteCompany(), url);
        assertTrue("Expected Tradedoubler formatted link to have url",
                affiliatedLink.indexOf(String.format("url(%s?)", url)) > 0);
    }

    private OfferCompany createLastMinuteCompany() {
        OfferCompany lastMinute = createTradedoublerCompany();
        lastMinute.setCompanyName("Lastminute");
        return lastMinute;
    }

    private OfferCompany createTradedoublerCompany() {
        OfferCompany tradedoublerAffiliatedCompany = new OfferCompany();
        tradedoublerAffiliatedCompany.setCompanyName("CoolAffiliatedCompany");
        tradedoublerAffiliatedCompany.setCompanyAffiliateType(CompanyAffiliateType.TRADEDOUBLER);
        tradedoublerAffiliatedCompany.setCompanyAffiliateId(String.format("%s %s %s", p, a, g));
        tradedoublerAffiliatedCompany.setCompanyAffiliateIdKey("p a g");
        return tradedoublerAffiliatedCompany;
    }
}
