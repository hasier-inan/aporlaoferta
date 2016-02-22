<%--
  Created by IntelliJ IDEA.
  User: hasiermetal
  Date: 20/02/16
  Time: 18:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html data-ng-app="aPorLaOferta">
<head>
    <script src="resources/assets/js/jquery.min.js"></script>
    <script src="resources/assets/js/jquery.scrolly.min.js"></script>
    <script src="resources/assets/js/skel.min.js"></script>
    <script src="resources/assets/js/util.js"></script>
    <!--[if lte IE 8]>
    <script src="resources/assets/js/ie/respond.min.js"></script><![endif]-->
    <script src="resources/assets/js/main.js"></script>
    <!--core-->
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>
    <script src="resources/js/lib/angular-recaptcha.js"></script>
    <script src="https://www.google.com/recaptcha/api.js?onload=vcRecaptchaApiLoaded&render=explicit&hl=es" async defer></script>
    <script src="resources/js/lib/angular-filter.min.js"></script>
    <script src="resources/js/lib/ng-flow-standalone.js"></script>
    <!--angular material dependencies-->
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular-animate.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular-route.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular-aria.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular-messages.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angular_material/1.0.4/angular-material.min.js"></script>
    <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/angular_material/1.0.4/angular-material.min.css">
    <!--modules-->
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
    <title></title>
</head>
<body data-ng-controller="APorLaOfertaController" >

<div id="createOfferCompanySection" ng-company-manager="companyManager"
      class="select-validation"></div>

</body>
</html>
