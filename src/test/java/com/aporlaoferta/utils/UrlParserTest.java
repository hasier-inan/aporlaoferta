package com.aporlaoferta.utils;

import org.apache.commons.httpclient.HttpStatus;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 19/07/15
 * Time: 18:24
 */
public class UrlParserTest {

    private final String correctUrl = "www.amazon.es/gp/product/B00K690INE/?aka=2#";
    private final String correctEndpointUrl = "http://www.amazon.es/gp/product/B00K690INE/theEndpoint?aka=2";
    private final String nonExistingUrl = "http://amazon.ez/gp/product/B00K690INE/?aka=2";
    private final String incorrectEncodingUrl = "http://www.amazon.es/gp/product/B00K690INE/?aká~ñ=伊2";

    @Test
    public void testUrlParametersAreExtracted() throws Exception {
        UrlParser urlParser = new UrlParser();
        Map parameters = urlParser.extractParameters(this.correctUrl);
        notNull(parameters);
    }

    @Test
    public void testHostIsExtracted() throws Exception {
        UrlParser urlParser = new UrlParser();
        String host = urlParser.extractHost(this.correctUrl);
        Assert.assertTrue(!isEmpty(host));
    }

    @Test
    public void testHostIsExtractedWithoutParameters() throws Exception {
        UrlParser urlParser = new UrlParser();
        String host = urlParser.extractHost(this.correctEndpointUrl);
        Assert.assertTrue(host.equals("http://www.amazon.es/gp/product/B00K690INE/theEndpoint"));
    }

    @Ignore
    @Test(expected = UnsupportedEncodingException.class)
    public void testUnsupportedEncodingUrlThrowsExceptionWhileExtractingParameters() throws Exception {
        UrlParser urlParser = new UrlParser();
        urlParser.extractParameters(this.incorrectEncodingUrl);
    }

    @Test(expected = UnhealthyException.class)
    public void testUnhealthyExceptionIsThrownIfUrlIsNotAlive() throws Exception {
        UrlParser urlParser = new UrlParser();
        urlParser.isAlive(this.nonExistingUrl);
    }

    @Test
    public void testHealthyURLReturnsCorrectResponse() throws Exception {
        UrlParser urlParser = new UrlParser();
        Assert.assertThat("Expected a 200 code response", urlParser.isAlive(this.correctUrl), is(HttpStatus.SC_OK));
    }
}
