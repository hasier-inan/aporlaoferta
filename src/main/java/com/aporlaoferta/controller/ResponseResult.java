package com.aporlaoferta.controller;

/**
 * Created with IntelliJ IDEA.
 * User: HInan
 * Date: 19/01/2015
 * Time: 07:52
 */
public enum ResponseResult {

    OK("OK"),
    VALIDATION_ERROR("Validation error"),
    INVALID_DATA_PROVIDED("Invalid data"),
    SYSTEM_ERROR("System error");

    private String code;

    private ResponseResult(String code) {
        this.code = code;
    }

    public String value() {
        return name();
    }

    public String getCode() {
        return code;
    }

}
