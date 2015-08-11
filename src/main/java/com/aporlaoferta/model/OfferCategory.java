package com.aporlaoferta.model;

/**
 * Created by hasiermetal on 15/01/15.
 */
public enum OfferCategory {
    ELECTRONICA("E"),
    GAMING("G"),
    AUDIOVISUALES("A"),
    HOGAR("H"),
    MODA("F"),
    VIAJES("T"),
    RESTAURANTES("R"),
    OTROS("O");
    /* Keep this code less than 3 chars. */
    private String code;

    private OfferCategory(String code) {
        this.code = code;
    }

    public String value() {
        return name();
    }

    public String getCode() {
        return code;
    }

    public static OfferCategory fromValue(String v) {
        return valueOf(v);
    }
}
