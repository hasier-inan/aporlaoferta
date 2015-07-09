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
            controller: ['$rootScope','$scope', 'requestManager', 'configService', function ($rootScope,$scope,requestManager, configService) {
                $scope.votePositive = function (id) {
                    requestManager.makePostCall({}, {'offerId': id}, configService.getEndpoint('positive.feedback'))
                        .success(function (data, status, headers, config) {
                            $rootScope.$broadcast('serverResponse', data);
                        }).error(function (data, status, headers, config) {
                            var theResponse = {};
                            theResponse.description = data;
                            theResponse.responseResult = "error";
                            $rootScope.$broadcast('serverResponse', theResponse);
                        });
                }
                $scope.voteNegative = function (id) {
                    requestManager.makePostCall({}, {'offerId': id}, configService.getEndpoint('negative.feedback'))
                        .success(function (data, status, headers, config) {
                            $rootScope.$broadcast('serverResponse', data);
                        }).error(function (data, status, headers, config) {
                            var theResponse = {};
                            theResponse.description = data;
                            theResponse.responseResult = "error";
                            $rootScope.$broadcast('serverResponse', theResponse);
                        });
                }
            }]
        }
    });