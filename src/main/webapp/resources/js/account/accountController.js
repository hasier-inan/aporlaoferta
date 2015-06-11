var accountControllerModule = angular.module('accountController', []);

accountControllerModule.factory('accountController', ['$http','requestManager','configService',
    function ($http,requestManager,configService) {
        var createAccountService = {};
        createAccountService.createAccount = function (theUser) {
            requestManager.makePostCall(theUser, {}, configService.getEndpoint('create.account'))
                .success(function (data, status, headers, config) {
                    return data;
                }).error(function (data, status, headers, config) {
                    //TODO: handle error;
                    alert("handle this error while retrieving data from create account");
                });
        };
        return createAccountService;
    }]);
