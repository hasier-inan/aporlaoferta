package com.aporlaoferta.affiliations;

import java.util.EnumSet;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 18/12/2016
 * Time: 12:35
 */
public enum TradedoublerPostfix {
    DEFAULT("default_tradedoubler_company", "url(%s)"),
    LASTMINUTE("Lastminute", "url(%s?)");

    private String name;
    private String text;

    private TradedoublerPostfix(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String value() {
        return name();
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public static TradedoublerPostfix fromValue(String companyName) {
        for (TradedoublerPostfix tradedoublerPostfix : EnumSet.allOf(TradedoublerPostfix.class)) {
            if (tradedoublerPostfix.getName().equalsIgnoreCase(companyName)) {
                return tradedoublerPostfix;
            }
        }
        return DEFAULT;
    }

}
