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
            controller: ['$scope', '$element',
                function ($scope, $element) {
                    $scope.shareUrl = "www.aporlaoferta.com";
                    $scope.shareText = $("meta[property='og:description']").attr("content");

                    $scope.$watch('isDisplayed', function () {
                        if ($scope.isDisplayed) {
                            $element.resize();
                            $element.find('.slick-dots li:first a').trigger('click');
                            $scope.isDisplayed = 0;
                        }
                    });


                }]
        }
    });