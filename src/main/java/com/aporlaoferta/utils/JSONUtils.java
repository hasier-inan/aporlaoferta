package com.aporlaoferta.utils;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 09/11/16
 * Time: 21:41
 */
public class JSONUtils {

    public final static String DOUBLE_QUOTE_PARSER = "xx#!!#xx";
    public final static String DOUBLE_QUOTE = "\\\\\"";
    public final static String QUOTE_PARSER = "xx#!#xx";
    public final static String QUOTE = "'";

    public static String escapeQuotes(String raw) {
        return raw.replaceAll(DOUBLE_QUOTE, DOUBLE_QUOTE_PARSER).replaceAll(QUOTE, QUOTE_PARSER);
    }
}
