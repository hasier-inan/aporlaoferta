<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page session="true" %>
<form name='loginForm'
      action="<c:url value='/j_spring_security_check' />" method='POST' class="centeredForm">
    <input ng-model="login.username" type="text" id="loginUsername" name="username" class="overheadField content-validation"
           placeholder="Usuario" ng-maxlength="250" required>
    <input ng-model="login.p455w012d" type="password" id="loginPassword" name="password" class="overheadField content-validation"
           placeholder="Contrase&ntilde;a" ng-maxlength="250" required/>
    <input type="hidden" name="${_csrf.parameterName}"
           value="${_csrf.token}"/>

    <div class="centeredForm">
        <button ng-disabled="!loginForm.$valid" class="button small icon fa-sign-in overheadButton" name="loginSubmit" ng-click="loginForm.submit()">
            Identificarse
        </button>
        <button ng-disabled="!loginForm.$valid" class="button mini icon fa-sign-in overheadButton" ng-click="loginForm.submit()"></button>
    </div>
</form>
