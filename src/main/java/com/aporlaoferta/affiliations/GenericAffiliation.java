package com.aporlaoferta.affiliations;

/**
 * Created by hasiermetal on 22/01/15.
 */
public class GenericAffiliation extends DefaultAffiliation {

    public GenericAffiliation(String affiliateId, String ending, String preAffiliatePattern, String splitterPattern) {
        super();
        this.setAffiliateId(affiliateId);
        this.setEndingAdder(ending);
        this.setPreAffiliateId(preAffiliatePattern);
        this.setSplitterPatternToRight(splitterPattern);
    }

    public GenericAffiliation(String ending) {
        super();
        this.setEndingAdder(ending);
    }

}
