/**
 * Created by hasiermetal on 26/06/15.
 */
aporlaofertaApp
    .directive('ngOfferCommentsQuotes', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-specifications/offer-comments/' +
                'commentsQuotes.jsp',
            scope: {
                theComments: '=',
                quotedComment: '='
            },
            controller: ['$rootScope', '$scope', 'requestManager', 'configService', function ($rootScope, $scope, requestManager, configService) {
                $scope.quoteActionEnable = -1;
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
                $scope.quoteComment = function (comment, id) {
                    requestManager.makePostCall(comment, {'quotedComment': id}, configService.getEndpoint('quote.comment'))
                        .success(function (data, status, headers, config) {
                            $rootScope.$broadcast('serverResponse', data);
                            $scope.qComment.commentText="";
                        }).error(function (data, status, headers, config) {
                            var theResponse = {};
                            theResponse.description = data;
                            theResponse.responseResult = "error";
                            $rootScope.$broadcast('serverResponse', theResponse);
                        });
                };

            }]
        }
    });