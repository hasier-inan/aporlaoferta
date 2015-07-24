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
                $scope.votePositive = function (id) {
                    requestManager.makePostCall({}, {'offerId': id}, configService.getEndpoint('positive.feedback'))
                        .success(function (data, status, headers, config) {
                            alertService.sendErrorMessage(data.descriptionEsp);
                            $scope.customCloseCallback = function () {
                                offerManager.showSpecifications(id);
                            }
                        }).error(function (data, status, headers, config) {
                            alertService.sendDefaultErrorMessage();
                            offerManager.showSpecifications(id);
                        });
                }
                $scope.voteNegative = function (id) {
                    requestManager.makePostCall({}, {'offerId': id}, configService.getEndpoint('negative.feedback'))
                        .success(function (data, status, headers, config) {
                            alertService.sendErrorMessage(data.descriptionEsp);
                            offerManager.showSpecifications(id);
                        }).error(function (data, status, headers, config) {
                            alertService.sendDefaultErrorMessage();
                            offerManager.showSpecifications(id);
                        });
                }
            }]
        }
    });