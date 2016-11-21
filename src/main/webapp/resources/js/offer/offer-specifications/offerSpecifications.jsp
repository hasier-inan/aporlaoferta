<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div>
    <div ng-repeat="offer in theOffer track by offer.id">
        <div class="offerSpecificationsContainer">
            <sec:authorize access="isAuthenticated()">
                <sec:authentication var="nickname" property="principal.username"/>
                <div class="offer-specifications edit-offer"
                     ng-show="offer.offerUser.userNickname == '${nickname}'">
                    <button class="button small small--rounded icon hvr-icon-grow fa-pencil whiteButton blue-border"
                            ng-click="updateOffer(offer)">
                        Editar oferta
                    </button>
                    <button class="button mini icon hvr-icon-grow fa-pencil whiteButton blue-border"
                            ng-click="updateOffer(offer)">
                    </button>
                </div>
                <div class="offer-specifications edit-offer expire-offer"
                     ng-show="offer.offerUser.userNickname == '${nickname}'">
                    <button ng-disabled="offer.offerExpired"
                            class="button small small--rounded icon hvr-icon-grow fa-trash whiteButton blue-border"
                            confirmed-click="expireOffer(offer)"
                            ng-confirm-click="¿Seguro que quieres caducar la oferta?">
                        Oferta Caducada
                    </button>
                    <button ng-disabled="offer.offerExpired"
                            class="button mini icon hvr-icon-grow fa-trash whiteButton blue-border"
                            confirmed-click="expireOffer(offer)"
                            ng-confirm-click="¿Seguro que quieres caducar la oferta?">
                    </button>
                </div>
                <sec:authorize ifAllGranted="ROLE_ADMIN">
                    <div class="offer-specifications edit-offer"
                         ng-show="offer.offerUser.userNickname != '${nickname}'">
                        <button class="button small small--rounded icon hvr-icon-grow fa-pencil whiteButton blue-border"
                                ng-click="updateOffer(offer)">
                            Editar oferta
                        </button>
                        <button class="button mini icon hvr-icon-grow fa-pencil whiteButton blue-border"
                                ng-click="updateOffer(offer)">
                        </button>
                    </div>
                    <div class="offer-specifications edit-offer expire-offer"
                         ng-show="offer.offerUser.userNickname != '${nickname}'">
                        <button ng-disabled="offer.offerExpired"
                                class="button small small--rounded  icon hvr-icon-grow fa-trash whiteButton blue-border"
                                confirmed-click="expireOffer(offer)"
                                ng-confirm-click="¿Seguro que quieres caducar la oferta?">
                            Oferta Caducada
                        </button>
                        <button ng-disabled="offer.offerExpired"
                                class="button mini icon hvr-icon-grow fa-trash whiteButton blue-border"
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
                    <div class="offer-temperature" ng-class="offer.offerExpired?'offer-expired':''">
                        <span class="offer-thermometer offer-thermometer--big"
                              ng-class="offerFeedbackStyle(offer)"></span>
                    </div>
                    <img src="{{offer.offerImage}}" ng-class="offer.offerExpired?'offer-expired':''"/>

                    <div class=" offerSpecificationsFeedback">
                        <div class="offerSpecificationsFeedbackTotal">

                            <div class="feedbackSumatory">
                                <label class="feedbackSumatory__temperature">
                                    <input type="checkbox" ng-model="specificFeedback">
                                    <span class="feedbackTemperature">{{offer.offerPositiveVote - offer.offerNegativeVote |
                                        number:0}}º</span>
                                </label>

                                <div class="offerSpecificationsFeedbackSmall" ng-show="specificFeedback">
                                    <div id="offerPositiveFeedback">+{{offer.offerPositiveVote}}</div>
                                    <div id="offerNegativeFeedback">-{{offer.offerNegativeVote}}</div>
                                </div>
                            </div>
                            <sec:authorize access="isAnonymous()">
                                <div class="loginFeedbackMessage"><span ng-click="processLogin()">Identifícate</span> o
                                    <span ng-click="processRegister()">Regístrate</span> para votar esta oferta
                                </div>
                            </sec:authorize>
                        </div>
                        <sec:authorize access="isAuthenticated()">
                            <div class="feedbackButtons">
                                <button class="button small icon whiteButton positive"
                                        ng-click="votePositive(offer.id)">+
                                </button>
                                <button class="button small icon whiteButton negative"
                                        ng-click="voteNegative(offer.id)">-
                                </button>
                            </div>
                        </sec:authorize>

                    </div>
                </div>

            </div>
            <div class="offerSpecificationsRightContainer containerSplitter">
                <div class="offerSpecificationsTitle">
                    <h2 ng-class="offer.offerExpired?'offer-expired':''">
                        <a href="{{offer.offerLink}}" target="_blank">
                            <span>{{offer.offerTitle}}: </span>
                            <span ng-if="offer.finalPrice">{{parsePrice(offer.finalPrice)}}€</span>
                            <span ng-if="offer.finalPrice==0">Gratis</span>
                        </a>
                    </h2>

                    <div class="offerCompany offerSpecificationsBox"><i class="fa fa-tag" aria-hidden="true"></i>{{offer.offerCompany.companyName}}
                    </div>
                    <div class="offerSpecificationsOriginalPrice offerSpecificationsBox" ng-show="offer.originalPrice">
                        {{parsePrice(offer.originalPrice)}}€
                    </div>
                    <div class="offerSpecificationsFinalPrice offerSpecificationsBox">
                        {{parsePrice(offer.finalPrice)}}€
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
                <a href="{{offer.offerLink}}" target="_blank"
                   class="icon medium--rounded hvr-icon-grow hot-offers"
                   ng-class="offer.offerExpired?'offer-expired':''">
                    <div class="main-logo"></div>
                    <div class="main-logo__cta">¡A por la oferta!</div>
                </a>
            </div>

            <div ng-offer-comments="offerComments" the-comments="offer.offerComments" the-offer="offer.id"
                 class="offerSpecificationsComments" comments-custom-close-callback="commentsCustomCloseCallback"></div>

        </div>
    </div>
</div>
