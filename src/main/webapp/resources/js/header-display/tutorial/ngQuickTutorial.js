/**
 * Created by hasiermetal on 08/08/16.
 */
aporlaofertaApp
    .directive('ngQuickTutorial', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/header-display/tutorial/quickTutorial.jsp',
            controller: ['$scope', '$element', '$rootScope', '$cookies', 'configService',
                function ($scope, $element, $rootScope, $cookies, configService) {

                    $scope.hideTutorial = function () {
                        if ($cookies.get(configService.getEndpoint('aporlaoferta_cle')) === 'accepted') {
                            $cookies.put(configService.getEndpoint('aporlaoferta_tutorial'), true);
                        }
                        $rootScope.$broadcast('closeResponse');
                    };
                }]
        }
    });