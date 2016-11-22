/**
 * Created by hasiermetal on 30/06/15.
 */
aporlaofertaApp
    .directive('ngDummyOfferFilter', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-filter/offerFilter.html',
            controller: ['$scope', function ($scope) {
                $scope.filter = {};
                $scope.offerFilterContainer = "dummyFilter";
                $scope.filterBy = "dummyFilterBy";
                $scope.filterByExpiredCheckbox = "dummyFilterByExpiredCheckbox";
                $scope.cleanFilters = function () {
                    $scope.filter.expired = false;
                    $scope.filter.text = "";
                    $scope.resetCategory = true;
                    $scope.filter.dateRange = 1;
                };

                $scope.displayFilterContents = function () {
                    $scope.displayFilterContent = ($scope.displayFilterContent == 'filters-displayed') ?
                        'filters-hidden' : 'filters-displayed';
                };

                $scope.cleanFilters();
            }]
        }
    });