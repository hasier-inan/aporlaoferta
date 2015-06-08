/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngOfferList', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/templates/offerList.html',
            scope: {
                jsnArt: '='
            },
            link: function (scope, elem, attrs) {

            },
            controller:['$scope', '$timeout', 'offerController', function ($scope, $timeout, offerController) {
                $scope.requestNewestOffers = function () {
                    offerController.getNewestOffers();
                }

                $scope.requestHottestOffers = function () {
                    offerController.getHottestOffers();
                }
            }]
        }
    });