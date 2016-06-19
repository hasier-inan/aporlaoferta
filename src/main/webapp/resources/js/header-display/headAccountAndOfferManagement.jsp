<%@taglib prefix="sec"
          uri="http://www.springframework.org/security/tags" %>
<ul class="actions top_header">
    <sec:authorize ifNotGranted="ROLE_USER">
        <li>
            <button class="button small icon fa-sign-in whiteButton green-background" ng-click="displayLogin()">
                Identificarse
            </button>
            <button class="button mini icon fa-sign-in whiteButton green-background" ng-click="displayLogin()"></button>
        </li>
        <li>
            <button class="button small icon fa-user whiteButton blueish-background" ng-click="displaySignup()">
                Registrarse
            </button>
            <button class="button mini icon fa-user whiteButton blueish-background" ng-click="displaySignup()"></button>
        </li>
    </sec:authorize>

    <sec:authorize ifAllGranted="ROLE_USER">
        <button class="button small icon fa-pencil-square whiteButton green-background" ng-click="displayOfferCreate()">
            Crear
            Oferta
        </button>
        <button class="button mini icon fa-pencil-square whiteButton inline right-aligned green-background"
                ng-click="displayOfferCreate()"></button>
    </sec:authorize>

    <sec:authorize ifAnyGranted="ROLE_USER, ROLE_ADMIN">
        <div ng-account-logout="accountLogout" class="profileForm"></div>
        <button class="button small icon fa-pencil-square whiteButton blueish-background"
                ng-click="displayAccountUpdateForm()">Actualizar
            Perfil
        </button>
        <button class="button mini icon fa-user whiteButton inline right-aligned blueish-background"
                ng-click="displayAccountUpdateForm()"></button>
    </sec:authorize>
</ul>