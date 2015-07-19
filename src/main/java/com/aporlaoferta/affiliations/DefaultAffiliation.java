package com.aporlaoferta.affiliations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by hasiermetal on 22/01/15.
 */
public abstract class DefaultAffiliation implements AffiliationBase {

    private final Logger LOG = LoggerFactory.getLogger(DefaultAffiliation.class);

    private String splitterPatternToRight = "";
    private String endingAdder = "";

    private String preAffiliateId = "";
    private String affiliateId = "";

    @Override
    public String addAffiliationIdToLink(String link) {
        String resultingAffiliationIdLink;
        if (!isEmpty(splitterPatternToRight)) {
            String[] splittedLink = link.split(splitterPatternToRight);
            if (splittedLink.length > 2) {
                //there are more patterns, don't include any affiliation and check this issue
                LOG.error("Can't add any affiliation id because there are multiple patterns in {} for {}", link,
                        splitterPatternToRight);
                return link;
            }
            resultingAffiliationIdLink = splittedLink[0] + preAffiliateId + affiliateId + splittedLink[1] + endingAdder;
        } else if (!isEmpty(endingAdder)) {
            resultingAffiliationIdLink = link + endingAdder;
        } else {
            resultingAffiliationIdLink = link;
        }
        return resultingAffiliationIdLink;
    }

    public void setPreAffiliateId(String preAffiliateId) {
        this.preAffiliateId = preAffiliateId;
    }

    public void setAffiliateId(String affiliateId) {
        this.affiliateId = affiliateId;
    }

    public void setSplitterPatternToRight(String splitterPatternToRight) {
        this.splitterPatternToRight = splitterPatternToRight;
    }

    public void setEndingAdder(String endingAdder) {
        this.endingAdder = endingAdder;
    }
}
