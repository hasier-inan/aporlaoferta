/**
 * Created by hasiermetal on 08/08/16.
 */
aporlaofertaApp
    .directive('ngQuickTutorial', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/header-display/quickTutorial.html',
            controller: ['$scope', '$cookies', 'configService',
                function ($scope, $cookies, configService) {

                    $scope.hideTutorial = function () {
                        if ($cookies.get(configService.getEndpoint('law.cookie')) === 'accepted') {
                            $cookies.put(configService.getEndpoint('tutorial.cookie'), true);
                        }
                    };
                }]
        }
    });