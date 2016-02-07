<%@taglib prefix="sec"
          uri="http://www.springframework.org/security/tags" %>
<div id="overheadSubContainer" class="overhead_subcontainer fadein hiddencontainer"
     ng-class="{'userRelated' :(displayAccountLogin || displayAccountCreation),
                 'offerRelated' : displayOfferCreation,
                 'offer-specifications-background' :displayOfferSpecifications,
                 'overhead_subcontainer-fullscreen':fullscreen}"
     ng-show="overheadVisible">
    <ul id="overheadSubContainerContent" class="actions top_header">
        <br/>

        <div ng-show="displayOfferSpecifications" ng-offer-specifications="offerSpecifications"
             the-offer="offerSpecifications" custom-close-callback="customCloseCallback"></div>
        <div ng-show="displayResponseFromServer" ng-response-from-server="responseFromServer"
             the-response="theResponse"></div>
        <sec:authorize ifAllGranted="ROLE_USER">
            <div ng-offer-creation="offerCreation" ng-show="displayOfferCreation"
                 overhead-display="overheadVisible" custom-close-callback="customCloseCallback"
                 display-callback="displayOfferCreate"></div>
            <div ng-offer-update="offerUpdate" ng-show="displayOfferToBeUpdate" offer="offerSpecifications[0]"
                 overhead-display="overheadVisible" custom-close-callback="customCloseCallback"
                 display-callback="displayOfferUpdate"></div>
            <div ng-account-update="accountUpdate" ng-show="displayAccountUpdate"
                 overhead-display="overheadVisible" custom-close-callback="customCloseCallback"
                 display-callback="displayAccountUpdateForm"></div>
        </sec:authorize>
        <sec:authorize ifNotGranted="ROLE_USER">
            <div ng-account-login="accountLogin" ng-show="displayAccountLogin"></div>
            <div ng-account-creation="accountCreation" ng-show="displayAccountCreation"
                 overhead-display="overheadVisible" custom-close-callback="customCloseCallback"
                 display-callback="displaySignup"></div>
        </sec:authorize>
        <li>
            <button class="button small icon fa-times close_button red-background"
                    ng-click="closeOverheadDisplay(customCloseCallback)">Cerrar
            </button>
            <button class="button mini icon fa-times close_button red-background"
                    ng-click="closeOverheadDisplay(customCloseCallback)"></button>
        </li>
    </ul>
</div>
<div id="overheadContainer" class="overhead_container " ng-show="overheadVisible" ng-keypress="keyHandler($event)"
     ng-click="justCloseOverheadDisplay()"></div>
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


