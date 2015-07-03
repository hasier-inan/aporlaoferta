package com.aporlaoferta.data;

import com.aporlaoferta.model.OfferCategory;
import com.aporlaoferta.model.OfferFilters;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 02/07/2015
 * Time: 16:00
 */
public class FilterBuilder {

    private OfferCategory category;
    private String text;
    private boolean expired;

    public static FilterBuilder aFilter() {
        return new FilterBuilder();
    }

    public FilterBuilder() {
        super();
    }

    public FilterBuilder withCategory(OfferCategory category) {
        this.category = category;
        return this;
    }

    public FilterBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public FilterBuilder isExpired(boolean expired) {
        this.expired = expired;
        return this;
    }

    public OfferFilters build() {
        OfferFilters offerFilters = new OfferFilters();
        offerFilters.setSelectedcategory(this.category.name());
        offerFilters.setText(this.text);
        offerFilters.setExpired(this.expired);
        return offerFilters;
    }
}
