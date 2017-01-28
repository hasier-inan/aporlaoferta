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
    'offerHelper',
    'accountHelper',
    'pwCheckModule',
    'ngMessages',
    'ngDropdowns',
    'ngSanitize',
    'MassAutoComplete',
    'truncate',
    'angular-cookie-law',
    'ngCookies',
    'slick'
]);

//main controller
aporlaofertaApp.controller('APorLaOfertaController', ['$scope', '$rootScope', function ($scope, $rootScope) {
    $scope.keyHandler = function ($event) {
        $rootScope.$broadcast('keydownControl', $event.keyCode);
    };

    $scope.time = new Date().getTime();
    angular.element(document.body).bind("mousemove keypress", function () {
        $scope.time = new Date().getTime();
    });
    $scope.minute = 60000;
    $scope.refresh = function () {
        (new Date().getTime() - $scope.time >= 20 * $scope.minute) ? window.location.reload() : setTimeout($scope.refresh, 10 * $scope.minute);
    }
    setTimeout($scope.refresh, 10 * $scope.minute);
}]);

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

aporlaofertaApp.config(['flowFactoryProvider', '$compileProvider',
    function (flowFactoryProvider, $compileProvider) {
    flowFactoryProvider.defaults = {
        target: 'uploadImage',
        permanentErrors: [404, 500, 501],
        maxChunkRetries: 1,
        chunkRetryInterval: 5000,
        simultaneousUploads: 4,
        singleFile: true,
        chunkSize: 4500000,
        testChunks: false
    };
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|mailto|whatsapp):/);

}]);