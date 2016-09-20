<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div>
    <div ng-repeat="offer in theOffer track by offer.id">
        <div class="offerSpecificationsContainer">
            <sec:authorize access="isAuthenticated()">
                <sec:authentication var="nickname" property="principal.username"/>
                <div class="offer-specifications edit-offer"
                     ng-show="offer.offerUser.userNickname == '${nickname}'">
                    <button class="button small small--rounded icon hvr-icon-grow fa-pencil whiteButton green-background"
                            ng-click="updateOffer(offer)">
                        Editar oferta
                    </button>
                    <button class="button mini icon hvr-icon-grow fa-pencil whiteButton green-background"
                            ng-click="updateOffer(offer)">
                    </button>
                </div>
                <div class="offer-specifications edit-offer expire-offer"
                     ng-show="offer.offerUser.userNickname == '${nickname}'">
                    <button ng-disabled="offer.offerExpired"
                            class="button small small--rounded icon hvr-icon-grow fa-trash whiteButton red-background"
                            confirmed-click="expireOffer(offer)"
                            ng-confirm-click="¿Seguro que quieres caducar la oferta?">
                        Oferta Caducada
                    </button>
                    <button ng-disabled="offer.offerExpired"
                            class="button mini icon hvr-icon-grow fa-trash whiteButton red-background"
                            confirmed-click="expireOffer(offer)"
                            ng-confirm-click="¿Seguro que quieres caducar la oferta?">
                    </button>
                </div>
                <sec:authorize ifAllGranted="ROLE_ADMIN">
                    <div class="offer-specifications edit-offer"
                         ng-show="offer.offerUser.userNickname != '${nickname}'">
                        <button class="button small small--rounded icon hvr-icon-grow fa-pencil whiteButton green-background"
                                ng-click="updateOffer(offer)">
                            Editar oferta
                        </button>
                        <button class="button mini icon hvr-icon-grow fa-pencil whiteButton green-background"
                                ng-click="updateOffer(offer)">
                        </button>
                    </div>
                    <div class="offer-specifications edit-offer expire-offer"
                         ng-show="offer.offerUser.userNickname != '${nickname}'">
                        <button ng-disabled="offer.offerExpired"
                                class="button small small--rounded  icon hvr-icon-grow fa-trash whiteButton red-background"
                                confirmed-click="expireOffer(offer)"
                                ng-confirm-click="¿Seguro que quieres caducar la oferta?">
                            Oferta Caducada
                        </button>
                        <button ng-disabled="offer.offerExpired"
                                class="button mini icon hvr-icon-grow fa-trash whiteButton red-background"
                                confirmed-click="expireOffer(offer)"
                                ng-confirm-click="¿Seguro que quieres caducar la oferta?">
                        </button>
                    </div>
                </sec:authorize>
            </sec:authorize>
            <div class="offerSpecificationsLeftContainer containerSplitter">
                <div class="offerSpecificationsImage">
                    <sec:authorize ifAllGranted="ROLE_ADMIN">
                        <div class="remove-offer">
                            <i class="fa fa-trash" aria-hidden="true"
                               ng-confirm-click="Confirmar borrado de oferta"
                               confirmed-click="removeOffer(offer.id)"></i>
                        </div>
                    </sec:authorize>
                    <div class="offer-image-expired" ng-show="offer.offerExpired"></div>
                    <img ng-src="{{offer.offerImage}}" ng-class="offer.offerExpired?'offer-expired':''"/>

                    <div class=" offerSpecificationsFeedback">
                        <div class="offerSpecificationsFeedbackTotal">

                            <div class="feedbackSumatory">
                                <h2 class="feedbackSumatory">{{offer.offerPositiveVote - offer.offerNegativeVote |
                                    number:0}}</h2>

                                <div class="offerSpecificationsFeedbackSmall">
                                    <div id="offerPositiveFeedback">+{{offer.offerPositiveVote}}</div>
                                    <div id="offerNegativeFeedback">-{{offer.offerNegativeVote}}</div>
                                </div>
                            </div>
                            <sec:authorize access="isAnonymous()">
                                <div class="loginFeedbackMessage">Inicia sesión para votar esta oferta</div>
                            </sec:authorize>
                        </div>
                        <sec:authorize access="isAuthenticated()">
                            <div class="feedbackButtons">
                                <div>
                                    <button class="button small icon hvr-icon-grow fa-plus whiteButton green-background"
                                            ng-click="votePositive(offer.id)"></button>
                                </div>
                                <div>
                                    <button class="button small icon hvr-icon-grow fa-minus whiteButton red-background"
                                            ng-click="voteNegative(offer.id)"></button>
                                </div>
                            </div>
                        </sec:authorize>

                    </div>
                </div>

            </div>
            <div class="offerSpecificationsRightContainer containerSplitter">
                <div class="offerSpecificationsTitle">
                    <h2 ng-class="offer.offerExpired?'offer-expired':''">
                        <a href="{{offer.offerLink}}" target="_blank">{{offer.offerTitle}}:
                            {{parsePrice(offer.finalPrice)}}€</a>
                    </h2>

                    <div class="offerCompany offerSpecificationsBox">Compañía: {{offer.offerCompany.companyName}}</div>
                    <div class="offerSpecificationsOriginalPrice offerSpecificationsBox" ng-show="offer.originalPrice">
                        Precio original: {{parsePrice(offer.originalPrice)}}€
                    </div>
                    <div class="offerSpecificationsFinalPrice offerSpecificationsBox">
                        Precio de oferta: {{parsePrice(offer.finalPrice)}}€
                    </div>
                </div>
                <div class="offerSpecificationsSubtitle">
                    <img ng-src="{{offer.offerUser.userAvatar}}"/>

                    <div class="offerOwner">Creado por: <i
                            ng-class="{'user-banned' : (!offer.offerUser.enabled)}">{{offer.offerUser.userNickname}}</i>
                        <i ng-show="!offer.offerUser.enabled"> (Usuario expulsado)</i>
                    </div>
                    <sec:authorize ifAllGranted="ROLE_ADMIN">
                        <div class="remove-owner" ng-show="offer.offerUser.enabled">
                            <i class="fa fa-trash" aria-hidden="true"
                               ng-confirm-click="Confirmar expulsión del usuario {{offer.offerUser.userNickname}}"
                               confirmed-click="banUser(offer.offerUser.userNickname)"></i>
                        </div>
                    </sec:authorize>
                    <div class="offerCreatedDate">el {{offer.offerCreatedDate | date:'MM/dd/yyyy @h:mma'}}
                    </div>
                </div>
                <div class="offerSpecificationsDescription offerDescription">
                    <pre class="parsed-text">{{offer.offerDescription}}</pre>
                </div>
                <div ng-social-media="socialMedia" share-url="sharePrefix+offer.id"
                     share-text="offer.finalPrice + sharePrice + offer.offerTitle"></div>
            </div>

            <div class="offerSpecificationsLink">
                <a href="{{offer.offerLink}}" target="_blank">Ir a la oferta <i
                        class="fa fa-cart-arrow-down"></i></a>
            </div>

            <div ng-offer-comments="offerComments" the-comments="offer.offerComments" the-offer="offer.id"
                 class="offerSpecificationsComments" comments-custom-close-callback="commentsCustomCloseCallback"></div>

        </div>
    </div>
</div>
