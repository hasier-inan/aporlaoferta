<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div>
    <div ng-repeat="offer in theOffer track by offer.id">
        <div class="offerSpecificationsContainer">

            <div class="offerSpecificationsLeftContainer containerSplitter">
                <div class="offerSpecificationsImage">
                    <!--<img ng-src="{{offer.offerImage}}"/>-->
                    <img src="resources/images/pic01.jpg"/>

                    <div class="offerSpecificationsFeedback">
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
                    <h1><a href="http://{{offer.offerLink}}" target="_blank">{{offer.offerTitle}}:
                        {{offer.finalPrice}}€</a></h1>

                    <div class="offerCompany offerSpecificationsBox">Compañía: {{offer.offerCompany.companyName}}</div>
                    <div class="offerSpecificationsOriginalPrice offerSpecificationsBox" ng-show="offer.originalPrice">
                        Precio original: {{offer.originalPrice}}€
                    </div>
                    <div class="offerSpecificationsFinalPrice offerSpecificationsBox">
                        Precio de oferta: {{offer.finalPrice}}€
                    </div>
                </div>
                <div class="offerSpecificationsSubtitle">
                    <div class="offerOwner">Creado por: <i>{{offer.offerUser.userNickname}}</i> el</div>
                    <div class="offerCreatedDate">el {{offer.offerCreatedDate | date:'MM/dd/yyyy @h:mma'}}
                    </div>
                </div>
                <div class="offerSpecificationsDescription offerDescription">
                    {{offer.offerDescription}}
                </div>
                <div class="offerSpecificationsLink">
                    <a href="http://{{offer.offerLink}}" target="_blank">ir a la oferta</a>
                </div>

            </div>
            <div ng-offer-comments="offerComments" the-comments="offer.offerComments" the-offer="offer.id"
                 class="offerSpecificationsComments"></div>

        </div>
    </div>
</div>
