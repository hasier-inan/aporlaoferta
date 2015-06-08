/**
 * Created by hasiermetal on 16/03/14.
 */
var aporlaofertaApp = angular.module('aPorLaOferta', ['offerController']);
//main controller
aporlaofertaApp.controller('APorLaOfertaController', ['$scope', 'offerController', function($scope, offerController) {
    offerController.initMainController($scope);
}]);