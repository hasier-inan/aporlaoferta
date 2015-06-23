/**
 * Created by hasiermetal on 22/06/15.
 */
aporlaofertaApp
    .directive('ngAccountUpdate', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/account/account-signup/createAccount.html',
            scope: {
                overheadDisplay: '='
            },
            link: function (scope, elem, attrs) {
            },
            controller: ['$rootScope','$scope', 'accountController', 'requestManager', 'configService', function ($rootScope,$scope, accountController, requestManager, configService) {
                $scope.disableNickname = true;
                $scope.theUser = {};
                $scope.createAccount = function (theUser) {
                    accountController.updateAccount(theUser);
                    //$scope.theUser = {};
                    $scope.overheadDisplay = false;
                }
                $scope.getUserDetails = function () {
                    //$scope.theUser=accountController.getUserDetails();
                    requestManager.makePostCall({}, {}, configService.getEndpoint('get.account.details'))
                        .success(function (data, status, headers, config) {
                            $scope.theUser = data;
                        }).error(function (data, status, headers, config) {
                            var theResponse = {};
                            theResponse.description = data;
                            theResponse.responseResult = "error";
                            $rootScope.$broadcast('serverResponse', theResponse);
                        });
                }
                $scope.getUserDetails();
            }]
        }
    });