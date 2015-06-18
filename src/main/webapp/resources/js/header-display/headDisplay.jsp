<%@taglib prefix="sec"
          uri="http://www.springframework.org/security/tags" %>
<div id="overheadSubContainer" class="overhead_subcontainer fadein fadeout" ng-show="overheadVisible">
    <ul id="overheadSubContainerContent" class="actions top_header">
        <div ng-show="displayOfferSpecifications" ng-offer-specifications="offerSpecifications" the-offer="offerSpecifications"></div>
        <sec:authorize ifAllGranted="ROLE_USER">
            <div ng-offer-creation="offerCreation" ng-show="displayOfferCreation"></div>
        </sec:authorize>
        <sec:authorize ifNotGranted="ROLE_USER">
            <div ng-account-login="accountLogin" ng-show="displayAccountLogin"></div>
            <div ng-account-creation="accountCreation" ng-show="displayAccountCreation"></div>
        </sec:authorize>
        <li>
            <button class="button small icon fa-times close_button" ng-click="closeOverheadDisplay()">Cerrar
            </button>
            <button class="button mini icon fa-times close_button" ng-click="closeOverheadDisplay()"></button>
        </li>
    </ul>
</div>
<div id="overheadContainer" class="overhead_container " ng-show="overheadVisible"></div>
<ul class="actions top_header">
    <sec:authorize ifNotGranted="ROLE_USER">
        <li>
            <button class="button small icon fa-sign-in whiteButton green-background" ng-click="displayLogin()">
                Identificarse
            </button>
            <button class="button mini icon fa-sign-in whiteButton green-background" ng-click="displayLogin()"></button>
        </li>
        <li>
            <button class="button small icon fa-user whiteButton" ng-click="displaySignup()">Registrarse</button>
            <button class="button mini icon fa-user whiteButton" ng-click="displaySignup()"></button>
        </li>
    </sec:authorize>

    <sec:authorize ifAllGranted="ROLE_USER">
        <button class="button small icon fa-pencil-square whiteButton" ng-click="displayOfferCreate()">Crear
            Oferta
        </button>
        <button class="button mini icon fa-pencil-square whiteButton inline" ng-click="displayOfferCreate()"></button>
    </sec:authorize>

    <sec:authorize ifAnyGranted="ROLE_USER, ROLE_ADMIN">
        <div ng-account-logout="accountLogout" class="profileForm"></div>
    </sec:authorize>
</ul>


