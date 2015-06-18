<%@taglib prefix="sec"
          uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/j_spring_security_logout" var="logoutUrl"/>
<form action="${logoutUrl}" method="post" id="logoutForm" class="profileHiddenForm">
    <input type="hidden" name="${_csrf.parameterName}"
           value="${_csrf.token}"/>
</form>
<script>
    function formSubmit() {
        document.getElementById("logoutForm").submit();
    }
</script>
<c:if test="${pageContext.request.userPrincipal.name != null}">
        <label class="overheadLabel">Usuario : ${pageContext.request.userPrincipal.name}</label>
        <button class="button small icon fa-sign-out whiteButton red-background" onclick="formSubmit()">Cerrar sesi&oacute;n</button>
        <button class="button mini icon fa-sign-out whiteButton inline red-background" onclick="formSubmit()"></button>
</c:if>
