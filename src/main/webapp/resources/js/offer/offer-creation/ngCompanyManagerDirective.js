/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngCompanyManager', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-creation/companyManagement.html',
            scope: {
                reset: '='
            },
            controller: ['$rootScope', '$scope', 'requestManager', 'configService', 'offerCreationService', function ($rootScope, $scope, requestManager, configService, offerCreationService) {
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
                    offerCreationService.setSelectedCompany(company);
                }
                $scope.updateSelectedCompany = function (isSelected) {
                    if (!isSelected) {
                        $scope.onCompanyChange($scope.company);
                    }
                    else {
                        offerCreationService.setSelectedCompany($scope.offerCompany);
                    }
                }
                $scope.$watch('reset', function () {
                    if ($scope.reset) {
                        console.log($scope.offerCompanies);
                    }
                    $scope.reset = false;
                });
                $scope.$watch('offerCompany.companyName', function () {
                    offerCreationService.setSelectedCompany($scope.offerCompany);
                });
                $scope.$watch('offerCompany.companyUrl', function () {
                    offerCreationService.setSelectedCompany($scope.offerCompany);
                });
                $scope.populateCompanyList();
            }]
        }
    });