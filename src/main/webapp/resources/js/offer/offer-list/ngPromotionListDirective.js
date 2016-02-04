/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngPromotionList', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-list/promotionList.html',
            controller: ['$scope','offerManager',
                function ($scope,offerManager) {
                    $scope.defaultList = "";

                    $scope.requestNewestOffers = function () {
                        offerManager.requestNewestOffers();
                        $scope.defaultList = 'newestOffers';
                    }

                    $scope.requestHottestOffers = function () {
                        offerManager.requestHottestOffers();
                        $scope.defaultList = 'hottestOffers';
                    }
                }]
        }
    });