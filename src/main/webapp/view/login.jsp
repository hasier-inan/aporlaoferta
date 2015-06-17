<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page session="true" %>
<c:if test="${not empty error}">
    <div class="error">${error}</div>
</c:if>
<c:if test="${not empty msg}">
    <div class="msg">${msg}</div>
</c:if>
<form name='loginForm'
      action="<c:url value='/j_spring_security_check' />" method='POST' class="centeredForm">
    <label for="loginUsername" class="overheadLabel">Usuario</label>
    <input type="text" id="loginUsername" name="username" class="overheadField">
    <label for="loginPassword" class="overheadLabel">Contraseña:</label>
    <input type="password" id="loginPassword" name="password" class="overheadField"/>
    <input type="hidden" name="${_csrf.parameterName}"
           value="${_csrf.token}"/>

    <div class="centeredForm">
        <button class="button small icon fa-sign-in overheadButton" name="loginSubmit" ng-click="loginForm.submit()">Identificarse
        </button>
        <button class="button mini icon fa-sign-in overheadButton" ng-click="loginForm.submit()"></button>
    </div>
</form>
</div>