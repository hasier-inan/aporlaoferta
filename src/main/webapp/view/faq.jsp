<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page session="true" %>
<!DOCTYPE HTML>

<!-- aporlaoferta -->
<!-- p4p spanish offer and deals -->
<!-- Fractal template by HTML5 UP html5up.net -->
<!-- icons and logos by font awesome http://fortawesome.github.io/ -->
<html data-ng-app="aPorLaOferta" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="head/meta.jsp" %>
    <sec:csrfMetaTags/>
    <link rel="shortcut icon"
          href="resources/images/favicon.ico"/>
    <%@include file="head/resources.jsp" %>
</head>
<body id="top" data-ng-controller="APorLaOfertaController" class="top-header">

<div ng-overhead-display="overheadDisplay" no-accounts="true" overhead-visible="overheadVisible"></div>

<%@include file="head/logo.jsp" %>
<%@include file="faq/definition.jsp" %>

<footer id="footer">
    <div ng-footer="footer"/>
</footer>
<%@include file="footer/cookie.jsp" %>
</body>
</html>