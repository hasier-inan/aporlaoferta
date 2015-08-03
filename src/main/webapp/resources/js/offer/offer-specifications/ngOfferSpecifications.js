/**
 * Created by hasiermetal on 11/06/15.
 */
aporlaofertaApp
    .directive('ngOfferSpecifications', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-specifications/offerSpecifications.jsp',
            scope: {
                theOffer: '=',
                customCloseCallback: '='
            },
            controller: ['offerManager', 'alertService', '$scope', 'requestManager', 'configService', function (offerManager, alertService, $scope, requestManager, configService) {
                $scope.commentsCustomCloseCallback={};
                $scope.votePositive = function (id) {
                    requestManager.makePostCall({}, {'offerId': id}, configService.getEndpoint('positive.feedback'))
                        .success(function (data, status, headers, config) {
                            $scope.sendMessageAndShowSpecifications(data.descriptionEsp, id);
                        }).error(function (data, status, headers, config) {
                            $scope.sendDefaultErrorMessageAndShowSpecifications(id);
                        });
                }
                $scope.voteNegative = function (id) {
                    requestManager.makePostCall({}, {'offerId': id}, configService.getEndpoint('negative.feedback'))
                        .success(function (data, status, headers, config) {
                            $scope.sendMessageAndShowSpecifications(data.descriptionEsp, id);
                        }).error(function (data, status, headers, config) {
                            $scope.sendDefaultErrorMessageAndShowSpecifications(id);
                        });
                }
                $scope.sendMessageAndShowSpecifications = function (message, id) {
                    alertService.sendErrorMessage(message);
                    $scope.customCloseCallback = function () {
                        offerManager.showSpecifications(id);
                    }
                }
                $scope.sendDefaultErrorMessageAndShowSpecifications = function (id) {
                    alertService.sendDefaultErrorMessage();
                    $scope.customCloseCallback = function () {
                        offerManager.showSpecifications(id);
                    }
                }
                $scope.$on('commentsCustomCloseCallback', function (event, args) {
                    var customCallback = args;
                    $scope.customCloseCallback=customCallback;
                });

            }]
        }
    });