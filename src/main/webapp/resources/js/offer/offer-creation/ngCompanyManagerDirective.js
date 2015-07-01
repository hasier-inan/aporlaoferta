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
                selectedcompany:'='
            },
            controller: ['$rootScope', '$scope', 'requestManager', 'configService', function ($rootScope, $scope, requestManager, configService) {
                $scope.populateCompanyList = function () {
                    $scope.offerCompanies = {};
                    requestManager.makePostCall({}, {}, configService.getEndpoint('get.companies'))
                        .success(function (data, status, headers, config) {
                            $scope.offerCompanies = angular.copy(data);
                        }).error(function (data, status, headers, config) {
                            var theResponse = {};
                            theResponse.description = data;
                            theResponse.responseResult = "error";
                            $rootScope.$broadcast('serverResponse', theResponse);
                        });
                }
                $scope.onCompanyChange = function (company) {
                    $scope.selectedcompany=company;
                }
                $scope.updateSelectedCompany = function (isSelected) {
                    if (!isSelected) {
                        $scope.onCompanyChange($scope.company);
                    }
                    else {
                        $scope.selectedcompany=$scope.offerCompany;
                    }
                }
                $scope.$watch('reset', function () {
                    if ($scope.reset) {
                        console.log($scope.offerCompanies);
                    }
                    $scope.reset = false;
                });
                $scope.$watch('offerCompany.companyName', function () {
                    $scope.selectedcompany=$scope.offerCompany;
                });
                $scope.$watch('offerCompany.companyUrl', function () {
                    $scope.selectedcompany=$scope.offerCompany;
                });
                $scope.populateCompanyList();
            }]
        }
    });