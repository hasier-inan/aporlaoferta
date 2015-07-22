package com.aporlaoferta.utils.http;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
    public Boolean executeConnection() {
        //initialize client
        try {
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, getConnectionTimeout());
            HttpClient client = new DefaultHttpClient(httpParams);
            setResponse(client.execute(new HttpGet(this.getUrl())));
            return isHealthyConnection();
        } catch (IOException e) {
            this.LOG.error(e.getMessage());
        } catch (Exception e) {
            this.LOG.error(e.getMessage());
        }
        return false;
    }

}
