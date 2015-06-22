/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngOfferCategoryManager', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-creation/offerCategoryManagement.html',
            link: function (scope, elem, attrs) {
            },
            controller: ['$rootScope','$scope', 'requestManager', 'configService', 'offerCreationService', function ($rootScope,$scope, requestManager, configService, offerCreationService) {
                $scope.populateAllCategories = function () {
                    requestManager.makePostCall({}, {}, configService.getEndpoint('get.offer.categories'))
                        .success(function (data, status, headers, config) {
                            $scope.offerCategories = angular.copy(data);
                        }).error(function (data, status, headers, config) {
                            var theResponse = {};
                            theResponse.description = data;
                            theResponse.responseResult = "error";
                            $rootScope.$broadcast('serverResponse', theResponse);
                        });
                }
                $scope.onCategoryChange = function (category) {
                    offerCreationService.setSelectedCategory(category);
                }
                $scope.populateAllCategories();
            }]
        }
    });