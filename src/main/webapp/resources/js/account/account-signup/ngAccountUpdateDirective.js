/**
 * Created by hasiermetal on 22/06/15.
 */
aporlaofertaApp
    .directive('ngAccountUpdate', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/account/account-signup/createAccount.html',
            scope: {
                overheadDisplay: '='
            },
            link: function (scope, elem, attrs) {
            },
            controller: ['$scope', 'vcRecaptchaService', 'alertService', 'accountController', 'requestManager', 'configService', function ($scope, vcRecaptchaService, alertService, accountController, requestManager, configService) {
                $scope.disableNickname = true;
                $scope.theUser = {};
                $scope.publicKey = "6LdqHQoTAAAAAAht2VhkrLGU26eBOjL-nK9zXxcn";
                $scope.createAccount = function (theUser) {
                    if (vcRecaptchaService.getResponse() === "") {
                        alertService.sendErrorMessage("Por favor, haga click en el captcha para demostrar que no es un robot");
                    }
                    else {
                        accountController.updateAccount(theUser);
                        //$scope.theUser = {};
                        $scope.overheadDisplay = false;
                    }
                }
                $scope.getUserDetails = function () {
                    //$scope.theUser=accountController.getUserDetails();
                    requestManager.makePostCall({}, {}, configService.getEndpoint('get.account.details'))
                        .success(function (data, status, headers, config) {
                            $scope.theUser = data;
                        }).error(function (data, status, headers, config) {
                            alertService.sendErrorMessage("No se ha podido obtener la información del usuario");
                        });
                }
                $scope.getUserDetails();
            }]
        }
    });