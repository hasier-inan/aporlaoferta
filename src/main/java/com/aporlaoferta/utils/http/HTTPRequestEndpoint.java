package com.aporlaoferta.utils.http;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: hinan
 * Date: 19/07/15
 * Time: 19:34
 */
public class HTTPRequestEndpoint implements HTTPEndpoint {

    //final variables
    private final Logger LOG = LoggerFactory.getLogger(HTTPRequestEndpoint.class);
    private HttpResponse response;

    private String url;

    private int connectionTimeout;

    @Override
    public Boolean executeConnection() {
        return false;
    }

    @Override
    public String getEndpointResponse() {
        String resultPost = "";
        if (isHealthyConnection()) {
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(this.response.getEntity().getContent()));
                String inputLine;
                StringBuffer result = new StringBuffer();
                while ((inputLine = rd.readLine()) != null) {
                    result.append(inputLine);
                }
                resultPost = result.toString();
                rd.close();
            } catch (IOException ioe) {
                this.LOG.error("Unexpected error occured while reading response: " + ioe.getMessage());
            }
        } else {
            if (this.response != null) {
                int statusCode = this.response.getStatusLine().getStatusCode();
                resultPost = statusCode + ": " + HttpStatus.getStatusText(statusCode);
            }
        }
        return resultPost;
    }

    @Override
    public int getEndpointStatusCode() {
        if (this.response != null) {
            return this.response.getStatusLine().getStatusCode();
        }
        return HttpStatus.SC_NOT_FOUND;
    }

    protected Boolean isHealthyConnection() {
        return this.response != null && (this.response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);
    }

    ///Setter in case a custom post connection is launched
    protected void setUrl(String url) {
        this.url = url;
    }

    protected String getUrl() {
        return this.url;
    }

    ///Setter in case a custom post connection is launched
    protected void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    protected int getConnectionTimeout() {
        return connectionTimeout;
    }

    protected void setResponse(HttpResponse response) {
        this.response = response;
    }

    public HttpResponse getResponse() {
        return response;
    }
}
