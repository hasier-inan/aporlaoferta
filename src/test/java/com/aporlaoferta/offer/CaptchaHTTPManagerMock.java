package com.aporlaoferta.offer;

import com.aporlaoferta.service.CaptchaHTTPManager;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 21/07/15
 * Time: 22:52
 */
public class CaptchaHTTPManagerMock extends CaptchaHTTPManager {

    @Override
    public boolean validHuman(String reCaptcha) {
        return true;
    }
}
