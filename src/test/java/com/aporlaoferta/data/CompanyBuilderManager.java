package com.aporlaoferta.data;

/**
 * Created with IntelliJ IDEA.
 * User: HInan
 * Date: 19/01/2015
 * Time: 17:58
 */
public class CompanyBuilderManager {

    public static CompanyBuilder aRegularCompanyWithName(String name) {
        return CompanyBuilder.aCompany()
                .withName(name)
                .withAffiliateId("12121")
                .withLogo("companyLogo.kjp")
                .withUrl("http://kakaka.kakak")
                ;
    }

    public static CompanyBuilder aRegularCompanyWithId(Long id) {
        return CompanyBuilder.aCompany()
                .withId(id)
                .withName("dafuqincpmny")
                .withAffiliateId("12121")
                .withLogo("companyLogo.kjp")
                .withUrl("http://kakaka.kakak")
                ;
    }
}
