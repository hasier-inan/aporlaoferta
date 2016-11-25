<%@taglib prefix="sec"
          uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page session="true" %>
<div id="accountCreationContainer">

    <sec:authorize access="isRememberMe()">
        <div class="remember-me-security">
            <div>
                Por razones de seguridad tendrás que identificarte de nuevo para modificar tu perfil.
            </div>

            <button class="button small small--rounded hvr-icon-grow icon fa-sign-in whiteButton green-background"
                    ng-click="processLogin()"
                    tabindex="0">
                Identifícate
            </button>

            <button class="button mini icon fa-sign-in whiteButton green-background account-management fixed"
                    ng-click="processLogin()" tabindex="0"></button>
        </div>
    </sec:authorize>

    <sec:authorize access="isFullyAuthenticated()||isAnonymous()">
        <div>
            <form name="accountCreationContainer" class="centeredForm">
                <ul>
                    <label for="createUserNickname" class="overhead-hint-label" ng-hide="disableNickname">Elige un
                        nombre de
                        usuario</label>
                    <input type="text" id="createUserNickname" ng-model="theUser.userNickname"
                           class="overheadField content-validation"
                           placeholder="usuario" ng-disabled="disableNickname" required ng-maxlength="250"/>
                </ul>
                <ul ng-show="disableNickname">
                    <div class="inline-block">
                        <input type="checkbox" id="updatePasswordIsRequired" name="updatePasswordIsRequired"
                               ng-model="passwordUpdateRequired" ng-click="resetPassword()">
                        <label id="updatePasswordIsRequiredLabel" for="updatePasswordIsRequired" class="overheadLabel">Actualizar
                            Contraseña</label>
                    </div>
                </ul>
                <ul ng-show="disableNickname && passwordUpdateRequired">
                    <label for="oldUserPassword" class="overhead-hint-label">
                        Añade tu antigua y nueva contraseña</label>
                    <input type="password" id="oldUserPassword" ng-model="theUser.oldPassword"
                           class="overheadField content-validation"
                           placeholder="Contraseña antigua" ng-required="disableNickname && passwordUpdateRequired"
                           ng-pattern="validPassword" ng-maxlength="250"/>
                </ul>
                <ul ng-show="passwordUpdateRequired">
                    <label for="createUserPassword" class="overhead-hint-label"
                           ng-hide="disableNickname && passwordUpdateRequired">
                        La contraseña debe tener un mínimo de 8 carácteres, compuesto por lo menos por un número
                        y una letra</label>
                    <label for="createUserPassword" class="overhead-hint-label"
                           ng-show="disableNickname && passwordUpdateRequired">
                        Recuerda que la contraseña debe tener un mínimo de 8 carácteres, compuesto por lo
                        menos por un número y una letra</label>
                    <input type="password" id="createUserPassword" ng-model="theUser.userSpecifiedPassword"
                           ng-pattern="validPassword"
                           class="overheadField content-validation"
                           placeholder="Contraseña" ng-maxlength="250"
                           ng-required="passwordUpdateRequired"/>
                    <input type="password" id="createUserPassword2" ng-model="userPassword2" ng-pattern="validPassword"
                           class="overheadField content-validation"
                           placeholder="Repite la Contraseña" ng-maxlength="250"
                           pw-check="createUserPassword"
                           ng-required="passwordUpdateRequired"/>
                </ul>
                <ul ng-show="disableNickname">
                    <div class="inline-block margin-top--small margin-bottom--small">
                        <input type="checkbox" id="updateEmailIsRequired" name="updateEmailIsRequired"
                               ng-model="updateEmailRequired" ng-click="resetEmail()">
                        <label for="updateEmailIsRequired" class="overheadLabel">Actualizar
                            Correo Electrónico</label>
                    </div>
                </ul>
                <ul>
                    <label for="createUserEmail" class="overhead-hint-label" ng-hide="disableNickname">
                        Recibirás un correo electrónico para confirmar tu cuenta</label>
                    <label for="createUserEmail" class="overhead-hint-label" ng-show="disableNickname && updateEmailRequired">
                        Puedes cambiar tu correo electrónico y avatar</label>
                    <input type="text" id="createUserEmail" ng-model="theUser.userSpecifiedEmail"
                           ng-show="!disableNickname || updateEmailRequired"
                           ng-required="!disableNickname || updateEmailRequired"
                           class="overheadField content-validation"
                           placeholder="Email" ng-pattern="validMail" ng-maxlength="250"/>
                </ul>
                <ul>
                    <label id="imageUploadLabel" for="imageUploadContent" class="overhead-hint-label"
                           ng-hide="disableNickname">Puedes añadir opcionalmente una imagen de perfil</label>

                    <div id="imageUploadContent" ng-image-uploader="createAccountImageUploader"
                         already-uploaded-image="theUser.userAvatar"
                         final-url="theUser.userAvatar"></div>
                    <input type="hidden" id="createUserAvatar" ng-model="theUser.userAvatar" class="overheadField"/>
                </ul>

                <ul>
                    <label for="createAccountCaptcha" class="overhead-hint-label">Selecciona la casilla para probar que
                        no
                        eres
                        un robot</label>

                </ul>
                <ul class="regular-bottom">
                    <li>
                        <div id="createAccountCaptcha" on-create="setWidgetId(widgetId)" vc-recaptcha key="publicKey"
                             class="offerCaptcha"></div>
                    </li>
                </ul>
            </form>
        </div>

        <button ng-disabled="!accountCreationContainer.$valid || processing"
                class="button small icon fa-user overheadButton"
                ng-click="createAccount(theUser)" class="overheadButton" ng-hide="disableNickname">
            Crear perfil
        </button>
        <button ng-disabled="!accountCreationContainer.$valid || processing"
                class="button small icon fa-user overheadButton"
                ng-click="createAccount(theUser)" class="overheadButton" ng-show="disableNickname">
            Actualizar perfil
        </button>
        <button ng-disabled="!accountCreationContainer.$valid || processing"
                class="button mini icon fa-user overheadButton"
                ng-click="createAccount(theUser)"></button>
        <div class="loading-dialogue ">
            <i class="fa fa-refresh loadingdialogue white popup-loading" ng-class="!processing?'hideLoading':''"></i>
        </div>
    </sec:authorize>
</div>

