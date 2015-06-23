/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngAccountCreation', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/account/account-signup/createAccount.html',
            scope: {
                overheadDisplay: '='
            },
            link: function (scope, elem, attrs) {
            },
            controller: ['$scope', 'accountController', function ($scope, accountController) {
                $scope.theUser = {};
                $scope.disableNickname=false;
                $scope.createAccount = function (theUser) {
                    accountController.createAccount(theUser);
                    $scope.theUser = {};
                    $scope.overheadDisplay = false;
                }
            }]
        }
    });