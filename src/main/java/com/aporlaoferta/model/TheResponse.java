package com.aporlaoferta.model;

import com.aporlaoferta.controller.ResponseResult;
import com.aporlaoferta.controller.ResultCode;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 11/02/2015
 * Time: 18:23
 */
public class TheResponse {

    private int code;
    private ResponseResult responseResult;
    private String description;
    private String descriptionEsp;
    private String other;

    public void assignResultCode(ResultCode resultCode) {
        setCode(resultCode.getCode());
        setResponseResult(resultCode.getResponseResult());
        setDescription(resultCode.getResultDescription());
        setDescriptionEsp(resultCode.getResultDescriptionEsp());
    }

    public void assignResultCode(ResultCode resultCode, String description, String descriptionEsp) {
        setCode(resultCode.getCode());
        setResponseResult(resultCode.getResponseResult());
        setDescription(description);
        setDescriptionEsp(descriptionEsp);
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ResponseResult getResponseResult() {
        return responseResult;
    }

    public void setResponseResult(ResponseResult responseResult) {
        this.responseResult = responseResult;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionEsp() {
        return descriptionEsp;
    }

    public void setDescriptionEsp(String descriptionEsp) {
        this.descriptionEsp = descriptionEsp;
    }
}
