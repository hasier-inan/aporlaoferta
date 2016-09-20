<%@taglib prefix="sec"
          uri="http://www.springframework.org/security/tags" %>
<ul class="actions top_header">
    <sec:authorize access="isAnonymous()">
        <li>
            <button class="button small small--rounded hvr-icon-grow icon fa-sign-in whiteButton green-background" ng-click="displayLogin()">
                Identificarse
            </button>
            <button class="button mini icon hvr-icon-grow fa-sign-in whiteButton green-background account-management fixed"
                    ng-click="displayLogin()"></button>
        </li>
        <li>
            <button class="button small small--rounded hvr-icon-grow icon fa-user whiteButton blueish-background" ng-click="displaySignup()">
                Registrarse
            </button>
            <button class="button mini icon hvr-icon-grow fa-user whiteButton blueish-background account-management fixed nth"
                    ng-click="displaySignup()"></button>
        </li>
        <button ng-class="{hidden:scrollPosition}"
                class="button desktop overflown overflown-left icon hvr-icon-grow fa-sign-in whiteButton green-background"
                ng-click="displayLogin()"></button>
        <button ng-class="{hidden:scrollPosition}"
                class="button desktop overflown overflown-left overflown-second icon hvr-icon-grow fa-user whiteButton blueish-background"
                ng-click="displaySignup()"></button>
    </sec:authorize>

    <sec:authorize access="isAuthenticated()">
        <button class="button small small--rounded hvr-icon-grow icon fa-pencil-square whiteButton green-background" ng-click="displayOfferCreate()">
            Crear Oferta
        </button>
        <button class="button mini hvr-icon-grow icon fa-pencil-square whiteButton inline right-aligned green-background fixed"
                ng-click="displayOfferCreate()"></button>
        <button ng-class="{hidden:scrollPosition}"
                class="button desktop overflown overflown-left icon hvr-icon-grow fa-pencil-square whiteButton inline right-aligned green-background"
                ng-click="displayOfferCreate()"></button>
    </sec:authorize>

    <sec:authorize access="isAuthenticated()">
        <div ng-account-logout="accountLogout" class="profileForm" account-update="displayAccountUpdateForm"></div>

    </sec:authorize>
    <button class="button to-the-top hvr-icon-bob overflown overflown-right icon fa-chevron-down whiteButton no-decoration filters-displayed"
            ng-class="{hidden:scrollPosition}"
            ng-click="toTheTop()"></button>
</ul>