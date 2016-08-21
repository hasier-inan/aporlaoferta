/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngPromotionList', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-list/promotionList.html',
            controller: ['$scope', '$rootScope', 'offerManager',
                function ($scope, $rootScope, offerManager) {
                    $scope.shareUrl = "www.aporlaoferta.com";
                    $scope.shareText = $("meta[property='og:description']").attr("content");
                    $scope.defaultList = "";
                    $scope.appliedOfferFilters = {};

                    $scope.$on('appliedOfferFilters', function (event, args) {
                        $scope.appliedOfferFilters = args;
                    });

                    $scope.requestNewestOffers = function () {
                        offerManager.requestNewestOffers($scope.appliedOfferFilters);
                        $scope.defaultList = 'newestOffers';
                    }

                    $scope.requestHottestOffers = function () {
                        offerManager.requestHottestOffers($scope.appliedOfferFilters);
                        $scope.defaultList = 'hottestOffers';
                    }

                    $scope.displayTutorialModal = function () {
                        $rootScope.$broadcast('displayTutorial');
                    }
                }]
        }
    });