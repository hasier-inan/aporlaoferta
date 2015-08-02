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
                    $scope.requestNewestOffers = function () {
                        offerManager.requestNewestOffers();
                    }

                    $scope.requestHottestOffers = function () {
                        offerManager.requestHottestOffers();
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