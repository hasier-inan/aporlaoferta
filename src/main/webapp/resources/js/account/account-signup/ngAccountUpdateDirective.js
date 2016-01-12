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
            link: function (scope, elem, attrs) {
            },
            controller: ['$scope', 'vcRecaptchaService', 'alertService', '$http', 'requestManager', 'configService',
                function ($scope, vcRecaptchaService, alertService, http, requestManager, configService) {
                    $scope.disableNickname = true;
                    $scope.theUser = {};
                    $scope.widgetId = null;
                    $scope.validMail = /^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/;
                    $scope.publicKey = "6LdqHQoTAAAAAAht2VhkrLGU26eBOjL-nK9zXxcn";
                    $scope.createAccount = function (theUser) {
                        if (vcRecaptchaService.getResponse($scope.widgetId) === "") {
                            $scope.displayErrorMessageAndDisplayAccount();
                        }
                        else {
                            $scope.processing = true;
                            requestManager.makePostCall(theUser, {recaptcha: vcRecaptchaService.getResponse($scope.widgetId)}, configService.getEndpoint('update.account'))
                                .success(function (data, status, headers, config) {
                                    $scope.processAccountResponse(data);
                                }).error(function (data, status, headers, config) {
                                    $scope.accountDefaultError();
                                })
                                .finally(function () {
                                    $scope.processing = false;
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
                        }
                        $scope.restartRecaptcha();
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