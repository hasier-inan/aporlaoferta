<%@taglib prefix="sec"
          uri="http://www.springframework.org/security/tags" %>
<div id="overheadSubContainer" class="overhead_subcontainer fadein fadeout" ng-show="overheadVisible">
    <ul class="actions top_header">
        <sec:authorize ifAllGranted="ROLE_USER">
            <div ng-offer-creation="offerCreation" ng-show="displayOfferCreation"></div>
        </sec:authorize>
        <sec:authorize ifNotGranted="ROLE_USER">
            <div ng-account-login="accountLogin" ng-show="displayAccountLogin"></div>
            <div ng-account-creation="accountCreation" ng-show="displayAccountCreation"></div>
        </sec:authorize>
        <li>
            <button class="button small icon fa-times-circle-o close_button" ng-click="closeOverheadDisplay()">Cerrar</button>
            <button class="button mini icon fa-times-circle-o close_button" ng-click="closeOverheadDisplay()"></button>
        </li>
    </ul>
</div>
<div id="overheadContainer" class="overhead_container " ng-show="overheadVisible" ng-click="closeOverheadDisplay()"></div>
<ul class="actions top_header">
    <sec:authorize ifNotGranted="ROLE_USER">
        <li>
            <button class="button small icon fa-sign-in whiteButton" ng-click="displayLogin()">Identificarse</button>
            <button class="button mini icon fa-sign-in whiteButton" ng-click="displayLogin()"></button>
        </li>
        <li>
            <button class="button small icon fa-user whiteButton" ng-click="displaySignup()">Registrarse</button>
            <button class="button mini icon fa-user whiteButton" ng-click="displaySignup()"></button>
        </li>
    </sec:authorize>

    <sec:authorize ifAllGranted="ROLE_USER">
        <li id="displayOfferCreateLi">
            <button class="button small icon fa-pencil-square whiteButton" ng-click="displayOfferCreate()">Crear
                Oferta
            </button>
            <button class="button mini icon fa-pencil-square whiteButton" ng-click="displayOfferCreate()"></button>
        </li>
    </sec:authorize>

    <sec:authorize ifAnyGranted="ROLE_USER, ROLE_ADMIN">
        <li class="displayProfileLi"><div ng-account-logout="accountLogout" class="profileForm"></div></li>
    </sec:authorize>
</ul>

<header id="header">
    <div class="content">
        <h1><em>aporlaoferta</em></h1>

        <p>Ofertas y promociones online</p>
        <ul class="actions">
            <li><a href="#promotion_list" class="button icon fa-shopping-cart whiteButton">Últimas promociones</a></li>
            <li><a href="#" class="button special icon fa-fire whiteButton">Ofertas calientes</a></li>
        </ul>
    </div>
    <div class="image phone">
        <div class="inner"><img src="resources/images/screen.jpg" alt=""/></div>
    </div>
</header>
