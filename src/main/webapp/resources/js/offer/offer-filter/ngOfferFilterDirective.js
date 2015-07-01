/**
 * Created by hasiermetal on 30/06/15.
 */
aporlaofertaApp
    .directive('ngOfferFilter', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-filter/offerFilter.html',
            scope: {
                offerList: '='
            },
            controller: ['$scope', '$rootScope', 'requestManager', 'configService', function ($scope, $rootScope, requestManager, configService) {
                $scope.filter = {};
                $scope.filter.selectedcategory = "puta";
                $scope.requestFilterApply = function () {
                    console.log($scope.filter);
//                    requestManager.makePostCall({}, {'filters': $scope.filter}, configService.getEndpoint('get.filtered.offers'))
//                        .success(function (data, status, headers, config) {
//                            $scope.offerList = data.theOffers;
//                        }).error(function (data, status, headers, config) {
//                            var theResponse = {};
//                            theResponse.description = data;
//                            theResponse.responseResult = "error";
//                            $rootScope.$broadcast('serverResponse', theResponse);
//                        });
                };
                $scope.cleanFilters = function () {
                    $scope.filter.filterWithExpired = false;
                    $scope.filter.offerTextFilter = "";
                    $scope.filter.resetCategory = true;
                };
                $scope.cleanFilters();
            }]
        }
    });