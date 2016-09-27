<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page session="true" %>
<label for="loginForm" class="overhead-hint-label" ng-hide="isPasswordForgotten">Identifícate con tus datos
    personales</label>
<label for="loginForm" class="overhead-hint-label" ng-show="isPasswordForgotten">¿Cuál es tu correo electrónico?</label>

<form id="loginForm" name='loginForm'
      action="<c:url value='/process_login' />" method='POST' class="centeredForm">
    <input ng-model="login.email" type="text" id="loginEmail" name="email"
           class="overheadField content-validation"
           placeholder="Correo electrónico" ng-maxlength="250"
           ng-pattern="validMail" ng-show="isPasswordForgotten" ng-required="isPasswordForgotten">
    <input ng-model="login.username" type="text" id="loginUsername" name="username"
           class="overheadField content-validation"
           placeholder="Usuario" ng-maxlength="250" required
           ng-hide="isPasswordForgotten" ng-required="!isPasswordForgotten">
    <input ng-model="login.p455w012d" type="password" id="loginPassword" name="password"
           class="overheadField content-validation"
           placeholder="Contraseña" ng-maxlength="250" ng-required="!isPasswordForgotten"
           ng-hide="isPasswordForgotten"/>
    <input type="hidden" name="${_csrf.parameterName}"
           value="${_csrf.token}"/>

    <div class="centeredForm" ng-hide="isPasswordForgotten">
        <div class="remember-me-option">
            <input type="checkbox" id="remember-login-request"
                   name="remember-me" ng-model="isRememberNeeded"/>
            <label id="rememberLoginRequestLabel" for="remember-login-request" class="overheadLabel">Mantenerme
                identificado</label>
        </div>
        <button ng-disabled="!loginForm.$valid || processing" class="button small icon fa-sign-in overheadButton"
                name="loginSubmit"
                ng-click="processLogin()">
            Identificarse
        </button>
        <button ng-disabled="!loginForm.$valid || processing" class="button mini icon fa-sign-in overheadButton"
                ng-click="processLogin()"></button>
        <div class="loading-dialogue ">
            <i class="fa fa-refresh loadingdialogue white popup-loading" ng-class="!processing?'hideLoading':''"></i>
        </div>
    </div>

</form>

<form id="forgottenPasswordForm" name="forgottenPasswordForm" class="password-forgotten-option centeredForm">
    <div class="centeredForm" ng-show="isPasswordForgotten">
        <button ng-disabled="!loginForm.$valid || processing" class="button small icon fa-sign-in overheadButton blue"
                name="forgottenPasswordSubmit" ng-click="requestPassword(login.email)">
            Solicitar nueva contraseña
        </button>
        <button ng-disabled="!loginForm.$valid || processing" class="button mini icon fa-sign-in overheadButton blue"
                ng-click="requestPassword(login.email)"></button>
        <div class="loading-dialogue ">
            <i class="fa fa-refresh loadingdialogue white popup-loading" ng-class="!processing?'hideLoading':''"></i>
        </div>
    </div>

    <hr/>

    <input type="checkbox" id="forgottenPasswordRequest"
           name="forgottenPasswordRequest" ng-model="isPasswordForgotten"/>
    <label id="forgottePasswordRequestLabel" for="forgottenPasswordRequest" class="overheadLabel">He olvidado la
        contraseña</label>
</form>