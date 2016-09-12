package com.aporlaoferta.model;

/**
 * Created by hasiermetal on 15/01/15.
 */
public enum OfferCategory {
    ELECTRONICA("E", "Electrónica"),
    VIDEOJUEGOS("V", "Videojuegos"),
    CINE_TV_MUSICA("C", "Cine, TV y Música"),
    INFORMATICA("I", "Informática"),
    HOGAR("H", "Hogar y Bricolaje"),
    JUGUETES("J","Juguetes"),
    MODA("MO", "Moda y Complementos"),
    DEPORTES_AIRE_LIBRE("D", "Deportes y Aire Libre"),
    MOTOR("M", "Motor"),
    ALIMENTACION("A", "Supermercados y Alimentación"),
    VIAJES("VI", "Viajes y Ocio"),
    RESTAURANTES("R", "Restaurantes"),
    OTROS("O", "Otros");

    /* Keep this code less than 3 chars. */
    private String code;
    private String text;

    private OfferCategory(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String value() {
        return name();
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static OfferCategory fromValue(String v) {
        return valueOf(v);
    }
}
