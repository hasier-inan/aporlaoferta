<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="commentsHeadTitle">comentarios:</div>
<div ng-repeat="theComment in theComments track by theComment.id">
    <div class="commentsHeadOwner">
        {{theComment.commentOwner.userNickname}} - {{theComment.commentCreationDate | date:'MM/dd/yyyy @ h:mma'}}
    </div>
    <div>
        <div ng-show="theComment.commentsQuotedComment">
            show quoted comment for #{{theComment.commentsQuotedComment}}
        </div>
    </div>
    <div class="commentsTheComment">
        {{theComment.commentText}}
        <sec:authorize ifAllGranted="ROLE_USER">
            <div class="commentQuoteCommentAction">
                <a href="#" class="quoteAction" ng-click="quoteAction(theComment.id)">quote:</a>

                <div class="commentQuoteCommentContent" ng-show="isQuoteActionEnabled(theComment.id)">
                    <textarea ng-model="qComment.commentText"
                              class="overheadField ng-pristine ng-valid" rows="6">
                    </textarea>

                    <div class="commentQuoteButtons">
                        <button class="button small icon fa-pencil-square whiteButton green-background"
                                ng-click="quoteComment(qComment, theComment.id)">quote
                        </button>
                        <button class="button mini icon fa-pencil-square whiteButton inline green-background"
                                ng-click="quoteComment(qComment, theComment.id)"></button>
                    </div>
                </div>
            </div>
        </sec:authorize>

    </div>
</div>
<div>
    <hr/>
    <sec:authorize ifAllGranted="ROLE_USER">

        <div>
            <textarea ng-model="comment.commentText"
                      class="overheadField ng-pristine ng-valid" rows="6">
            </textarea>
            <button ng-click="writeComment(comment, theOffer)">add comment</button>
        </div>
    </sec:authorize>
    <sec:authorize ifNotGranted="ROLE_USER">
        <div class="loginCommentMessage">Inicia sesión para añadir un comentario</div>
    </sec:authorize>
</div>