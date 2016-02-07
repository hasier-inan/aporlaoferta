<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page session="true" %>
<!DOCTYPE HTML>

<!-- aporlaoferta, by thatsoftwarecompany -->
<!-- p4p spanish offer and deals -->
<!-- Fractal template by HTML5 UP html5up.net -->
<!-- icons and logos by font awesome http://fortawesome.github.io/ -->
<!-- file upload base by ng-flow -->
<html data-ng-app="aPorLaOferta" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>aporlaoferta ~ ofertas y promociones online </title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta property="og:site_name" content="aporlaoferta">
    <meta property="og:url" content="http://www.aporlaoferta.com" />
    <meta property="og:type" content="gestor y buscador de ofertas online" />
    <meta property="og:title" content="aporlaoferta ~ ofertas y promociones online" />
    <meta property="og:description" content="aporlaoferta es un gestor de ofertas, promociones, rebajas y chollos online, creados por y para todos los usuarios españoles" />
    <meta property="og:image" content="http://www.aporlaoferta.com/images/logo.jpg" />
    <sec:csrfMetaTags/>
    <link rel="shortcut icon"
          href="resources/images/favicon.ico" />
    <!--template-->
    <!--[if lte IE 8]>
    <script src="resources/assets/js/ie/html5shiv.js"></script><![endif]-->
    <link rel="stylesheet" href="resources/assets/css/main.css"/>
    <!--[if lte IE 8]>
    <link rel="stylesheet" href="resources/assets/css/ie8.css"/><![endif]-->
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="resources/assets/css/ie9.css"/><![endif]-->
    <link rel="stylesheet" href="resources/assets/css/main_decorator.css"/>
    <link rel="stylesheet" href="resources/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="resources/assets/css/main_animator.css"/>
    <link rel="stylesheet" href="resources/js/offer/offer-specifications/offerSpecifications.css"/>
    <script src="resources/assets/js/jquery.min.js"></script>
    <script src="resources/assets/js/jquery.scrolly.min.js"></script>
    <script src="resources/assets/js/skel.min.js"></script>
    <script src="resources/assets/js/util.js"></script>
    <!--[if lte IE 8]>
    <script src="resources/assets/js/ie/respond.min.js"></script><![endif]-->
    <script src="resources/assets/js/main.js"></script>
    <!--core-->
    <script src="resources/js/lib/angular.js"></script>
    <script src="resources/js/lib/angular-animate.js"></script>
    <script src="https://www.google.com/recaptcha/api.js?onload=vcRecaptchaApiLoaded&render=explicit&hl=es" async defer></script>
    <script src="resources/js/lib/angular-recaptcha.js"></script>
    <script src="resources/js/lib/angular-filter.min.js"></script>
    <script src="resources/js/lib/ng-flow-standalone.js"></script>
    <script src="resources/assets/js/main.js"></script>
    <script src="resources/js/main-controller/mainController.js"></script>
    <script src="resources/js/request-handler/requestManagerService.js"></script>
    <script src="resources/js/account/account-signup/ngAccountCreationDirective.js"></script>
    <script src="resources/js/account/account-signup/ngAccountUpdateDirective.js"></script>
    <script src="resources/js/account/account-signup/pwdCheck.js"></script>
    <script src="resources/js/account/account-login/ngAccountLoginDirective.js"></script>
    <script src="resources/js/account/account-logout/ngAccountLogoutDirective.js"></script>
    <script src="resources/js/config/configService.js"></script>
    <script src="resources/js/offer/offer-list/ngOfferListDirective.js"></script>
    <script src="resources/js/offer/offer-list/ngPromotionListDirective.js"></script>
    <script src="resources/js/offer/offer-specifications/ngOfferSpecifications.js"></script>
    <script src="resources/js/offer/offer-specifications/offer-comments/ngOfferComments.js"></script>
    <script src="resources/js/offer/offer-specifications/offer-comments/ngOfferCommentsQuotes.js"></script>
    <script src="resources/js/offer/offer-creation/ngOfferCreationDirective.js"></script>
    <script src="resources/js/offer/offer-creation/ngOfferUpdateDirective.js"></script>
    <script src="resources/js/offer/offer-creation/ngCompanyManagerDirective.js"></script>
    <script src="resources/js/offer/offer-creation/ngOfferCategoryManagerDirective.js"></script>
    <script src="resources/js/offer/offer-filter/ngOfferFilterDirective.js"></script>
    <script src="resources/js/header-display/ngHeadDisplayDirective.js"></script>
    <script src="resources/js/uploader/ngImageUploadDirective.js"></script>
    <script src="resources/js/response/ngResponseFromServer.js"></script>
    <script src="resources/js/response/alertService.js"></script>
    <script src="resources/js/offer/offer-list/offerManagerService.js"></script>
    <link rel="stylesheet" href="resources/js/uploader/imageUpload.css"/>
    <!--social media-->
    <script src="resources/js/social/ngSocialMediaDirective.js"></script>
    <%--<script id="facebook-jssdk" src="//connect.facebook.net/es_ES/sdk.js#xfbml=1&version=v2.5"></script>--%>
</head>
<body id="top" data-ng-controller="APorLaOfertaController" flow-prevent-drop ng-keydown="keyHandler($event)">

<c:if test="${not empty msg}">
    <input type="text" id="errorMessage" style="display:none" value="${msg}"/>
</c:if>

<div ng-overhead-display="overheadDisplay" specific-offer="${specificOffer}"></div>

<div ng-promotion-list="promotionList"></div>

<!-- Footer -->
<footer id="footer">
    <ul class="icons">
        <li><a href="#" class="icon fa-facebook social-media-link"><span class="label">Facebook</span></a></li>
        <li><a href="#" class="icon fa-twitter social-media-link"><span class="label">Twitter</span></a></li>
        <li><a href="#" class="icon fa-google-plus social-media-link"><span class="label">Instagram</span></a></li>
    </ul>
    <p class="copyright">&copy; aporlaoferta, thatsoftwarecompany 2015</p>
</footer>
</body>
</html>