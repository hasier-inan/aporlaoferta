/**
 * Created by hasiermetal on 14/08/16.
 */
aporlaofertaApp
    .directive('ngFooter', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/footer/footer.jsp',
            controller: ['$scope', '$rootScope',
                function ($scope, $rootScope) {
                    $scope.displayTutorial = function () {
                        $rootScope.$broadcast('displayTutorial');
                    }
                }]
        }
    });