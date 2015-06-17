/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngOverheadDisplay', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/header-display/headDisplay.jsp',
            link: function (scope, elem, attrs) {},
            controller:['$scope', function ($scope) {
                $scope.overheadVisible=false;
                $scope.displayOfferCreation=false;
                $scope.displayAccountLogin=false;
                $scope.displayAccountCreation=false;
                $scope.displayLogin=function(){
                    $scope.overheadVisible=true;
                    $scope.displayOfferCreation=false;
                    $scope.displayAccountLogin=true;
                    $scope.displayAccountCreation=false;
                };
                $scope.displaySignup=function(){
                    $scope.overheadVisible=true;
                    $scope.displayOfferCreation=false;
                    $scope.displayAccountLogin=false;
                    $scope.displayAccountCreation=true;
                };
                $scope.displayOfferCreate=function(){
                    $scope.overheadVisible=true;
                    $scope.displayOfferCreation=true;
                    $scope.displayAccountLogin=false;
                    $scope.displayAccountCreation=false;
                };
                $scope.setDefaultVisibility=function(){
                    $scope.overheadVisible=false;
                    $scope.displayOfferCreation=false;
                    $scope.displayAccountLogin=false;
                    $scope.displayAccountCreation=false;
                };
                $scope.closeOverheadDisplay=function(){
                    $scope.setDefaultVisibility();
                }
                $scope.setDefaultVisibility();
            }]
        }
    });