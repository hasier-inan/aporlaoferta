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
    'ngDropdowns',
    'truncate',
    'angular-cookie-law',
    'ngCookies',
    'slick'
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

                scope.time = new Date().getTime();
                angular.element(document.body).bind("mousemove keypress", function () {
                    scope.time = new Date().getTime();
                });

                scope.minute=60000;
                scope.refresh = function () {
                    if (new Date().getTime() - scope.time >= 20*scope.minute)
                        window.location.reload();
                    else
                        setTimeout(scope.refresh, 10*scope.minute);
                }

                setTimeout(scope.refresh, 10*scope.minute);
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