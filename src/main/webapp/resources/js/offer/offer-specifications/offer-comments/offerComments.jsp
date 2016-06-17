<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div>
    <sec:authorize ifAllGranted="ROLE_USER">

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
                            class="button small icon fa-pencil-square whiteButton green-background"
                            ng-click="writeComment(comment, theOffer)">a&ntilde;adir comentario
                    </button>
                    <button ng-disabled="!commentContentForm.$valid || processing"
                            class="button mini icon fa-pencil-square whiteButton inline green-background"
                            ng-click="writeComment(comment, theOffer)"></button>
                </div>
            </form>
        </div>
    </sec:authorize>
    <sec:authorize ifNotGranted="ROLE_USER">
        <div class="loginCommentMessage">Inicia sesión para añadir un comentario</div>
    </sec:authorize>
    <hr/>
</div>
<div class="commentsHeadTitle">Comentarios:</div>
<div ng-repeat="theComment in theComments track by theComment.id" class="commentBlock">
    <div class="commentsHeadOwner">
        {{theComment.commentOwner.userNickname}} - {{theComment.commentCreationDate | date:'MM/dd/yyyy @ h:mma'}}
    </div>
    <div class="theCommentAvatar commentAvatar" ng-if="theComment.commentOwner.userAvatar">
        <img ng-src="{{theComment.commentOwner.userAvatar}}"/>
    </div>
    <div class="commentsTheComment">
        <div ng-if="theComment.commentsQuotedComment" class="quotedContent">
            <div style="display: none">
                {{quotedComment=getQuotedComment(theComment.commentsQuotedComment, theComments)}}
            </div>
            <div class="commentsHeadOwner">
                {{quotedComment.commentOwner.userNickname}} - {{quotedComment.commentCreationDate | date:'MM/dd/yyyy @
                h:mma'}}
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
        <sec:authorize ifAllGranted="ROLE_USER">
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
                                    class="button small icon fa-pencil-square whiteButton green-background"
                                    ng-click="quoteComment(qComment, theComment.id)">citar
                            </button>
                            <button ng-disabled="!quoteCommentForm.$valid || processing"
                                    class="button mini icon fa-pencil-square whiteButton inline green-background"
                                    ng-click="quoteComment(qComment, theComment.id)"></button>
                        </div>
                    </form>
                </div>
            </div>
        </sec:authorize>

    </div>
</div>