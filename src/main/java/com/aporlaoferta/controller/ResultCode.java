package com.aporlaoferta.controller;

/**
 * Created with IntelliJ IDEA.
 * User: HInan
 * Date: 19/01/2015
 * Time: 07:51
 */
public enum ResultCode {

    ALL_OK(0, ResponseResult.OK, "All OK"),
    USER_NAME_IS_INVALID(1, ResponseResult.INVALID_DATA_PROVIDED, "Provided user nickname is incorrect"),
    USER_NAME_DOES_NOT_EXIST(2, ResponseResult.INVALID_DATA_PROVIDED, "Provided user nickname does not exist"),
    USER_NAME_ALREADY_EXISTS(3, ResponseResult.INVALID_DATA_PROVIDED, "Provided user nickname already exists"),
    UPDATE_USER_VALIDATION_ERROR(10, ResponseResult.VALIDATION_ERROR, "Validation process failed while updating user"),
    CREATE_USER_VALIDATION_ERROR(11, ResponseResult.VALIDATION_ERROR, "Validation process failed while creating user"),
    COMMENT_VALIDATION_ERROR(12, ResponseResult.VALIDATION_ERROR, "Validation process failed while creating comment"),
    QUOTE_VALIDATION_ERROR(13, ResponseResult.VALIDATION_ERROR, "Validation process failed while quoting comment"),
    INVALID_COMMENT_OWNER_ERROR(14, ResponseResult.INVALID_DATA_PROVIDED, "Provided comment is not valid for the user"),
    CREATE_COMPANY_VALIDATION_ERROR(14, ResponseResult.VALIDATION_ERROR,
            "Validation process failed while creating company"),
    CREATE_OFFER_VALIDATION_ERROR(15, ResponseResult.VALIDATION_ERROR,
            "Validation process failed while creating offer"),
    DATABASE_RETURNED_EMPTY_OBJECT(20, ResponseResult.SYSTEM_ERROR,
            "Database has not been updated , empty object returned");

    private int code;
    private String resultDescription;
    private ResponseResult result;
    private String messageKey;

    private ResultCode(int code, ResponseResult result, String errorField) {
        this.code = code;
        this.resultDescription = errorField;
        this.result = result;
    }

    private ResultCode(int code, ResponseResult result, String errorField, String messageKey) {
        this.code = code;
        this.resultDescription = errorField;
        this.result = result;
        this.messageKey = messageKey;
    }

    public int getCode() {
        return this.code;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public ResponseResult getResponseResult() {
        return result;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getCodeAndResultKey() {
        return getResponseResult().getCode() + " " + getCode();
    }
}
