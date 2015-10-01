<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page session="true" %>
<label for="loginForm" class="overhead-hint-label" ng-hide="isPasswordForgotten">Identif&iacute;cate con tus datos
    personales</label>
<label for="loginForm" class="overhead-hint-label" ng-show="isPasswordForgotten">Cu&iacute;l es tu nombre de
    usuario?</label>

<form id="loginForm" name='loginForm'
      action="<c:url value='/j_spring_security_check' />" method='POST' class="centeredForm">
    <input ng-model="login.username" type="text" id="loginUsername" name="username"
           class="overheadField content-validation"
           placeholder="Usuario" ng-maxlength="250" required>
    <input ng-model="login.p455w012d" type="password" id="loginPassword" name="password"
           class="overheadField content-validation"
           placeholder="Contrase&ntilde;a" ng-maxlength="250" ng-required="!isPasswordForgotten" ng-hide="isPasswordForgotten"/>
    <input type="hidden" name="${_csrf.parameterName}"
           value="${_csrf.token}"/>

    <div class="centeredForm" ng-hide="isPasswordForgotten">
        <button ng-disabled="!loginForm.$valid" class="button small icon fa-sign-in overheadButton" name="loginSubmit"
                ng-click="loginForm.submit()">
            Identificarse
        </button>
        <button ng-disabled="!loginForm.$valid" class="button mini icon fa-sign-in overheadButton"
                ng-click="loginForm.submit()"></button>
    </div>

</form>

<form id="forgottenPasswordForm" name="forgottenPasswordForm" class="centeredForm">
    <div class="centeredForm" ng-show="isPasswordForgotten">
        <button ng-disabled="!loginForm.$valid" class="button small icon fa-sign-in overheadButton blue"
                name="forgottenPasswordSubmit" ng-click="requestPassword(login.username)">
            Solicitar nueva contrase&ntilde;a
        </button>
        <button ng-disabled="!loginForm.$valid" class="button mini icon fa-sign-in overheadButton blue"
                ng-click="requestPassword(login.username)"></button>
    </div>

    <input type="checkbox" id="forgottenPasswordRequest"
           name="forgottenPasswordRequest" ng-model="isPasswordForgotten"/>
    <label id="forgottePasswordRequestLabel" for="forgottenPasswordRequest" class="overheadLabel">He olvidado la contrase&ntilde;a</label>
</form>