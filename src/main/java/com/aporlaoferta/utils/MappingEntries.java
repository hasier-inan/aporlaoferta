package com.aporlaoferta.utils;

/**
 * Created by hasiermetal on 15/01/15.
 */
public enum MappingEntries {
    COMPANY_NAME("companyName"),
    COMPANY_URL("companyUrl"),
    COMPANY_LOGO("companyLogoUrl"),
    COMPANY_AFFILIATE_ID("companyAffiliateId"),
    URL("url"),
    OFFER("commentsOffer"),
    OFFER_ID("offerId"),
    OFFER_COMMENT("offerComment"),
    QUOTE("commentsQuotedComment"),
    TEXT("commentText"),
    CATEGORY("category"),
    COMPANY("company"),
    DESCRIPTION("description"),
    IMAGE("image"),
    LINK("link"),
    TITLE("title"),
    NICKNAME("userNickname"),
    PASSWORD("userPassword"),
    EMAIL("userEmail"),
    FINAL_PRICE("final_price"),
    ORIGINAL_PRICE("original_price"),
    AVATAR("userAvatar"),;
    /* Keep this code less than 3 chars. */
    private String code;

    private MappingEntries(String code) {
        this.code = code;
    }

    public String value() {
        return name();
    }

    public String getCode() {
        return code;
    }

    public static MappingEntries fromValue(String v) {
        return valueOf(v);
    }
}
