package com.aporlaoferta.service;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 21/07/15
 * Time: 22:09
 */
public interface RequestGateway {
    String echo(Map<String, String> request);
}
