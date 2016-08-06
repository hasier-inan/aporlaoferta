<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page session="true" %>
<!DOCTYPE HTML>

<!-- aporlaoferta -->
<!-- p4p spanish offer and deals -->
<!-- Fractal template by HTML5 UP html5up.net -->
<!-- icons and logos by font awesome http://fortawesome.github.io/ -->
<html data-ng-app="aPorLaOferta" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>aporlaoferta ~ ofertas y promociones online </title>
    <%@include file="head/meta.jsp" %>
    <sec:csrfMetaTags/>
    <link rel="shortcut icon"
          href="resources/images/favicon.ico"/>
    <%@include file="head/resources.jsp" %>
</head>
<body id="top" data-ng-controller="APorLaOfertaController" class="top-header">

<div ng-overhead-display="overheadDisplay" no-accounts="true" overhead-visible="overheadVisible"></div>

<header id="header" class="top-header">
    <div class="content only-page">
        <h1 class="only-page">
            <em>aporlaoferta</em>
        </h1>

        <div class="mainLogo only-page">
        </div>
        <p>Ofertas y promociones online</p>
    </div>
</header>
<div ng-password-forgotten="ngPasswordForgotten" nick="'${nick}'" uuid="'${uuid}'" custom-close-callback="customCloseCallback"></div>

<footer id="footer" class="forgotten_footer">
    <%@include file="footer/social.jsp" %>
    <p class="copyright">&copy; aporlaoferta, 2016</p>
</footer>

</body>
</html>