var alertManager = angular.module('alertService', []);

alertManager.service('alertService', ['$rootScope', function ($rootScope) {
    var alertService = {};
    var defaultMessage = "No se ha podido ejecutar la operación";
    alertService.isAllOk = function (data) {
        return data.code == 0;
    }
    alertService.sendErrorMessage = function (data) {
        var theResponse = {};
        theResponse.descriptionEsp = data;
        theResponse.responseResult = "error";
        $rootScope.$broadcast('serverResponse', theResponse);
    };
    alertService.sendDefaultErrorMessage = function () {
        var theResponse = {};
        theResponse.descriptionEsp = alertService.getDefaultMessage();
        theResponse.responseResult = "error";
        $rootScope.$broadcast('serverResponse', theResponse);
    }
    alertService.getDefaultMessage = function () {
        return defaultMessage;
        ;
    }
    return alertService;
}]);