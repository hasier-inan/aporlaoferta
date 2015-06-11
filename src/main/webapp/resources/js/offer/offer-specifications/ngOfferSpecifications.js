/**
 * Created by hasiermetal on 11/06/15.
 */
aporlaofertaApp
    .directive('ngOfferSpecifications', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-specifications/offerSpecifications.jsp',
            scope: {
                theOffer: '='
            },
            controller: ['$scope', '$timeout', 'requestManager', 'configService', function ($scope, $timeout, requestManager, configService) {
                $scope.votePositive = function (id) {
                    requestManager.makePostCall({}, {'offerId': id}, configService.getEndpoint('positive.feedback'))
                        .success(function (data, status, headers, config) {

                        }).error(function (data, status, headers, config) {
                            //TODO: handle error;
                            alert("handle this error while retrieving data from newest offers");
                        });
                }
                $scope.voteNegative = function (id) {
                    requestManager.makePostCall({}, {'offerId': id}, configService.getEndpoint('negative.feedback'))
                        .success(function (data, status, headers, config) {
                        }).error(function (data, status, headers, config) {
                            //TODO: handle error;
                            alert("handle this error while retrieving data from newest offers");
                        });
                }
            }]
        }
    });