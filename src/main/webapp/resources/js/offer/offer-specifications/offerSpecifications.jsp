<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div>
    <div ng-repeat="offer in theOffer track by offer.id">
        <div style="background-color: #BBFFFF">
            <sec:authorize ifAllGranted="ROLE_USER">
                <button ng-click="votePositive(offer.id)">+</button>
                <button ng-click="voteNegative(offer.id)">-</button>
            </sec:authorize>
            <sec:authorize ifNotGranted="ROLE_USER">
                <div>Login to give your feedback</div>
            </sec:authorize>

            <div>
                {{offer.offerTitle}} - {{offer.finalPrice}}eur [+{{offer.offerPositiveVote}} |
                +{{offer.offerNegativeVote}}]
            </div>
            <div>
                {{offer.offerDescription}} - Image to be injected:{{offer.offerImage}}
            </div>
            <div>
                <a href="http://{{offer.offerLink}}">go to deal</a>
            </div>
            <div ng-offer-comments="offerComments" the-comments="offer.offerComments" the-offer="offer.id"></div>
        </div>
    </div>
</div>
