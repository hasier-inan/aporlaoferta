<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE HTML>
<!--
aporlaoferta, by thatsoftwarecompany
p4p spanish offer and deals
Fractal template by HTML5 UP html5up.net
-->
<html ng-app="aPorLaOferta" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>aporlaoferta ~ ofertas y promociones online </title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="author" content="thatsoftwarecompany"/>
    <meta name="description" content="Cover artwork library"/>
    <meta name="keywords"
          content="ofertas p4p rebajas tiendas promociones online"/>
    <sec:csrfMetaTags/>
    <!--template-->
    <!--[if lte IE 8]>
    <script src="resources/assets/js/ie/html5shiv.js"></script><![endif]-->
    <link rel="stylesheet" href="resources/assets/css/main.css"/>
    <!--[if lte IE 8]>
    <link rel="stylesheet" href="resources/assets/css/ie8.css"/><![endif]-->
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="resources/assets/css/ie9.css"/><![endif]-->
    <link rel="stylesheet" href="resources/assets/css/main_decorator.css"/>
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
    <script src="resources/assets/js/main.js"></script>
    <script src="resources/js/main-controller/mainController.js"></script>
    <script src="resources/js/request-handler/requestManagerService.js"></script>
    <script src="resources/js/account/account-signup/ngAccountCreationDirective.js"></script>
    <script src="resources/js/account/account-login/ngAccountLoginDirective.js"></script>
    <script src="resources/js/account/account-logout/ngAccountLogoutDirective.js"></script>
    <script src="resources/js/account/accountController.js"></script>
    <script src="resources/js/config/configService.js"></script>
    <script src="resources/js/offer/offer-list/ngOfferListDirective.js"></script>
    <script src="resources/js/offer/offer-specifications/ngOfferSpecifications.js"></script>
    <script src="resources/js/offer/offer-specifications/offer-comments/ngOfferComments.js"></script>
    <script src="resources/js/offer/offer-creation/offerCreationService.js"></script>
    <script src="resources/js/offer/offer-creation/ngOfferCreationDirective.js"></script>
    <script src="resources/js/offer/offer-creation/ngCompanyManagerDirective.js"></script>
    <script src="resources/js/offer/offer-creation/ngOfferCategoryManagerDirective.js"></script>
    <script src="resources/js/header-display/ngHeadDisplayDirective.js"></script>
</head>
<body id="top" ng-controller="APorLaOfertaController">

<div ng-overhead-display="overheadDisplay"></div>

<div ng-offer-list="offerList"></div>

<!-- Footer -->
<footer id="footer">
    <ul class="icons">
        <li><a href="#" class="icon fa-facebook"><span class="label">Facebook</span></a></li>
        <li><a href="#" class="icon fa-twitter"><span class="label">Twitter</span></a></li>
        <li><a href="#" class="icon fa-instagram"><span class="label">Instagram</span></a></li>
    </ul>
    <p class="copyright">&copy; aporlaoferta, thatsoftwarecompany 2015</p>
</footer>

</body>
</html>