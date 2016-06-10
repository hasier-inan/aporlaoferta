package com.aporlaoferta.utils;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 10/06/16
 * Time: 12:09
 */
public class UrlUtils {

    public static String parseUrl(String rawURL) {
        String protocol = "http://";
        String secureProtocol = "https://";
        if (rawURL.contains(protocol) || rawURL.contains(secureProtocol)) {
            return rawURL;
        }
        return protocol + rawURL;
    }
}
