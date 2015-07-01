/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngOfferCreation', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-creation/offerCreation.html',
            scope: {
                overheadDisplay: '='
            },
            controller: ['$rootScope', '$scope', 'requestManager', 'configService', function ($rootScope, $scope, requestManager, configService) {
                $scope.offer = {};
                $scope.resetCategory = true;
                $scope.createOffer = function () {
                    requestManager.makePostCall($scope.offer, {}, configService.getEndpoint('create.offer'))
                        .success(function (data, status, headers, config) {
                            $rootScope.$broadcast('serverResponse', data);
                        }).error(function (data, status, headers, config) {
                            var theResponse = {};
                            theResponse.description = data;
                            theResponse.responseResult = "error";
                            $rootScope.$broadcast('serverResponse', theResponse);
                        });
                    $scope.overheadDisplay = false;
                    $scope.resetValues();

                };
                $scope.bigDecimalsOnly = /^\-?\d+((\.|\,)\d+)?$/;
                $scope.resetValues = function () {
                    $scope.offer = {};
                    $scope.brandNewCompany = false;
                    $scope.resetCategory = true;
                    $scope.resetCompany=true;

                }
            }]
        }
    });