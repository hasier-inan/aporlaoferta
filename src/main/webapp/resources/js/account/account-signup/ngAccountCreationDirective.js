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
            controller: ['$scope', 'accountController', 'vcRecaptchaService','alertService', function ($scope, accountController, vcRecaptchaService,alertService) {
                $scope.theUser = {};
                $scope.publicKey = "6LdqHQoTAAAAAAht2VhkrLGU26eBOjL-nK9zXxcn";
                $scope.disableNickname = false;
                $scope.createAccount = function (theUser) {
                    if (vcRecaptchaService.getResponse() === "") {
                        alertService.sendErrorMessage("Por favor, haga click en el captcha para demostrar que no es un robot");
                        vcRecaptchaService.reload();
                    }
                    else {
                        accountController.createAccount(theUser, vcRecaptchaService.getResponse());
                        $scope.theUser = {};
                        $scope.overheadDisplay = false;
                    }
                }
            }]
        }
    });