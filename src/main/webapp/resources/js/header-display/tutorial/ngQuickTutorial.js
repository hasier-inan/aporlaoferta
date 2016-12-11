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
            controller: ['$scope', '$element', '$timeout',
                function ($scope, $element, $timeout) {
                    $scope.shareUrl = "http://www.aporlaoferta.com";
                    $scope.shareText = $("meta[property='og:description']").attr("content");

                    $scope.$watch('isDisplayed', function () {
                        if ($scope.isDisplayed) {
                            $scope.refreshTutorial();
                            $timeout(function () {
                                $scope.refreshTutorial();
                            }, 300);
                        }
                    });

                    $scope.refreshTutorial = function () {
                        $element.resize();
                        $element.find('.slick-dots li:first a').trigger('click');
                        $scope.isDisplayed = 0;
                    }

                    $scope.requestNewestOffers = function(){

                    }

                    $scope.requestHottestOffers = function(){

                    }

                }]
        }
    });