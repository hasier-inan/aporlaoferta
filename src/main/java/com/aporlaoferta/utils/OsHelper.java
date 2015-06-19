package com.aporlaoferta.utils;

/**
 * Created with IntelliJ IDEA.
 * User: hinan
 * Date: 12/05/15
 * Time: 13:05
 */
public class OsHelper {
    public static String osSeparator() {
        return System.getProperty("os.name").contains("indow") ? "\\" : "/";
    }
}
