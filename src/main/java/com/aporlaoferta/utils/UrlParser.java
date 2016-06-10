package com.aporlaoferta.utils;

import com.aporlaoferta.utils.http.HTTPEndpoint;
import com.aporlaoferta.utils.http.HTTPGetEndpoint;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created with IntelliJ IDEA.
 * User: HInan
 * Date: 17/07/15
 * Time: 16:07
 */
public class UrlParser {

    private static final int TIMEOUT = 5000;

    public Map extractParameters(String initialUrl) throws MalformedURLException, UnsupportedEncodingException {
        URL url = new URL(UrlUtils.parseUrl(initialUrl));
        return splitQuery(url);
    }

    private Map<String, List<String>> splitQuery(URL url) throws UnsupportedEncodingException {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<>();
        if (!isEmpty(url.getQuery())) {
            final String[] pairs = url.getQuery().split("&");
            for (String pair : pairs) {
                final int idx = pair.indexOf("=");
                final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
                if (!query_pairs.containsKey(key)) {
                    query_pairs.put(key, new LinkedList<String>());
                }
                final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
                query_pairs.get(key).add(value);
            }
        }
        return query_pairs;
    }

    public String extractHost(String link) throws MalformedURLException {
        URL url = new URL(UrlUtils.parseUrl(link));
        return url.getProtocol() + "://" + url.getHost() + url.getPath();
    }

    public String createLinkWithParameters(String initialUrl, String parameters) throws MalformedURLException {
        return extractHost(UrlUtils.parseUrl(initialUrl)) + parameters;
    }

    public int isAlive(String url) throws UnhealthyException {
        HTTPEndpoint httpEndpoint = new HTTPGetEndpoint(UrlUtils.parseUrl(url), TIMEOUT);
        if (!httpEndpoint.executeConnection()) {
            throw new UnhealthyException();
        }
        return httpEndpoint.getEndpointStatusCode();
    }
}
