/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngAccountCreation', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/account/account-signup/createAccount.html',
            scope: {
                overheadDisplay: '=',
                customCloseCallback: '=',
                displayCallback: '='
            },
            link: function (scope, elem, attrs) {
            },
            controller: ['$scope', '$http', 'requestManager', 'configService', 'vcRecaptchaService', 'alertService',
                function ($scope, http, requestManager, configService, vcRecaptchaService, alertService) {
                    $scope.theUser = {};
                    $scope.publicKey = "6LdqHQoTAAAAAAht2VhkrLGU26eBOjL-nK9zXxcn";
                    $scope.disableNickname = false;
                    $scope.validMail = /^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/;
                    $scope.createAccount = function (theUser) {
                        if (vcRecaptchaService.getResponse() === "") {
                            $scope.displayErrorMessageAndDisplayAccount();
                        }
                        else {
                            $scope.theUser.oldPassword = {};
                            requestManager.makePostCall(theUser, {recaptcha: vcRecaptchaService.getResponse()}, configService.getEndpoint('create.account'))
                                .success(function (data, status, headers, config) {
                                    $scope.processAccountResponse(data);
                                }).error(function (data, status, headers, config) {
                                    $scope.accountDefaultError();
                                });
                            $scope.theUser = {};
                            $scope.overheadDisplay = false;
                        }
                    }
                    $scope.displayErrorMessageAndDisplayAccount = function () {
                        alertService.sendErrorMessage("Por favor, haga click en el captcha para demostrar que no es un robot");
                        $scope.restartRecaptcha();
                        $scope.customCloseCallback = $scope.displayCallback;
                    }
                    $scope.processAccountResponse = function (data) {
                        if (!alertService.isAllOk(data)) {
                            $scope.accountError(data.descriptionEsp);
                        }
                        else {
                            alertService.sendErrorMessage(data.descriptionEsp);
                            $scope.customCloseCallback = false;
                        }
                        $scope.restartRecaptcha();
                    }
                    $scope.restartRecaptcha = function () {
                        vcRecaptchaService.reload();
                    }

                    $scope.accountDefaultError = function () {
                        alertService.sendDefaultErrorMessage();
                        vcRecaptchaService.reload();
                        $scope.customCloseCallback = $scope.displayCallback;
                    }

                    $scope.accountError = function (customMessage) {
                        alertService.sendErrorMessage(customMessage);
                        $scope.restartRecaptcha();
                        $scope.customCloseCallback = $scope.displayCallback;
                    };
                }]
        }
    });