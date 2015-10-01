<div id="overheadSubContainer" class="overhead_subcontainer fadein hiddencontainer"
     ng-class="(displayAccountLogin || displayAccountCreation)?'userRelated':
     (displayOfferCreation?'offerRelated':(displayOfferSpecifications)?'offer-specifications-background':'')"
     ng-show="overheadVisible">
    <ul id="overheadSubContainerContent" class="actions top_header">
        <br/>
        <div ng-show="displayResponseFromServer" ng-response-from-server="responseFromServer"
             the-response="theResponse"></div>
        <li>
            <button class="button small icon fa-times close_button" ng-click="closeOverheadDisplay(customCloseCallback)">Cerrar
            </button>
            <button class="button mini icon fa-times close_button" ng-click="closeOverheadDisplay(customCloseCallback)"></button>
        </li>
    </ul>
</div>
<div id="overheadContainer" class="overhead_container " ng-show="overheadVisible" ng-keypress="keyHandler($event)" ng-click="closeOverheadDisplay()"></div>
