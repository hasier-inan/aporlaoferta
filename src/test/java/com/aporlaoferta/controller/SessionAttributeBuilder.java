package com.aporlaoferta.controller;

import org.springframework.security.web.csrf.CsrfToken;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasiermetal on 8/02/15.
 */
public class SessionAttributeBuilder {

    public static Map getSessionAttributeWithHttpSessionCsrfTokenRepository(CsrfToken csrfToken){
        Map map = new HashMap();
        map.put("org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN",
                csrfToken);
        return map;
    }
}
