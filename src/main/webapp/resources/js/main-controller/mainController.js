/**
 * Created by hasiermetal on 16/03/15.
 */
aporlaofertaApp = angular.module('aPorLaOferta', [
    'angular.filter',
    'requestManager',
    'alertService',
    'configService',
    'flow',
    'ngAnimate',
    'vcRecaptcha',
    'offerManager',
    'pwCheckModule',
    'ngMaterial',
    'ngMessages',
    'ngDropdowns'
]);

//main controller
aporlaofertaApp.controller('APorLaOfertaController', function ($scope, $rootScope) {
    $scope.keyHandler = function ($event) {
        $rootScope.$broadcast('keydownControl', $event.keyCode);
    };
});

aporlaofertaApp.directive('ngConfirmClick', [
    function () {
        return {
            link: function (scope, element, attr) {
                var msg = attr.ngConfirmClick || "";
                var clickAction = attr.confirmedClick;
                element.bind('click', function (event) {
                    if (window.confirm(msg)) {
                        scope.$eval(clickAction)
                    }
                });
            }
        };
    }]);

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