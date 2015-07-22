package com.aporlaoferta.offer;

import com.aporlaoferta.service.CaptchaHTTPManager;
import com.aporlaoferta.service.CaptchaResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 4/07/15
 * Time: 18:29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:aporlaoferta-http-service-test-context.xml"})
public class CaptchaHTTPManagerTestIntegration {

    private final String possibleKey = "03AHJ_VutBKvCPJz6-O5i_aHuEqH620zcd9MKZZH3gYbC8XLnW8cukDRcr9oR" +
            "sWCk3Bte_mvUO5uD5JWwDMIAXeTuEdT4BjBRIpdmYSd1m_0qcGHaLRyh8E2_rYzrpkvy0_yXU53jfMJ5RLxHUj9_iw4u" +
            "pfW-qdiocanQENwBAyjErzkYZr9V8kht-dso4snahIcAmnJWH0ZoPH6wu74kgemFkwPnVFGRXrq0e3NJ6_VcbDnxx" +
            "eNcZu_4_Zs8A15W4qYf4VFhZFwzCwCr6ks9ab0ulHIOcL23GjIQq0UZDPiOZQW88q01A9werA0RMGirkZNHdHNiJo8k4" +
            "F4wwX7L7DwjuPSQhaOWMVfVuC4botEIULmurVp4bzet2tF8ToTLZ76laFwl29ELMzCpoc5PtH6nQFsauuTzx48_3T7W4bl" +
            "0jS9mVblz8yWU";

    @Autowired
    CaptchaHTTPManager captchaHTTPManager;

    @Ignore
    @Test
    public void testSuccessCodeIsReceivedFromEndpoint() throws Exception {
        String content = this.captchaHTTPManager.checkContent(possibleKey);
        Map parsedContents = getMapFromResponse(content);
        Assert.assertTrue("Expected true response from endpoint", (boolean) parsedContents.get(CaptchaResponse.SUCCESS.getCode()));
    }

    @Test
    public void testJsonContentIsReceivedFromCaptchaEndpoint() throws Exception {
        String content = this.captchaHTTPManager.checkContent("");
        assertTrue("Expected received content not to be empty", !isEmpty(content));
    }

    @Test
    public void testReceivedContentIsJsonContent() throws Exception {
        String content = this.captchaHTTPManager.checkContent("");
        Map parsedContents = getMapFromResponse(content);
        assertNotNull("Expected a json content response from endpoint", parsedContents);
        assertNotNull("Expected a success code in response", parsedContents.get(CaptchaResponse.SUCCESS.getCode()));
    }

    private Map getMapFromResponse(String content) throws java.io.IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(content, Map.class);
    }
}