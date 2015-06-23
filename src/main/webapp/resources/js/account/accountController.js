var accountControllerModule = angular.module('accountController', []);

accountControllerModule.factory('accountController', ['$rootScope','$http','requestManager','configService',
    function ($rootScope, $http,requestManager,configService) {
        var createAccountService = {};
        createAccountService.createAccount = function (theUser) {
            requestManager.makePostCall(theUser, {}, configService.getEndpoint('create.account'))
                .success(function (data, status, headers, config) {
                    $rootScope.$broadcast('serverResponse', data);
                }).error(function (data, status, headers, config) {
                    var theResponse={};
                    theResponse.description=data;
                    theResponse.responseResult="error";
                    $rootScope.$broadcast('serverResponse', theResponse);
                });
        };
        createAccountService.updateAccount = function (theUser) {
            requestManager.makePostCall(theUser, {}, configService.getEndpoint('update.account'))
                .success(function (data, status, headers, config) {
                    $rootScope.$broadcast('serverResponse', data);
                }).error(function (data, status, headers, config) {
                    var theResponse={};
                    theResponse.description=data;
                    theResponse.responseResult="error";
                    $rootScope.$broadcast('serverResponse', theResponse);
                });
        };
        return createAccountService;
    }]);
