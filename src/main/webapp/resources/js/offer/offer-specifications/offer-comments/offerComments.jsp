<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div style="background-color: #BBFFF0">
    <div>comments:</div>
    <div ng-repeat="theComment in theComments track by theComment.id">
        <div>
            {{theComment.commentOwner.userNickname}} - {{theComment.commentCreationDate | date:'MM/dd/yyyy @ h:mma'}}
        </div>
        <div style="background-color: #BBFFF1">
            <div ng-show="theComment.commentsQuotedComment">
                show quoted comment #{{theComment.commentsQuotedComment}}
            </div>
        </div>
        <div>
            {{theComment.commentText}}
        </div>
        <sec:authorize ifAllGranted="ROLE_USER">
            <div>
                <input type="text" ng-model="qComment.commentText"/>
                <button ng-click="quoteComment(qComment, theComment.id)">quote</button>
            </div>
        </sec:authorize>
    </div>
    <div>
        <sec:authorize ifAllGranted="ROLE_USER">
            <div>
                <input type="text" ng-model="comment.commentText"/>
                <button ng-click="writeComment(comment, theOffer)">add comment</button>
            </div>
        </sec:authorize>
        <sec:authorize ifNotGranted="ROLE_USER">
            <div>Login to write a comment</div>
        </sec:authorize>
    </div>
</div>
