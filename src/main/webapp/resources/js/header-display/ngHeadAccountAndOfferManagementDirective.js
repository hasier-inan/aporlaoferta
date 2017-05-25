/**
 * Created by hasiermetal on 19/06/16.
 */
aporlaofertaApp
    .directive('ngHeadAccountAndOfferManagement', ['$window', '$anchorScroll', function ($window, $anchorScroll) {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/header-display/headAccountAndOfferManagement.jsp',
            scope: {
                displayLogin: '=',
                displaySignup: '=',
                displayOfferCreate: '=',
                displayAccountUpdateForm: '='
            },
            link: function ($scope) {
                $scope.shareUrl = "https://www.aporlaoferta.com";
                $scope.shareText = $("meta[property='og:description']").attr("content");
                $scope.scrollPosition = true;
                angular.element($window).bind("scroll", function () {
                    $scope.scrollPosition = this.pageYOffset <= 50;
                    $scope.$apply();
                });
                $scope.toTheTop = function () {
                    $anchorScroll();
                }
            }
        }
    }]);