package com.aporlaoferta.model;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created with IntelliJ IDEA.
 * User: HInan
 * Date: 01/07/15
 * Time: 17:56
 */
public class OfferFilters {

    private OfferCategory selectedcategory;
    private String text;
    private boolean expired;
    private boolean hot;

    public String getSelectedcategory() {
        return selectedcategory != null ? selectedcategory.name() : "";
    }

    public void setSelectedcategory(String category) {
        if (!isEmpty(category)) {
            this.selectedcategory = OfferCategory.fromValue(category);
        }
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

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public boolean containsCategoryOnlyFilter() {
        return (isEmpty(getText())
                && (!isEmpty(getSelectedcategory())));
    }

    public boolean containsTextOnlyFilter() {
        return (!isEmpty(getText())
                && (isEmpty(getSelectedcategory())));
    }

    public boolean containsExpiredOnlyFilter() {
        return (isEmpty(getText())
                && (isEmpty(getSelectedcategory())))
                && this.expired;
    }

    public boolean containsFilter() {
        return (!isEmpty(getText())
                && (!isEmpty(getSelectedcategory())));
    }
}
