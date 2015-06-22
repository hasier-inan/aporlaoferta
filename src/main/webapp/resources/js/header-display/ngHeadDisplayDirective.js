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
                $scope.displayOfferSpecifications=false;
                $scope.displayLogin=function(){
                    $scope.setDefaultVisibility();
                    $scope.displayAccountLogin=true;
                    $scope.overheadVisible=true;
                };
                $scope.displaySignup=function(){
                    $scope.setDefaultVisibility();
                    $scope.displayAccountCreation=true;
                    $scope.overheadVisible=true;
                };
                $scope.displayOfferCreate=function(){
                    $scope.setDefaultVisibility();
                    $scope.displayOfferCreation=true;
                    $scope.overheadVisible=true;
                };
                $scope.displayOfferDetails=function(){
                    $scope.setDefaultVisibility();
                    $scope.displayOfferSpecifications=true;
                    $scope.overheadVisible=true;
                };
                $scope.setDefaultVisibility=function(){
                    $scope.overheadVisible=false;
                    $scope.displayOfferCreation=false;
                    $scope.displayAccountLogin=false;
                    $scope.displayAccountCreation=false;
                    $scope.displayOfferSpecifications=false;
                };
                $scope.closeOverheadDisplay=function(){
                    $scope.overheadVisible=false;
                };
                $scope.$on('offerSpecifications', function (event, args) {
                    $scope.offerSpecifications = args;
                    $scope.displayOfferDetails();

                });
                $scope.setDefaultVisibility();
            }]
        }
    });