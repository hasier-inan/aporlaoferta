<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html ng-app="aPorLaOferta" xmlns="http://www.w3.org/1999/xhtml" ng-controller="APorLaOfertaController">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta http-equiv="Content-Security-Policy" content="frame-ancestors 'self'">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>aporlaoferta</title>
    <meta name="description" content="Cover artwork library"/>
    <meta name="keywords"
          content="ofertas p4p rebajas tiendas online"/>
    <meta name="author" content="thatsoftwarecompany"/>
    <sec:csrfMetaTags/>
    <script src="resources/js/lib/jquery-1.11.3.min.js" type="text/javascript" language="javascript"></script>
    <script src="resources/js/lib/angular.js"></script>
    <script src="resources/js/main-controller/mainController.js"></script>
    <script src="resources/js/request-handler/requestManagerService.js"></script>
    <script src="resources/js/account/account-signup/ngAccountCreationDirective.js"></script>
    <script src="resources/js/account/account-login/ngAccountLoginDirective.js"></script>
    <script src="resources/js/account/account-logout/ngAccountLogoutDirective.js"></script>
    <script src="resources/js/config/configService.js"></script>
    <script src="resources/js/offer/offer-list/ngOfferListDirective.js"></script>
    <script src="resources/js/offer/offer-list/ngPromotionListDirective.js"></script>
    <script src="resources/js/offer/offer-specifications/ngOfferSpecifications.js"></script>
    <script src="resources/js/offer/offer-specifications/offer-comments/ngOfferComments.js"></script>
    <script src="resources/js/offer/offer-creation/ngOfferCreationDirective.js"></script>
    <script src="resources/js/offer/offer-creation/ngCompanyManagerDirective.js"></script>
    <script src="resources/js/offer/offer-creation/ngOfferCategoryManagerDirective.js"></script>
</head>
<body>

<div ng-promotion-list="promotionList"></div>

<hr/>
<sec:authorize ifAllGranted="ROLE_USER">
    <div ng-offer-creation="offerCreation"></div>
    <hr/>
</sec:authorize>


<sec:authorize ifNotGranted="ROLE_USER">
    <div ng-account-login="accountLogin"></div>
    <hr/>
    <div ng-account-creation="accountCreation"></div>
</sec:authorize>

<sec:authorize ifAnyGranted="ROLE_USER, ROLE_ADMIN">
    <div ng-account-logout="accountLogout"></div>
</sec:authorize>
</body>
</html>
