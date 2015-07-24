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
                theOffer: '='
            },
            controller: ['$filter', 'alertService', '$scope', '$timeout', 'requestManager', 'configService', function ($filter, alertService, $scope, $timeout, requestManager, configService) {
                $scope.quoteActionEnable = -1;
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
                            alertService.sendErrorMessage(data.descriptionEsp);
                            $scope.comment.commentText = "";
                        }).error(function (data, status, headers, config) {
                            alertService.sendDefaultErrorMessage();
                            $scope.comment.commentText = "";
                        });
                };
                $scope.quoteComment = function (comment, id) {
                    requestManager.makePostCall(comment, {'quotedComment': id}, configService.getEndpoint('quote.comment'))
                        .success(function (data, status, headers, config) {
                            alertService.sendErrorMessage(data.descriptionEsp);
                            $scope.qComment.commentText = "";
                        }).error(function (data, status, headers, config) {
                            alertService.sendDefaultErrorMessage();
                            $scope.qComment.commentText = "";
                        });
                }

            }
            ]
        }
    })
;