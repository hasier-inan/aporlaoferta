var alertManager = angular.module('alertService', []);

alertManager.service('alertService', ['$rootScope',function ($rootScope) {
    var alertService = {};
    alertService.sendErrorMessage = function (data) {
        var theResponse = {};
        theResponse.descriptionEsp = data;
        theResponse.responseResult = "error";
        $rootScope.$broadcast('serverResponse', theResponse);
    };
    alertService.sendDefaultErrorMessage=function(){
        var theResponse = {};
        theResponse.descriptionEsp = "No se ha podido ejecutar la operación";
        theResponse.responseResult = "error";
        $rootScope.$broadcast('serverResponse', theResponse);
    }
    return alertService;
}]);