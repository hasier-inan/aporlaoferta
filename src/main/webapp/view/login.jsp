<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page session="true" %>
<form name='loginForm'
      action="<c:url value='/j_spring_security_check' />" method='POST' class="centeredForm">
    <input type="text" id="loginUsername" name="username" class="overheadField"
           placeholder="Usuario">
    <input type="password" id="loginPassword" name="password" class="overheadField"
           placeholder="Contrase&ntilde;a"/>
    <input type="hidden" name="${_csrf.parameterName}"
           value="${_csrf.token}"/>

    <div class="centeredForm">
        <button class="button small icon fa-sign-in overheadButton" name="loginSubmit" ng-click="loginForm.submit()">
            Identificarse
        </button>
        <button class="button mini icon fa-sign-in overheadButton" ng-click="loginForm.submit()"></button>
    </div>
</form>
</div>