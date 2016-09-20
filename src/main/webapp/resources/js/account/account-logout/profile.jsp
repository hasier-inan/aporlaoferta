<%@taglib prefix="sec"
          uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/j_spring_security_logout" var="logoutUrl"/>
<form action="${logoutUrl}" method="post" id="logoutForm" class="profileHiddenForm">
    <input type="hidden" name="${_csrf.parameterName}"
           value="${_csrf.token}"/>
</form>

<c:if test="${pageContext.request.userPrincipal.name != null}">
    <div id="userProfileLogout">
        <img ng-src="{{userAvatar}}"/>
        <label class="overheadLabel">${pageContext.request.userPrincipal.name}</label>
    </div>
    <div dropdown-select="profileOptions"
         dropdown-model="profileModel"
         dropdown-item-label="text" class="icon hvr-icon-grow fa fa-cogs profile-options-dropdown">

    </div>
</c:if>
