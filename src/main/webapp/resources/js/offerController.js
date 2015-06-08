var offerControllerModule = angular.module('offerController', []);

offerControllerModule.factory('offerController', ['$http',
    function ($http) {
        var offerControllerService = {};
        offerControllerService.initMainController = function (scope) {
            alert("initializing offer controller, default values will be set here...");
        };
        offerControllerService.getNewestOffers = function () {
            alert("http newestrequest can be performed here, defined in the main offerController maybe?");
        }
        offerControllerService.getHottestOffers = function () {
            alert("http hottest request can be performed here, defined in the main offerController maybe?");
        }
        return offerControllerService;
    }]);
