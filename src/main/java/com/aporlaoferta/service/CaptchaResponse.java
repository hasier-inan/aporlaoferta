package com.aporlaoferta.service;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 21/07/15
 * Time: 21:33
 */
public enum CaptchaResponse {
    SUCCESS("success", ""),
    SECRET("secret", "6LdqHQoTAAAAAFEZfUv4pNn4EgBlu_Nek3GFZ-xH"),
    RESPONSE("response", "");
    private String code;
    private String value;

    private CaptchaResponse(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
