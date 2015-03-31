package com.aporlaoferta.controller;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

/**
 * Created by hasiermetal on 8/02/15.
 */
public class CsrfTokenBuilder {
    public static CsrfToken generateAToken(){
        HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
        return httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
    }
}
