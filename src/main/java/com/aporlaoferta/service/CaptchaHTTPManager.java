package com.aporlaoferta.service;

import com.aporlaoferta.controller.ResultCode;
import com.aporlaoferta.model.TheResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 4/07/15
 * Time: 17:58
 */

public class CaptchaHTTPManager {
    private final Logger LOG = LoggerFactory.getLogger(CaptchaHTTPManager.class);

    private RequestGateway requestGateway;

    public boolean validHuman(String reCaptcha) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Boolean> captchaMap = mapper.readValue(checkContent(reCaptcha), Map.class);
            return captchaMap.get(CaptchaResponse.SUCCESS.getCode());
        } catch (IOException e) {
            LOG.error("Could not convert captcha map ", e);
        }
        return false;
    }

    public String checkContent(String key) {
        Map payload = constructPayloadMap(key);
        return this.requestGateway.echo(payload);
    }

    private Map<String, String> constructPayloadMap(String recaptchaResponse) {
        Map<String, String> payload = new HashMap<>();
        payload.put(CaptchaResponse.SECRET.getCode(), CaptchaResponse.SECRET.getValue());
        payload.put(CaptchaResponse.RESPONSE.getCode(), recaptchaResponse);
        return payload;
    }

    public void setRequestGateway(RequestGateway requestGateway) {
        this.requestGateway = requestGateway;
    }
}
