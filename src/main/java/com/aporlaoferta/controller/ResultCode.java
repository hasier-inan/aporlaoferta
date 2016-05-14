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
    USER_NAME_PASSWORD_INVALID(4, ResponseResult.INVALID_DATA_PROVIDED, "Provided user password is invalid", "La contraseña facilitada es inválida"),
    INVALID_PASSWORD_VERIFICATION(5, ResponseResult.INVALID_DATA_PROVIDED, "Provided user data is invalid", "Los datos de usuario facilitados son inválidos"),
    USER_EMAIL_ALREADY_EXISTS(6, ResponseResult.INVALID_DATA_PROVIDED, "Provided user email already exists", "Actualmente ya existe un usuario vinculado a ese correo electrónico"),
    USER_EMAIL_IS_INVALID(7, ResponseResult.INVALID_DATA_PROVIDED, "Provided user email is incorrect", "El correo electrónico facilitado es inválido"),
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
            "Could not perform image upload process", "No se ha podido cargar la imagen"),
    IMAGE_TOO_HEAVY_ERROR(57, ResponseResult.INVALID_DATA_PROVIDED,
            "Image is too heavy", "La imagen es demasido pesada"),
    DEFAULT_ERROR(66, ResponseResult.INVALID_DATA_PROVIDED,
            "Could not perform operation", "No se ha podido ejecutar la operación"),
    UNHEALTHY_ENDPOINT(70, ResponseResult.INVALID_DATA_PROVIDED,
            "Provided url is not healthy", "La dirección url de la oferta no es correcta"),
    INVALID_CAPTCHA(75, ResponseResult.INVALID_DATA_PROVIDED, "Invalid Captcha", "Captcha inválido"),
    INVALID_CONFIRMATION_ID(78, ResponseResult.INVALID_DATA_PROVIDED, "Invalid confirmation id", "Id de confirmación inválido");

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
