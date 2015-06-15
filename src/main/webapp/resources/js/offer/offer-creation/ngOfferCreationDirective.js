/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngOfferCreation', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-creation/offerCreation.html',
            controller: ['$scope', 'requestManager', 'configService', 'offerCreationService', function ($scope, requestManager, configService, offerCreationService) {
                $scope.offer = {};
                $scope.createOffer = function () {
                    alert("should do a validation process before...");
                    requestManager.makePostCall($scope.offer, {}, configService.getEndpoint('create.offer'))
                        .success(function (data, status, headers, config) {
                            return data;
                        }).error(function (data, status, headers, config) {
                            //TODO: handle error;
                            alert("handle this error while retrieving data from newest offers");
                        });
                };
                $scope.bigDecimalsOnly=/^\-?\d+((\.|\,)\d+)?$/;
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