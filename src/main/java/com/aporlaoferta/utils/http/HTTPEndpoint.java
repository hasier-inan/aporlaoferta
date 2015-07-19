package com.aporlaoferta.utils.http;

/**
 * Created with IntelliJ IDEA.
 * User: hinan
 * Date: 19/07/15
 * Time: 19:34
 */
public interface HTTPEndpoint {
    //returns true if connection is healthy
    Boolean executeConnection();

    //get raw response (if it is not healthy error details should be provided)
    String getEndpointResponse();

    int getEndpointStatusCode();
}
