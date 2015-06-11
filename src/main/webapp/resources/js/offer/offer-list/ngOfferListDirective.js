/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngOfferList', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-list/offerList.html',
            link: function (scope, elem, attrs) {},
            controller:['$scope', '$timeout', 'offerController', function ($scope, $timeout, offerController) {
                $scope.requestNewestOffers = function () {
                    var result=offerController.getNewestOffers();
                    //$scope.offerList=result.theOffers;
                }

                $scope.requestHottestOffers = function () {
                    var result=offerController.getHottestOffers();
                    //$scope.offerList=result.theOffers;
                }
            }]
        }
    });