/**
 * Created by hasiermetal on 19/06/16.
 */
aporlaofertaApp
    .directive('ngHeadAccountAndOfferManagement', function ($window,$anchorScroll) {
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
                $scope.scrollPosition = true;
                angular.element($window).bind("scroll", function () {
                    $scope.scrollPosition = this.pageYOffset <= 50;
                    $scope.$apply();
                });
                $scope.toTheTop=function(){
                    $anchorScroll();
                }
            }
        }
    });