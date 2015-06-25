package com.aporlaoferta.controller;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 19/01/2015
 * Time: 07:51
 */
public enum ResultCode {

    ALL_OK(0, ResponseResult.OK, "All OK", "OK"),
    USER_NAME_IS_INVALID(1, ResponseResult.INVALID_DATA_PROVIDED, "Provided user nickname is incorrect", "El nombre de usuario facilitado es inválido"),
    USER_NAME_DOES_NOT_EXIST(2, ResponseResult.INVALID_DATA_PROVIDED, "Provided user nickname does not exist", "El nombre de usuario no existe"),
    USER_NAME_ALREADY_EXISTS(3, ResponseResult.INVALID_DATA_PROVIDED, "Provided user nickname already exists", "El nombre de usuario ya existe"),
    UPDATE_USER_VALIDATION_ERROR(10, ResponseResult.VALIDATION_ERROR, "Validation process failed while updating user", "El proceso de validación ha fallado mientras se procedía a actualizar el usuario"),
    CREATE_USER_VALIDATION_ERROR(11, ResponseResult.VALIDATION_ERROR, "Validation process failed while creating user", "El proceso de validación ha fallado mientras se procedía a crear el usuario"),
    COMMENT_VALIDATION_ERROR(12, ResponseResult.VALIDATION_ERROR, "Validation process failed while creating comment", "El proceso de validación ha fallado mientras se procedía a comentar la oferta"),
    QUOTE_VALIDATION_ERROR(13, ResponseResult.VALIDATION_ERROR, "Validation process failed while quoting comment", "El proceso de validación ha fallado mientras se procedía a comentar el comentario"),
    CREATE_COMPANY_VALIDATION_ERROR(14, ResponseResult.VALIDATION_ERROR,
            "Validation process failed while creating company", "El proceso de validación ha fallado mientras se procedía a crear la compañía"),
    CREATE_OFFER_VALIDATION_ERROR(15, ResponseResult.VALIDATION_ERROR,
            "Validation process failed while creating offer", "El proceso de validación ha fallado mientras se procedía a crear la oferta"),
    UPDATE_OFFER_VALIDATION_ERROR(16, ResponseResult.VALIDATION_ERROR,
            "Validation process failed while updating offer", "El proceso de validación ha fallado mientras se procedía a actualizar la oferta"),
    UPDATE_COMMENT_VALIDATION_ERROR(17, ResponseResult.VALIDATION_ERROR,
            "Validation process failed while updating comment", "El proceso de validación ha fallado mientras se procedía a comentar la oferta"),
    DATABASE_RETURNED_EMPTY_OBJECT(20, ResponseResult.SYSTEM_ERROR,
            "Database has not been updated , empty object returned", ""),
    INVALID_OWNER_ERROR(30, ResponseResult.INVALID_DATA_PROVIDED, "Invalid owner found", "Usuario inválido"),
    FEEDBACK_VALIDATION_ERROR(41, ResponseResult.VALIDATION_ERROR,
            "Validation process failed while adding feedback", "El proceso de validación ha fallado mientras se procedía a valorar la oferta"),
    FEEDBACK_ALREADY_PERFORMED_ERROR(42, ResponseResult.VALIDATION_ERROR,
            "Feedback process was already performed by user and given offer", "Esta oferta ya ha sido valorada previamente"),
    FEEDBACK_FROM_OWNER_PERFORMED_ERROR(43, ResponseResult.VALIDATION_ERROR,
            "Feedback process can't be performed by offer owner", "El creador de la oferta no puede valorarse a sí mismo"),
    IMAGE_UPLOAD_ERROR(56, ResponseResult.INVALID_DATA_PROVIDED,
            "Could not perform image upload process", "No se ha podido cargar la imagen");

    private int code;
    private String resultDescription;
    private String resultDescriptinEsp;
    private ResponseResult result;
    private String messageKey;

    private ResultCode(int code, ResponseResult result, String errorField, String errorFieldEsp) {
        this.code = code;
        this.resultDescription = errorField;
        this.result = result;
        this.resultDescriptinEsp = errorFieldEsp;
    }

    public int getCode() {
        return this.code;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public String getResultDescriptionEsp() {
        return resultDescriptinEsp;
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
