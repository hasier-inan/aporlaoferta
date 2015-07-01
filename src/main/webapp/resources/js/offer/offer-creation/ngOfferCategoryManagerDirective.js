/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngOfferCategoryManager', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-creation/offerCategoryManagement.html',
            scope: {
                reset: '=',
                selectedcategory:'='
            },
            controller: ['$rootScope', '$scope', 'requestManager', 'configService', 'offerCreationService', function ($rootScope, $scope, requestManager, configService, offerCreationService) {
                $scope.offerCategories = {};
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
                    //todo: use selected category in scope {...} to get value into other/parent scope...?
                    $scope.selectedcategory=category;
                    offerCreationService.setSelectedCategory(category);
                }
                $scope.populateAllCategories();
                $scope.$watch('reset', function () {
                    if ($scope.reset) {
                        $scope.category = "";
                    }
                    $scope.reset = false;
                });

            }]
        }
    });