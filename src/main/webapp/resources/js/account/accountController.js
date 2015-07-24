var accountControllerModule = angular.module('accountController', []);

accountControllerModule.factory('accountController', ['$http', 'requestManager', 'configService', 'alertService', 'vcRecaptchaService',
    function ($http, requestManager, configService, alertService, vcRecaptchaService) {
        var createAccountService = {};
        createAccountService.createAccount = function (theUser) {
            requestManager.makePostCall(theUser, {recaptcha: vcRecaptchaService.getResponse()}, configService.getEndpoint('create.account'))
                .success(function (data, status, headers, config) {
                    alertService.sendErrorMessage(data.descriptionEsp);
                    vcRecaptchaService.reload();
                }).error(function (data, status, headers, config) {
                    alertService.sendDefaultErrorMessage();
                    vcRecaptchaService.reload();
                });
        };
        createAccountService.updateAccount = function (theUser) {
            requestManager.makePostCall(theUser, {recaptcha: vcRecaptchaService.getResponse()}, configService.getEndpoint('update.account'))
                .success(function (data, status, headers, config) {
                    alertService.sendErrorMessage(data.descriptionEsp);
                    vcRecaptchaService.reload();
                }).error(function (data, status, headers, config) {
                    alertService.sendDefaultErrorMessage();
                    vcRecaptchaService.reload();
                });
        };
        return createAccountService;
    }]);
