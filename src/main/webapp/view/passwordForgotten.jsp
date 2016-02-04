<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page session="true" %>
<!DOCTYPE HTML>

<!-- aporlaoferta, by thatsoftwarecompany -->
<!-- p4p spanish offer and deals -->
<!-- Fractal template by HTML5 UP html5up.net -->
<!-- icons and logos by font awesome http://fortawesome.github.io/ -->
<html data-ng-app="aPorLaOferta" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>aporlaoferta ~ ofertas y promociones online </title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="author" content="thatsoftwarecompany"/>
    <meta name="description" content="Cover artwork library"/>
    <meta name="keywords"
          content="ofertas p4p rebajas tiendas promociones online"/>
    <sec:csrfMetaTags/>
    <link rel="shortcut icon"
          href="resources/images/favicon.ico"/>
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
    <script src="resources/js/header-display/ngHeadDisplayLightDirective.js"></script>
    <script src="resources/js/uploader/ngImageUploadDirective.js"></script>
    <script src="resources/js/response/ngResponseFromServer.js"></script>
    <script src="resources/js/response/alertService.js"></script>
    <script src="resources/js/offer/offer-list/offerManagerService.js"></script>
    <script src="resources/js/account/password-forgotten/ngPasswordForgottenDirective.js"></script>
</head>
<body id="top" data-ng-controller="APorLaOfertaController" class="password-forgotten">

<div ng-overhead-display-light="overheadDisplayLight" class="overheadDisplayLight"></div>

<header id="header" class="password-forgotten">
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
    <ul class="icons">
        <li><a href="#" class="icon fa-facebook forgotten_footer"><span class="label">Facebook</span></a></li>
        <li><a href="#" class="icon fa-twitter forgotten_footer"><span class="label">Twitter</span></a></li>
        <li><a href="#" class="icon fa-instagram forgotten_footer"><span class="label">Instagram</span></a></li>
    </ul>
    <p class="copyright">&copy; aporlaoferta, thatsoftwarecompany 2015</p>
</footer>

</body>
</html>