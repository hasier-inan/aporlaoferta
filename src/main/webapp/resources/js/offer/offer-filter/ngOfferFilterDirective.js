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
                $scope.displayFilterContent = "";
                $scope.requestFilterApply = function () {
                    requestManager.makePostCall($scope.filter, {}, configService.getEndpoint('get.filtered.offers'))
                        .success(function (data, status, headers, config) {
                            $scope.offerList = data.theOffers;
                        }).error(function (data, status, headers, config) {
                            var theResponse = {};
                            theResponse.description = data;
                            theResponse.responseResult = "error";
                            $rootScope.$broadcast('serverResponse', theResponse);
                        });
                };
                $scope.cleanFilters = function () {
                    $scope.filter.expired = false;
                    $scope.filter.text = "";
                    $scope.resetCategory = true;
                    $scope.filter.selectedcategory = "";
                    $scope.requestFilterApply();
                };
                $scope.displayFilterContents = function () {
                    $scope.displayFilterContent = ($scope.displayFilterContent == 'filters-displayed') ?
                        'filters-hidden' : 'filters-displayed';
                };
                $scope.cleanFilters();
            }]
        }
    });