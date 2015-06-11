package com.aporlaoferta.data;

import com.aporlaoferta.model.OfferCategory;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 19/01/2015
 * Time: 17:58
 */
public class OfferBuilderManager {

    public static OfferBuilder aBasicOfferWithId(Long id) {
        return OfferBuilder.anOffer()
                .withId(id)
                .withCategory(OfferCategory.ELECTRONICS)
                .createdOn(new Date())
                .isExpired(false)
                .withCompany(CompanyBuilderManager.aRegularCompanyWithName("tututu").build())
                .withImage("product.image.url.jpg")
                .withDescription("the amazing product we have here this is a ducking deal")
                .withLink("this.is.the.direct.url.with.affiliation.id-i.hope")
                .withNegatives(3L)
                .withPositives(121L)
                .withFinalPrice(new BigDecimal(200))
                .withOriginalPrice(new BigDecimal(300))
                .withTitle("yellow duck with an amazing price!")
                .withUser(UserBuilderManager.aRegularUserWithNickname("duckU").build());
    }

    public static OfferBuilder aBasicOfferWithoutId() {
        return OfferBuilder.anOffer()
                .withCategory(OfferCategory.ELECTRONICS)
                .createdOn(new Date())
                .isExpired(false)
                .withCompany(CompanyBuilderManager.aRegularCompanyWithName("tututu").build())
                .withImage("product.image.url.jpg")
                .withDescription("the amazing product we have here this is a ducking deal")
                .withLink("this.is.the.direct.url.with.affiliation.id-i.hope")
                .withFinalPrice(new BigDecimal(200))
                .withOriginalPrice(new BigDecimal(300))
                .withTitle("yellow duck with an amazing price!")
                .withUser(UserBuilderManager.aRegularUserWithNickname("duckU").build());
    }

    public static OfferBuilder aBasicOfferWithoutUserOrId(){
        return OfferBuilder.anOffer()
                .withCategory(OfferCategory.ELECTRONICS)
                .createdOn(new Date())
                .isExpired(false)
                .withCompany(CompanyBuilderManager.aRegularCompanyWithName("tututu").build())
                .withImage("product.image.url.jpg")
                .withDescription("the amazing product we have here this is a ducking deal")
                .withLink("this.is.the.direct.url.with.affiliation.id-i.hope")
                .withNegatives(3L)
                .withPositives(121L)
                .withFinalPrice(new BigDecimal(200))
                .withOriginalPrice(new BigDecimal(300))
                .withTitle("yellow duck with an amazing price!")
                ;
    }
}
