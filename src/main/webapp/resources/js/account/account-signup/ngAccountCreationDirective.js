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
            controller: ['$scope', 'requestManager', 'configService', 'vcRecaptchaService', 'alertService',
                function ($scope, requestManager, configService, vcRecaptchaService, alertService) {
                    $scope.theUser = {};
                    $scope.widgetId = null;
                    $scope.publicKey = "6LdqHQoTAAAAAAht2VhkrLGU26eBOjL-nK9zXxcn";
                    $scope.disableNickname = false;
                    $scope.passwordUpdateRequired = true;
                    $scope.validMail = /^[_a-z0-9-]+(\.[_a-z0-9-]+)*(\+[a-z0-9-]+)?@[a-z]+\.[a-z.]{2,5}$/;
                    $scope.validPassword= /^(?=.*?)(?=.*?[a-z])(?=.*?[0-9])(?=.*?).{8,}$/;
                    $scope.createAccount = function (theUser) {
                        if (vcRecaptchaService.getResponse($scope.widgetId) === "") {
                            $scope.displayErrorMessageAndDisplayAccount();
                        }
                        else {
                            delete $scope.theUser.oldPassword;
                            $scope.processing = true;
                            requestManager.makePostCall(theUser, {recaptcha: vcRecaptchaService.getResponse($scope.widgetId)}, configService.getEndpoint('create.account'))
                                .success(function (data, status, headers, config) {
                                    $scope.processAccountResponse(data);
                                }).error(function (data, status, headers, config) {
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