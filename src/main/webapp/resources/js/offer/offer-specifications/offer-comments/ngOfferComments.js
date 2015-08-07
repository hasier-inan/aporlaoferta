/**
 * Created by hasiermetal on 11/06/15.
 */
aporlaofertaApp
    .directive('ngOfferComments', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-specifications/offer-comments/' +
                'offerComments.jsp',
            scope: {
                theComments: '=',
                theOffer: '=',
                commentsCustomCloseCallback: '='
            },
            controller: ['$filter', 'alertService', '$scope', '$rootScope', 'requestManager', 'configService', 'offerManager',
                function ($filter, alertService, $scope, $rootScope, requestManager, configService, offerManager) {
                    $scope.quoteActionEnable = -1;
                    $scope.qComment = {};
                    $scope.comment = {};
                    $scope.getQuotedComment = function (id, comments) {
                        return $filter('filter')(comments, {id: id})[0];
                    }
                    $scope.quoteAction = function (id) {
                        if ($scope.quoteActionEnable == id) {
                            $scope.quoteActionEnable = -1;
                        }
                        else {
                            $scope.quoteActionEnable = id;
                        }
                    }
                    $scope.isQuoteActionEnabled = function (id) {
                        return $scope.quoteActionEnable == id;
                    }
                    $scope.writeComment = function (comment, id) {
                        requestManager.makePostCall(comment, {'offer': id}, configService.getEndpoint('create.comment'))
                            .success(function (data, status, headers, config) {
                                $scope.sendMessageAndShowSpecifications(data.descriptionEsp);
                            }).error(function (data, status, headers, config) {
                                $scope.sendDefaultErrorMessageAndShowSpecifications();
                            });
                    };
                    $scope.quoteComment = function (comment, id) {
                        requestManager.makePostCall(comment, {'quotedComment': id}, configService.getEndpoint('quote.comment'))
                            .success(function (data, status, headers, config) {
                                $scope.sendMessageAndShowSpecifications(data.descriptionEsp);
                            }).error(function (data, status, headers, config) {
                                $scope.sendDefaultErrorMessageAndShowSpecifications();
                            });
                    }

                    $scope.sendMessageAndShowSpecifications = function (message) {
                        //alertService.sendErrorMessage(message);
                        //$scope.updateViewAndCloseCallback();
                        $scope.restartCommentTexts();
                        offerManager.showSpecifications($scope.theOffer);
                    }
                    $scope.sendDefaultErrorMessageAndShowSpecifications = function () {
                        alertService.sendDefaultErrorMessage();
                        $scope.updateViewAndCloseCallback();
                    }
                    $scope.updateViewAndCloseCallback = function () {
                        $scope.commentsCustomCloseCallback = function () {
                            offerManager.showSpecifications($scope.theOffer);
                        }
                        $rootScope.$broadcast('commentsCustomCloseCallback', $scope.commentsCustomCloseCallback);
                        $scope.restartCommentTexts();
                    }
                    $scope.restartCommentTexts = function () {
                        $scope.comment.commentText = "";
                        $scope.qComment.commentText = "";
                    }

                }
            ]
        }
    })
;