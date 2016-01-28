/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngOfferList', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-list/offerList.html',
            link: function (scope, elem, attrs) {
            },
            controller: ['$scope', 'offerManager',
                function ($scope, offerManager) {
                    $scope.defaultList = "";
                    $scope.offerList = [];
                    $scope.moreOffersLoading = false;
                    $scope.requestNewestOffers = function () {
                        offerManager.requestNewestOffers();
                        $scope.defaultList = 'newestOffers';
                    }

                    $scope.requestHottestOffers = function () {
                        offerManager.requestHottestOffers();
                        $scope.defaultList = 'hottestOffers';
                    }

                    $scope.showMoreOffers = function (lastOffer) {
                        $scope.moreOffersLoading = true;
                        $scope.offerFilter.hot = $scope.defaultList === 'hottestOffers';
                        offerManager.requestMoreOffers($scope.offerFilter, lastOffer,
                            function (data) {
                                $scope.offerList = $scope.offerList.concat(data);
                                $scope.moreOffersLoading = false;
                            }, function () {
                                $scope.moreOffersLoading = false;
                            });
                    }
                    $scope.$on('offerList', function (event, args) {
                        $scope.offerList = args;
                    })

                    $scope.showSpecifications = function (id) {
                        offerManager.showSpecifications(id);
                    }
                    $scope.initialiseScrollyButtons = function () {
                        $('.scrolly')
                            .scrolly({
                                speed: 1500
                            });
                    }
                    $scope.initialiseScrollyButtons();
                }]
        }
    });