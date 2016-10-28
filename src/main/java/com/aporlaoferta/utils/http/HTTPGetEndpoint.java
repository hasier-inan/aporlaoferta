package com.aporlaoferta.utils.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: hinan
 * Date: 19/07/15
 * Time: 19:34
 */
@Component
public class HTTPGetEndpoint extends HTTPRequestEndpoint implements HTTPEndpoint {
    private final Logger LOG = LoggerFactory.getLogger(HTTPGetEndpoint.class);

    public HTTPGetEndpoint() {
    }

    public HTTPGetEndpoint(String url, int timeout) {
        super();
        setUrl(url);
        setConnectionTimeout(timeout);
    }

    @Override
    public String executeConnection() {
        try {
            URL url = new URL(this.getUrl());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setInstanceFollowRedirects(true);
            int statusCode = http.getResponseCode();

            String lastUrl = http.getURL().toString();
            setResponse(http);
            if (isHealthyConnection()) {
                return lastUrl;
            }
        } catch (IOException e) {
            this.LOG.error(e.getMessage());
        } catch (Exception e) {
            this.LOG.error(e.getMessage());
        }
        return null;
    }

}
