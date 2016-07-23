/**
 * Created by hasiermetal on 30/06/15.
 */
aporlaofertaApp
    .directive('ngOfferFilter', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-filter/offerFilter.html',
            scope: {
                offerList: '=',
                selection: '=',
                offerFilter: '='
            },
            controller: ['$scope', '$rootScope', 'requestManager', 'configService', function ($scope, $rootScope, requestManager, configService) {
                $scope.filter = {};
                $scope.displayFilterContent = "";
                $scope.previousCategory = "all";

                $scope.requestFilterApply = function () {
                    $scope.processing = true;
                    $scope.filter.hot = $scope.selection === 'hottestOffers';
                    if (!$scope.isCategorySelected()) {
                        $scope.filter.selectedcategory = "";
                    }
                    $scope.offerFilter = angular.copy($scope.filter);
                    if ($scope.offerFilter.text.length < 3) {
                        $scope.offerFilter.text = "";
                    }
                    requestManager.makePostCall($scope.offerFilter, {}, configService.getEndpoint('get.filtered.offers'))
                        .success(function (data, status, headers, config) {
                            $scope.offerList = data.theOffers;
                        }).error(function (data, status, headers, config) {
                            var theResponse = {};
                            theResponse.description = data;
                            theResponse.responseResult = "error";
                            $rootScope.$broadcast('serverResponse', theResponse);
                        }).finally(function () {
                            $scope.processing = false;
                        });
                };

                $scope.searchCriteriaChanged = function () {
                    $scope.offerFilter = angular.copy($scope.filter);
                    if ($scope.filter.text.length > 2) {
                        $scope.requestFilterApply();
                    }
                };

                $scope.$watch('filter.selectedcategory', function () {
                    if ($scope.previousCategory !== $scope.filter.selectedcategory) {
                        $scope.previousCategory = $scope.filter.selectedcategory;
                        $scope.requestFilterApply();
                    }
                });

                $scope.cleanFilters = function () {
                    $scope.filter.expired = false;
                    $scope.filter.text = "";
                    $scope.resetCategory = true;
                    $scope.filter.selectedcategory = "";
//                    $scope.requestFilterApply();
                };

                $scope.displayFilterContents = function () {
                    $scope.displayFilterContent = ($scope.displayFilterContent == 'filters-displayed') ?
                        'filters-hidden' : 'filters-displayed';
                };
                $scope.isCategorySelected = function () {
                    return $scope.filter.selectedcategory != ""
                        && $scope.filter.selectedcategory != null
                        && $scope.filter.selectedcategory != undefined
                        && $scope.filter.selectedcategory != "CATEGORÍA";
                };
                $scope.cleanFilters();
            }]
        }
    });