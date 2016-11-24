/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngAccountCreation', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/account/account-signup/createAccount.jsp',
            scope: {
                overheadDisplay: '=',
                customCloseCallback: '=',
                displayCallback: '='
            },
            controller: ['$scope', 'requestManager', 'configService', 'vcRecaptchaService', 'alertService','accountHelper',
                function ($scope, requestManager, configService, vcRecaptchaService, alertService,accountHelper) {
                    $scope.theUser = {};
                    $scope.widgetId = null;
                    $scope.publicKey = "6LdqHQoTAAAAAAht2VhkrLGU26eBOjL-nK9zXxcn";
                    $scope.disableNickname = false;
                    $scope.passwordUpdateRequired = true;
                    $scope.validMail = accountHelper.validMail;
                    $scope.validPassword = accountHelper.validPassword;
                    $scope.createAccount = function (theUser) {
                        if (vcRecaptchaService.getResponse($scope.widgetId) === "") {
                            $scope.displayErrorMessageAndDisplayAccount();
                        }
                        else {
                            delete $scope.theUser.oldPassword;
                            $scope.processing = true;
                            requestManager.makePostCall(theUser, {recaptcha: vcRecaptchaService.getResponse($scope.widgetId)}, configService.getEndpoint('create.account'))
                                .success(function (data) {
                                    $scope.processAccountResponse(data);
                                }).error(function () {
                                    $scope.accountDefaultError();
                                }).finally(function () {
                                    $scope.processing = false;
                                });
                            $scope.theUser = {};
                            $scope.userPassword2 = "";
                            //$scope.overheadDisplay = false;
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
                        vcRecaptchaService.reload($scope.widgetId);
                    }

                    $scope.accountDefaultError = function () {
                        alertService.sendDefaultErrorMessage();
                        vcRecaptchaService.reload($scope.widgetId);
                        $scope.customCloseCallback = $scope.displayCallback;
                    }

                    $scope.accountError = function (customMessage) {
                        alertService.sendErrorMessage(customMessage);
                        $scope.restartRecaptcha();
                        $scope.customCloseCallback = $scope.displayCallback;
                    };

                    $scope.setWidgetId = function (widgetId) {
                        $scope.widgetId = widgetId;
                    };
                }]
        }
    });