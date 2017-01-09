<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<title>aporlaoferta ~ comparte ofertas y promociones online </title>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<meta http-equiv="Content-Security-Policy"
      content="script-src 'self' https://ajax.googleapis.com https://www.google.com https://www.gstatic.com https://www.google-analytics.com; style-src 'self' 'unsafe-inline' https://ajax.googleapis.com https://www.google.com https://www.gstatic.com">
<meta property="og:site_name" content="aporlaoferta">
<meta name="google-site-verification" content="z28rreBCf5vqKa0AF697qgWVHQDsDlO9e6aetoSq10Q" />
<c:choose>
    <c:when test="${not empty offerId}">
        <meta property="og:title" content="aporlaoferta ~ ${offerTitle}"/>
        <meta property="og:url" content="http://www.aporlaoferta.com/offer?sh=${offerId}"/>
        <meta property="og:description" content="${offerDescription}"/>
        <meta property="og:image" content="${offerImage}"/>
        <meta property="keywords" content="aporlaoferta,ofertas online,${offerKeywords.replaceAll(' ', ',')}"/>
    </c:when>
    <c:otherwise>
        <meta property="og:title" content="aporlaoferta ~ comparte ofertas y promociones online"/>
        <meta property="og:url" content="http://www.aporlaoferta.com"/>
        <meta property="og:description" content="Promociones, rebajas y chollos online! Si conoces una oferta, ¡publícala! También hay miles para ti."/>
        <meta property="og:image" content="https://s3-us-west-2.amazonaws.com/aporlaofertaimages/static/logo3.png"/>
        <meta property="keywords" content="aporlaoferta,ofertas,chollos,rebajas,promociones,ofertas online,publicar,compartir,shopping,descuentos"/>
    </c:otherwise>
</c:choose>
<meta property="og:type" content="website"/>
