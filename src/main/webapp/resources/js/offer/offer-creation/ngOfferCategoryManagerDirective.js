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
                selectedcategory: '=',
                includeAll: '='
            },
            controller: ['$rootScope', '$scope', 'requestManager', 'configService', function ($rootScope, $scope, requestManager, configService) {
                $scope.offerCategories = {};
                $scope.offerCategory = {
                    text: "CATEGORÍA",
                    display: "Categoría"
                };
                $scope.populateAllCategories = function () {
                    requestManager.makePostCall({}, {}, configService.getEndpoint('get.offer.categories'))
                        .success(function (data) {
                            $scope.offerCategories = angular.copy(data);
                            if ($scope.includeAll) {
                                $scope.offerCategories.push({text: "TODAS", value: "", display: "Todas"})
                            }
                        }).error(function (data) {
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
                            text: "CATEGORÍA",
                            display: "Categoría"
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