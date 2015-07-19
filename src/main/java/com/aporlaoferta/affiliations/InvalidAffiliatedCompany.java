package com.aporlaoferta.affiliations;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 19/07/15
 * Time: 17:24
 */
public class InvalidAffiliatedCompany extends Exception {
    public InvalidAffiliatedCompany(String offerCompanyName) {
        super(String.format("Invalid offer company affiliation: %s", offerCompanyName));
    }
}
