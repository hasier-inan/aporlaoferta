/**
 * Created by hasiermetal on 22/06/15.
 */
aporlaofertaApp
    .directive('ngAccountUpdate', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/account/account-signup/createAccount.html',
            scope: {
                overheadDisplay: '=',
                customCloseCallback: '=',
                displayCallback: '='
            },
            controller: ['$scope', 'vcRecaptchaService', 'alertService', '$http', 'requestManager', 'configService', '$rootScope',
                function ($scope, vcRecaptchaService, alertService, http, requestManager, configService, $rootScope) {
                    $scope.disableNickname = true;
                    $scope.theUser = {};
                    $scope.widgetId = null;
                    $scope.validMail = /^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/;
                    $scope.validPassword = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d!$%@#£€*?&]{8,}$/;
                    $scope.publicKey = "6LdqHQoTAAAAAAht2VhkrLGU26eBOjL-nK9zXxcn";
                    $scope.createAccount = function (theUser) {
                        if (vcRecaptchaService.getResponse($scope.widgetId) === "") {
                            $scope.displayErrorMessageAndDisplayAccount();
                        }
                        else {
                            $scope.processing = true;
                            requestManager.makePostCall(theUser, {recaptcha: vcRecaptchaService.getResponse($scope.widgetId)}, configService.getEndpoint('update.account'))
                                .success(function (data, status, headers, config) {
                                    if ($scope.processAccountResponse(data)) {
                                        $rootScope.$broadcast('userAvatar', angular.copy(theUser));
                                    }
                                }).error(function (data, status, headers, config) {
                                    $scope.accountDefaultError();
                                })
                                .finally(function () {
                                    $scope.processing = false;
                                    $scope.getUserDetails();
                                });
                            //$scope.theUser = {};
                            //$scope.overheadDisplay = false;
                        }
                    }
                    $scope.getUserDetails = function () {
                        requestManager.makePostCall({}, {}, configService.getEndpoint('get.account.details'))
                            .success(function (data, status, headers, config) {
                                $scope.theUser = data;
                            }).error(function (data, status, headers, config) {
                                alertService.sendErrorMessage("No se ha podido obtener la información del usuario");
                                $scope.customCloseCallback = false;
                            });
                    }
                    $scope.getUserDetails();

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
                            return true;
                        }
                        $scope.restartRecaptcha();
                        return false;
                    }

                    $scope.accountDefaultError = function () {
                        alertService.sendDefaultErrorMessage();
                        $scope.restartRecaptcha();
                        $scope.customCloseCallback = $scope.displayCallback;
                    }

                    $scope.accountError = function (customMessage) {
                        alertService.sendErrorMessage(customMessage);
                        $scope.restartRecaptcha();
                        $scope.customCloseCallback = $scope.displayCallback;
                    }
                    $scope.setWidgetId = function (widgetId) {
                        $scope.widgetId = widgetId;
                    };
                    $scope.restartRecaptcha = function () {
                        vcRecaptchaService.reload($scope.widgetId);
                    }
                }]
        }
    });