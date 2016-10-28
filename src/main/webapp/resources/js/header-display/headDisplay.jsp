<%@taglib prefix="sec"
          uri="http://www.springframework.org/security/tags" %>
<div id="overheadSubContainer" class="overhead_subcontainer hiddencontainer"
     ng-class="{ 'overhead_subcontainer--visible' : (overheadVisible),
                 'userRelated' :(displayAccountCreation || displayAccountUpdate),
                 'loginRelated' : (displayAccountLogin),
                 'offerRelated' : (displayOfferCreation),
                 'tutorialRelated' : (displayTutorial),
                 'offer-specifications-background' :displayOfferSpecifications,
                 'overhead_subcontainer-fullscreen':fullscreen}"
     ng-show="overheadVisible">
    <ul id="overheadSubContainerContent" class="actions top_header">
        <br/>

        <div ng-show="displayOfferSpecifications" ng-offer-specifications="offerSpecifications"
             the-offer="offerSpecifications" custom-close-callback="customCloseCallback"></div>
        <div ng-show="displayResponseFromServer" ng-response-from-server="responseFromServer"
             the-response="theResponse"></div>
        <div ng-show="displayTutorial" ng-quick-tutorial="quickTutorial" is-displayed="tutorialIsDisplayed"></div>
        <sec:authorize access="isAuthenticated()">
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

        <div ng-account-login="accountLogin" ng-show="displayAccountLogin"></div>
        <sec:authorize access="isAnonymous()">
            <div ng-account-creation="accountCreation" ng-show="displayAccountCreation"
                 overhead-display="overheadVisible" custom-close-callback="customCloseCallback"
                 display-callback="displaySignup"></div>
        </sec:authorize>
        <li>
            <button class="button small small--rounded icon hvr-icon-grow fa-times close_button grey-background"
                    ng-click="closeOverheadDisplay(customCloseCallback)">Cerrar
            </button>
            <button class="button mini icon hvr-icon-grow fa-times close_button grey-background"
                    ng-click="closeOverheadDisplay(customCloseCallback)"></button>
        </li>
    </ul>
</div>
<div id="overheadContainer" class="overhead_container " ng-show="overheadVisible" ng-keypress="keyHandler($event)">
</div>

<div ng-head-account-and-offer-management
     display-login="displayLogin"
     display-signup="displaySignup"
     display-offer-create="displayOfferCreate"
     display-account-update-form="displayAccountUpdateForm"
     ng-hide="noAccounts">
</div>


