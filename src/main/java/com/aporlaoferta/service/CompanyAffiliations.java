package com.aporlaoferta.service;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 19/07/15
 * Time: 17:04
 */
public enum CompanyAffiliations {

    AMAZON("Amazon");

    private String code;

    private CompanyAffiliations(String code) {
        this.code = code;
    }

    public static boolean isCompanyAffiliated(String company) {
        return fromCode(company) != null;
    }

    public static CompanyAffiliations fromCode(String company) {
        for (CompanyAffiliations companyAffiliations : CompanyAffiliations.values()) {
            if (companyAffiliations.getCode().equals(company)) {
                return companyAffiliations;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }
}
