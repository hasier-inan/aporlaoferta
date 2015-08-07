/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp = angular.module('aPorLaOferta', ['angular.filter','requestManager', 'alertService','configService', 'flow', 'ngAnimate','vcRecaptcha','offerManager','pwCheckModule']);

//main controller
aporlaofertaApp.controller('APorLaOfertaController', function ($scope, $rootScope) {
    $scope.keyHandler=function($event){
        $rootScope.$broadcast('keydownControl', $event.keyCode);
    };
});
aporlaofertaApp.config(['flowFactoryProvider', function (flowFactoryProvider) {
    flowFactoryProvider.defaults = {
        target: 'uploadImage',
        permanentErrors: [404, 500, 501],
        maxChunkRetries: 1,
        chunkRetryInterval: 5000,
        simultaneousUploads: 4,
        singleFile: true
    };

}]);