package com.aporlaoferta.model;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 3/08/15
 * Time: 16:49
 */
public enum TheDefaultOffer {
    OFFER_IMAGE_URL("/resources/images/offer.png"),
    AVATAR_IMAGE_URL("/resources/images/avatar.png");
    private String code;

    private TheDefaultOffer(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
