/**
 * Created by hasiermetal on 08/08/16.
 */
aporlaofertaApp
    .directive('ngQuickTutorial', function () {
        return {
            restrict: 'A',
            scope: {
                isDisplayed: '=',
            },
            templateUrl: 'resources/js/header-display/tutorial/quickTutorial.jsp',
            controller: ['$scope', '$element', '$rootScope', '$cookies', 'configService',
                function ($scope, $element, $rootScope, $cookies, configService) {
                    $scope.$watch('isDisplayed', function () {
                        if ($scope.isDisplayed) {
                            $element.resize();
                            $element.find('.slick-dots li:first a').trigger('click');
                            $scope.isDisplayed=0;
                        }
                    });

                    $scope.hideTutorial = function () {
                        if ($cookies.get(configService.getEndpoint('aporlaoferta_cle')) === 'accepted') {
                            $cookies.put(configService.getEndpoint('aporlaoferta_tutorial'), true);
                        }
                        $rootScope.$broadcast('closeResponse');
                    };
                }]
        }
    });