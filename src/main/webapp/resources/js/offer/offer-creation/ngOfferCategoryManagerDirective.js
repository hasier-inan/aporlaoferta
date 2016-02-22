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
                selectedcategory: '='
            },
            controller: ['$rootScope', '$scope', 'requestManager', 'configService', function ($rootScope, $scope, requestManager, configService) {
                $scope.offerCategories = {};
                $scope.offerCategory = {
                    text: "CATEGORÍA"
                };
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
                    $scope.selectedcategory = category;
                }
                $scope.populateAllCategories();
                $scope.$watch('reset', function () {
                    if ($scope.reset) {
                        $scope.offerCategory = {
                            text: "CATEGORÍA"
                        };
                    }
                    $scope.reset = false;
                });
                $scope.$watch('offerCategory', function () {
                    $scope.selectedcategory = $scope.offerCategory.text;
                });
                $scope.isCategorySelected = function () {
                    return $scope.category != ""
                        && $scope.category != null
                        && $scope.category != undefined;
                };
            }]
        }
    });