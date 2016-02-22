/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngCompanyManager', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-creation/companyManagement.html',
            scope: {
                reset: '=',
                selectedcompany: '='
            },
            controller: ['$rootScope', '$scope', 'requestManager', 'configService', function ($rootScope, $scope, requestManager, configService) {
                $scope.offerCompanies = {};

                $scope.populateCompanyList = function () {
                    requestManager.makePostCall({}, {}, configService.getEndpoint('get.companies'))
                        .success(function (data, status, headers, config) {
                            $scope.offerCompanies = angular.copy(data);
                        }).error(function (data, status, headers, config) {
                            var theResponse = {};
                            theResponse.description = data;
                            theResponse.responseResult = "error";
                            $rootScope.$broadcast('serverResponse', theResponse);
                        });
                };

                $scope.searchCompanyChange = function (companyName) {
                    if (companyName != "") {
                        $scope.selectedcompany = {companyName: companyName}
                    }
                }
                $scope.selectedItemChange = function (company) {
                    $scope.selectedcompany = company
                }

                $scope.$watch('reset', function () {
                    if ($scope.reset) {
                        $scope.selectedcompany = "";
                        $scope.searchCompany = "";
                    }
                    $scope.reset = false;
                });

                $scope.querySearch = function (company) {
                    if ($scope.offerCompanies.length > 0) {
                        return company ? $scope.offerCompanies.filter($scope.createFilterFor(company)) : $scope.offerCompanies;
                    }
                };

                $scope.createFilterFor = function (companyName) {
                    var lowercaseQuery = angular.lowercase(companyName);
                    return function filterFn(company) {
                        return (angular.lowercase(company.companyName)
                            .indexOf(lowercaseQuery) === 0);
                    };
                };

                $scope.populateCompanyList();
            }]
        }
    });