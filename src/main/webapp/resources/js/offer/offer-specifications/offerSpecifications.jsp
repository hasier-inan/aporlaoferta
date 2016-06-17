<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div>
    <div ng-repeat="offer in theOffer track by offer.id">
        <div class="offerSpecificationsContainer">
            <sec:authorize ifAllGranted="ROLE_USER">
                <sec:authentication var="nickname" property="principal.username"/>
                <div class="offer-specifications edit-offer"
                     ng-show="offer.offerUser.userNickname == '${nickname}'">
                    <button class="button small icon fa-pencil whiteButton green-background"
                            ng-click="updateOffer(offer)">
                        Editar oferta
                    </button>
                    <button class="button mini icon fa-pencil whiteButton green-background"
                            ng-click="updateOffer(offer)">
                    </button>
                </div>
                <div class="offer-specifications edit-offer expire-offer"
                     ng-show="offer.offerUser.userNickname == '${nickname}'">
                    <button ng-disabled="offer.offerExpired"
                            class="button small icon fa-trash whiteButton red-background"
                            confirmed-click="expireOffer(offer)"
                            ng-confirm-click="¿Seguro que quieres caducar la oferta?">
                        Oferta Caducada
                    </button>
                    <button ng-disabled="offer.offerExpired"
                            class="button mini icon fa-trash whiteButton red-background"
                            confirmed-click="expireOffer(offer)"
                            ng-confirm-click="¿Seguro que quieres caducar la oferta?">
                    </button>
                </div>
            </sec:authorize>
            <div class="offerSpecificationsLeftContainer containerSplitter">
                <div class="offerSpecificationsImage">
                    <div class="offer-image-expired" ng-show="offer.offerExpired">Oferta caducada</div>
                    <img ng-src="{{offer.offerImage}}" ng-class="offer.offerExpired?'offer-expired':''"/>

                    <div class=" offerSpecificationsFeedback">
                        <div class="offerSpecificationsFeedbackTotal">

                            <div class="feedbackSumatory">
                                <h1 class="feedbackSumatory">{{offer.offerPositiveVote - offer.offerNegativeVote |
                                    number:0}}</h1>

                                <div class="offerSpecificationsFeedbackSmall">
                                    <div id="offerPositiveFeedback">+{{offer.offerPositiveVote}}</div>
                                    <div id="offerNegativeFeedback">-{{offer.offerNegativeVote}}</div>
                                </div>
                            </div>
                            <sec:authorize ifNotGranted="ROLE_USER">
                                <div class="loginFeedbackMessage">Inicia sesión para votar esta oferta</div>
                            </sec:authorize>
                        </div>
                        <sec:authorize ifAllGranted="ROLE_USER">
                            <div class="feedbackButtons">
                                <div>
                                    <button class="button small icon fa-plus whiteButton green-background"
                                            ng-click="votePositive(offer.id)"></button>
                                </div>
                                <div>
                                    <button class="button small icon fa-minus whiteButton red-background"
                                            ng-click="voteNegative(offer.id)"></button>
                                </div>
                            </div>
                        </sec:authorize>

                    </div>
                </div>

            </div>
            <div class="offerSpecificationsRightContainer containerSplitter">
                <div class="offerSpecificationsTitle">
                    <h1 ng-class="offer.offerExpired?'offer-expired':''">
                        <a href="http://{{offer.offerLink}}" target="_blank">{{offer.offerTitle}}:
                            {{offer.finalPrice}}€</a>
                    </h1>

                    <div class="offerCompany offerSpecificationsBox">Compañía: {{offer.offerCompany.companyName}}</div>
                    <div class="offerSpecificationsOriginalPrice offerSpecificationsBox" ng-show="offer.originalPrice">
                        Precio original: {{offer.originalPrice}}€
                    </div>
                    <div class="offerSpecificationsFinalPrice offerSpecificationsBox">
                        Precio de oferta: {{offer.finalPrice}}€
                    </div>
                </div>
                <div class="offerSpecificationsSubtitle">
                    <img ng-src="{{offer.offerUser.userAvatar}}"/>

                    <div class="offerOwner">Creado por: <i>{{offer.offerUser.userNickname}}</i></div>
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
                <a href="{{offer.offerLink}}" target="_blank">ir a la oferta <i
                        class="fa fa-cart-arrow-down"></i></a>
            </div>

            <div ng-offer-comments="offerComments" the-comments="offer.offerComments" the-offer="offer.id"
                 class="offerSpecificationsComments" comments-custom-close-callback="commentsCustomCloseCallback"></div>

        </div>
    </div>
</div>
