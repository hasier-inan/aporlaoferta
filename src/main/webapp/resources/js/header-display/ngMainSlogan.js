/**
 * Created by hasiermetal on 10/08/16.
 */
aporlaofertaApp
    .directive('ngMainSlogan', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/header-display/mainSlogan.html',
            controller: ['$scope', '$timeout',
                function ($scope, $timeout) {
                    $timeout(function () {
                        $scope.faded=true;
                    }, 1200);
                }]
        }
    });