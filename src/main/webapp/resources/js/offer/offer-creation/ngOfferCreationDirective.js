/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngOfferCreation', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-creation/offerCreation.html',
            controller: ['$scope', 'offerController', 'offerCreationService', function ($scope, offerController, offerCreationService) {
                $scope.offer = {};
                $scope.createOffer = function () {
                    var result = offerController.createOffer($scope.offer);
                };
                $scope.$on('selectedCompany', function (event, args) {
                    var selectedCompany = args.company;
                    $scope.offer.offerCompany = selectedCompany;
                });
                $scope.$on('selectedCategory', function (event, args) {
                    $scope.offer.offerCategory = args.category;
                });
            }]
        }
    });