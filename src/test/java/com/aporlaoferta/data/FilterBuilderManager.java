package com.aporlaoferta.data;

import com.aporlaoferta.model.OfferCategory;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 19/01/2015
 * Time: 17:58
 */
public class FilterBuilderManager {

    public static FilterBuilder anAllElectronicsFilterWithText(String text) {
        return FilterBuilder.aFilter()
                .withCategory(OfferCategory.ELECTRONICS)
                .withText(text)
                .isExpired(true)
                ;
    }
}
