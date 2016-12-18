package com.aporlaoferta.affiliations;

import com.aporlaoferta.data.CompanyBuilderManager;
import com.aporlaoferta.model.CompanyAffiliateType;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.utils.UrlParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 19/07/15
 * Time: 17:41
 */
public class AffiliationManagerTest {

    protected AffiliationManager affiliationManager;
    private final String rawUrl = "http://www.amazon.es";
    private final String affiliateId = "aporlaoferta-21";
    private final String affiliateIdKey = "tag";
    //aporlaoferta-21   amazon.es   Amazon  amazon.es amazon
    private final String longUrlWithMultipleValues = "http://www.amazon.es/gp/product/B00K690INE/" +
            "?aka=2&ref=s9_simh_gw_p23_d12_i4?pf_rd_m=A1AT7YVPFBWXBL&pf_rd_s=desktop-1" +
            "&pf_rd_r=0YP4M8XZTZQRA7VN6Y28&pf_rd_t=36701" +
            "&pf_rd_p=579624687&pf_rd_i=desktop&tag=sdfsdf";

    private final String dxAffiliationIds = "17266950 affiliate";
    private final String dxAffiliationIdKeys = "Utm_rid Utm_source";
    private final String dxRawUrl = "http://www.dx.com/p/fidget-dice-cubic-toy-for-focusing-stress-relieving-black-452239#.WE06HbLJxhF";

    @Before
    public void setUp() throws Exception {
        affiliationManager = new AffiliationManager();
        affiliationManager.setTradedoublerManager(new TradedoublerManager());
        LinkManager linkManager = new LinkManager();
        linkManager.setUrlParser(new UrlParser());
        affiliationManager.setLinkManager(linkManager);
    }

    @Test
    public void testRawLinkIsReturnedIfCompanyIsNotAffiliated() throws Exception {
        String parsedUrl = affiliationManager.constructAffiliatedLink(createNonAffliatedCompany(), rawUrl);
        assertTrue(rawUrl.equals(parsedUrl));
    }

    @Test
    public void testCustomUrlIsReceivedIfCompanyIsAffiliated() throws Exception {
        String parsedUrl = affiliationManager.constructAffiliatedLink(createAmazonAffiliationCompany(), rawUrl);
        Assert.assertFalse(rawUrl.equals(parsedUrl));
        Assert.assertTrue(parsedUrl.indexOf(rawUrl) >= 0);
    }

    @Test
    public void testCustomUrlIsReceivedWithMultipleKeysWhenCompanyIsAffiliated() throws Exception {
        String parsedUrl = affiliationManager.constructAffiliatedLink(createDXAffiliationCompany(), dxRawUrl);
        UrlParser urlParser = new UrlParser();
        Map<String, List<String>> urlParameters = urlParser.extractParameters(parsedUrl);
        String[] multipleAffiliateId = dxAffiliationIds.split(" ");
        String[] multipleAffiliateIdKeys = dxAffiliationIdKeys.split(" ");
        for (int i = 0; i < multipleAffiliateIdKeys.length; i++) {
            Assert.assertTrue(multipleAffiliateId[i].equals(
                    urlParameters.get(multipleAffiliateIdKeys[i]).get(0)));
        }
    }

    @Test
    public void testUpdatedUrlIsReceivedAlthoughUrlAlreadyContainsAnAffiliateId() throws Exception {
        String parsedUrl = affiliationManager.constructAffiliatedLink(createAmazonAffiliationCompany(), longUrlWithMultipleValues);
        Assert.assertFalse(longUrlWithMultipleValues.equals(parsedUrl));
        UrlParser urlParser = new UrlParser();
        Map<String, List<String>> urlParameters = urlParser.extractParameters(parsedUrl);
        Assert.assertTrue(affiliateId.equals(urlParameters.get("tag").get(0)));
    }

    protected OfferCompany createAmazonAffiliationCompany() {
        return CompanyBuilderManager.aRegularCompanyWithName("Amazon")
                .withAffiliateId(affiliateId)
                .withCompanyAffiliateType(CompanyAffiliateType.LINK)
                .withAffiliateIdKey(affiliateIdKey).build();
    }

    protected OfferCompany createDXAffiliationCompany() {
        return CompanyBuilderManager.aRegularCompanyWithName("Dealextreme")
                .withAffiliateId(dxAffiliationIds)
                .withCompanyAffiliateType(CompanyAffiliateType.LINK)
                .withAffiliateIdKey(dxAffiliationIdKeys).build();
    }

    private OfferCompany createNonAffliatedCompany() {
        return CompanyBuilderManager.aRegularCompanyWithName("Maxun")
                .withAffiliateId("")
                .withAffiliateIdKey("").build();
    }
}
