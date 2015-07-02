package com.aporlaoferta.model;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created with IntelliJ IDEA.
 * User: HInan
 * Date: 01/07/15
 * Time: 17:56
 * Hitachi Capital (UK) PLC
 */
public class OfferFilters {

    private OfferCategory category;
    private String text;
    private boolean expired;

    public String getCategory() {
        return category != null ? category.name() : "";
    }

    public void setCategory(OfferCategory category) {
        this.category = category;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean containsFilter() {
        return (!isEmpty(getText())
                && (!isEmpty(getCategory())))
                || this.expired
                ;
    }
}
