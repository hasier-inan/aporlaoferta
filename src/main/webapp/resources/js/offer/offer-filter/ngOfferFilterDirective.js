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
            controller: ['$scope', '$rootScope', 'requestManager', 'configService', 'offerHelper', '$cookies',
                function ($scope, $rootScope, requestManager, configService, offerHelper, $cookies) {
                    $scope.filter = {};
                    $scope.displayFilterContent = "";
                    $scope.previousCategory = "";
                    $scope.offerFilterContainer = "offerFilterContainer";
                    $scope.filterBy = "filter-by";
                    $scope.filterByExpiredCheckbox = "filterByExpiredCheckbox";

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
                        $rootScope.$broadcast('appliedOfferFilters', angular.copy($scope.offerFilter));
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

                    $scope.$watch('filter.dateRange', function () {
                        if($scope.filter.dateRange){
                            $scope.setDateRange($scope.filter.dateRange);
                        }
                        $scope.requestFilterApply();
                    });

                    $scope.searchCriteriaChanged = function () {
                        $scope.offerFilter = angular.copy($scope.filter);
                        if ($scope.filter.text.length > 2 || $scope.filter.text.length == 0) {
                            $scope.requestFilterApply();
                        }
                    };

                    $scope.$watch('filter.selectedcategory', function () {
                        if (!(offerHelper.isEmptyCategory($scope.previousCategory) &&
                            offerHelper.isEmptyCategory($scope.filter.selectedcategory)) &&
                            $scope.previousCategory !== $scope.filter.selectedcategory) {
                            $scope.previousCategory = $scope.filter.selectedcategory;
                            $scope.requestFilterApply();
                        }
                    });

                    $scope.cleanFilters = function () {
                        $scope.filter.expired = false;
                        $scope.filter.text = "";
                        $scope.resetCategory = true;
                        $scope.filter.selectedcategory = "";
                        $scope.filter.selectedcategory = "";
                        $scope.filter.dateRange = $scope.defaultDateRange();
//                    $scope.requestFilterApply();
                        $rootScope.$broadcast('appliedOfferFilters', angular.copy($scope.offerFilter));
                    };

                    $scope.displayFilterContents = function () {
                        $scope.displayFilterContent = ($scope.displayFilterContent == 'filters-displayed') ?
                            'filters-hidden' : 'filters-displayed';
                    };
                    $scope.isCategorySelected = function () {
                        return !offerHelper.isEmptyCategory($scope.filter.selectedcategory);
                    };

                    $scope.defaultDateRange = function () {
                        var preAssigned = parseInt($cookies.get(configService.getEndpoint('daterange.cookie')));
                        return preAssigned>=0 ? preAssigned : 2;
                    }

                    $scope.setDateRange = function(dateRange){
                        var expireDate = new Date();
                        expireDate.setDate(expireDate.getDate() + 365);
                        $cookies.put(configService.getEndpoint('daterange.cookie'), dateRange, {'expires': expireDate});
                    }

                    $scope.cleanFilters();
                }]
        }
    });