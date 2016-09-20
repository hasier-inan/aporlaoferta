<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div>
    <sec:authorize access="isAuthenticated()">

        <div class="commentContent">
            <form name="commentContentForm">
                <textarea ng-model="comment.commentText"
                          class="overheadField ng-pristine ng-valid content-validation comment-textarea"
                          ng-maxlength="2000" rows="6"
                          placeholder="A&ntilde;ade un comentario (m&aacute;ximo 2000 car&aacute;cteres)"
                          required>
                </textarea>

                <div class="commentQuoteButtons">
                    <button ng-disabled="!commentContentForm.$valid || processing"
                            class="button small small--rounded icon hvr-icon-grow fa-pencil-square whiteButton green-background"
                            ng-click="writeComment(comment, theOffer)">a&ntilde;adir comentario
                    </button>
                    <button ng-disabled="!commentContentForm.$valid || processing"
                            class="button mini icon hvr-icon-grow fa-pencil-square whiteButton inline green-background write-comment"
                            ng-click="writeComment(comment, theOffer)"></button>
                </div>
            </form>
        </div>
    </sec:authorize>
    <sec:authorize access="isAnonymous()">
        <div class="loginCommentMessage">Inicia sesión para añadir un comentario</div>
    </sec:authorize>
</div>
<div class="commentsHeadTitle">Comentarios:</div>
<hr/>
<div ng-repeat="theComment in theComments track by theComment.id" class="commentBlock">
    <div class="commentsHeadOwner">
        <span ng-class="{'user-banned' : (!theComment.commentOwner.enabled)}">{{theComment.commentOwner.userNickname}}</span>
        <span ng-hide="theComment.commentOwner.enabled"> (Usuario expulsado) </span>
        <span>- {{theComment.commentCreationDate | date:'MM/dd/yyyy @ h:mma'}}</span>
    </div>
    <div class="theCommentAvatar commentAvatar" ng-if="theComment.commentOwner.userAvatar">
        <img ng-src="{{theComment.commentOwner.userAvatar}}"/>
        <sec:authorize ifAllGranted="ROLE_ADMIN">
            <div class="remove-owner" ng-show="theComment.commentOwner.enabled">
                <i class="fa fa-trash" aria-hidden="true"
                   ng-confirm-click="Confirmar expulsión del usuario {{theComment.commentOwner.userNickname}}"
                   confirmed-click="banUser(theComment.commentOwner.userNickname)"></i>
            </div>
        </sec:authorize>
    </div>
    <div class="commentsTheComment">
        <sec:authorize ifAllGranted="ROLE_ADMIN">
            <div class="remove-comment">
                <i class="fa fa-trash" aria-hidden="true"
                   ng-confirm-click="Confirmar borrado de comentario"
                   confirmed-click="deleteComment(theComment.id)"></i>
            </div>
        </sec:authorize>
        <div ng-if="theComment.commentsQuotedComment" class="quotedContent">
            <div style="display: none">
                {{quotedComment=getQuotedComment(theComment.commentsQuotedComment, theComments)}}
            </div>
            <div class="commentsHeadOwner">
                <span ng-class="{'user-banned' : (!quotedComment.commentOwner.enabled)}">{{quotedComment.commentOwner.userNickname}}</span>
                <span ng-hide="theComment.commentOwner.enabled"> (Usuario expulsado) </span>
                <span>- {{quotedComment.commentCreationDate | date:'MM/dd/yyyy @ h:mma'}}</span>
            </div>
            <div class="quotedCommentAvatar commentAvatar" ng-if="quotedComment.commentOwner.userAvatar">
                <img ng-src="{{quotedComment.commentOwner.userAvatar}}"/>
            </div>
            <div class="commentsTheComment quotedComment">
                <div class="commentText">
                    <pre class="parsed-text">{{quotedComment.commentText}}</pre>
                </div>

            </div>
            <!--<div ng-offer-comments-quotes="offerCommentsQuotes" the-comments="theComments"
                 quoted-comment="theComment.commentsQuotedComment"></div>-->
        </div>
        <div class="commentText">
            <pre class="parsed-text">{{theComment.commentText}}</pre>
        </div>
        <sec:authorize access="isAuthenticated()">
            <div class="commentQuoteCommentAction">
                <a href="#" class="quoteAction" ng-click="quoteAction(theComment.id)">citar:</a>

                <div class="commentQuoteCommentContent" ng-show="isQuoteActionEnabled(theComment.id)">
                    <form name="quoteCommentForm">
                        <textarea ng-model="qComment.commentText"
                                  class="overheadField ng-pristine ng-valid content-validation comment-textarea"
                                  ng-maxlength="2000"
                                  rows="6" required>
                        </textarea>

                        <div class="commentQuoteButtons">
                            <button ng-disabled="!quoteCommentForm.$valid || processing"
                                    class="button small small--rounded icon hvr-icon-grow fa-pencil-square whiteButton green-background"
                                    ng-click="quoteComment(qComment, theComment.id)">citar
                            </button>
                            <button ng-disabled="!quoteCommentForm.$valid || processing"
                                    class="button mini icon hvr-icon-grow fa-pencil-square whiteButton inline green-background write-comment"
                                    ng-click="quoteComment(qComment, theComment.id)"></button>
                        </div>
                    </form>
                </div>
            </div>
        </sec:authorize>

    </div>
</div>