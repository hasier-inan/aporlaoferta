<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page session="true" %>
<!DOCTYPE HTML>

<!-- aporlaoferta -->
<!-- p4p spanish offer and deals -->
<!-- Fractal template by HTML5 UP html5up.net -->
<!-- icons and logos by font awesome http://fortawesome.github.io/ -->
<!-- file upload base by ng-flow -->
<!-- angular material auto-complete from https://material.angularjs.org -->
<!-- custom dropdown from https://github.com/jseppi/angular-dropdowns -->
<!-- design by http://aitormaguregi.com/ -->
<html data-ng-app="aPorLaOferta" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="head/meta.jsp" %>
    <sec:csrfMetaTags/>
    <link rel="shortcut icon"
          href="resources/images/favicon.ico"/>
    <%@include file="head/resources.jsp" %>
</head>
<body id="top" data-ng-controller="APorLaOfertaController" flow-prevent-drop ng-keydown="keyHandler($event)"
      ng-class="{'no-overflow':overheadVisible && !overflowEnabled}">

<c:if test="${not empty msg}">
    <input type="text" id="errorMessage" style="display:none" value="${msg}"/>
</c:if>

<div ng-overhead-display="overheadDisplay"
     specific-offer="${specificOffer}"
     specific-offer-data='${specificOfferData}'
     fullscreen="fullscreen"
     no-accounts="fullscreen"
     overflow-enabled="overflowEnabled"
     overhead-visible="overheadVisible"></div>

<div ng-promotion-list="promotionList" ng-hide="fullscreen"></div>

<!-- Footer -->
<footer id="footer" ng-class="{'overhead-footer' : (fullscreen)}">
    <div ng-footer="footer"/>
</footer>
<c:if test="${!cookiesAccepted}">
    <%@include file="footer/cookie.jsp" %>
</c:if>
</body>
</html>