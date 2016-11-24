/**
 * Created by hasiermetal on 22/06/15.
 */
aporlaofertaApp
    .directive('ngAccountUpdate', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/account/account-signup/createAccount.jsp',
            scope: {
                overheadDisplay: '=',
                customCloseCallback: '=',
                displayCallback: '='
            },
            controller: ['$scope', 'vcRecaptchaService', 'alertService', '$http', 'requestManager', 'configService', '$rootScope','accountHelper',
                function ($scope, vcRecaptchaService, alertService, http, requestManager, configService, $rootScope,accountHelper) {
                    $scope.disableNickname = true;
                    $scope.theUser = {};
                    $scope.widgetId = null;
                    $scope.passwordUpdateRequired = false;
                    $scope.validMail = accountHelper.validMail;
                    $scope.validPassword = accountHelper.validPassword;
                    $scope.publicKey = "6LdqHQoTAAAAAAht2VhkrLGU26eBOjL-nK9zXxcn";
                    $scope.createAccount = function (theUser) {
                        if (vcRecaptchaService.getResponse($scope.widgetId) === "") {
                            $scope.displayErrorMessageAndDisplayAccount();
                        }
                        else {
                            $scope.processing = true;
                            requestManager.makePostCall(theUser, {recaptcha: vcRecaptchaService.getResponse($scope.widgetId)}, configService.getEndpoint('update.account'))
                                .success(function (data) {
                                    if ($scope.processAccountResponse(data)) {
                                        $rootScope.$broadcast('userAvatar', angular.copy(theUser));
                                    }
                                }).error(function () {
                                    $scope.accountDefaultError();
                                })
                                .finally(function () {
                                    $scope.processing = false;
                                    $scope.getUserDetails();
                                    $scope.passwordUpdateRequired = false;
                                });
                            //$scope.theUser = {};
                            //$scope.overheadDisplay = false;
                        }
                    }

                    $scope.processLogin = function(){
                        $rootScope.$broadcast('userLoginRequest');
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

                    $scope.resetPassword=function(){
                        $scope.theUser.oldPassword=null;
                        $scope.theUser.userSpecifiedPassword=null;
                        $scope.userPassword2=null;
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