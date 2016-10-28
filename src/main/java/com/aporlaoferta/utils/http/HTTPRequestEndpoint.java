package com.aporlaoferta.utils.http;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: hinan
 * Date: 19/07/15
 * Time: 19:34
 */
public class HTTPRequestEndpoint implements HTTPEndpoint {

    //final variables
    private final Logger LOG = LoggerFactory.getLogger(HTTPRequestEndpoint.class);
    private HttpURLConnection response;

    private String url;

    private int connectionTimeout;

    @Override
    public String executeConnection() {
        return null;
    }

    protected Boolean isHealthyConnection() throws IOException {
        return this.response != null && (this.response.getResponseCode() < HttpStatus.SC_MULTIPLE_CHOICES);
    }

    ///Setter in case a custom post connection is launched
    protected void setUrl(String url) {
        this.url = url;
    }

    protected String getUrl() {
        String protocol = "http://";
        String secureProtocol = "https://";
        if (this.url.contains(protocol) || this.url.contains(secureProtocol)) {
            return this.url;
        }
        return protocol + this.url;
    }

    ///Setter in case a custom post connection is launched
    protected void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    protected int getConnectionTimeout() {
        return connectionTimeout;
    }

    protected void setResponse(HttpURLConnection response) {
        this.response = response;
    }

    public HttpURLConnection getResponse() {
        return response;
    }
}
