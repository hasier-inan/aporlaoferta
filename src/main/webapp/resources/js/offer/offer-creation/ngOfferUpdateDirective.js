/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngOfferUpdate', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-creation/offerCreation.html',
            scope: {
                overheadDisplay: '=',
                customCloseCallback: '=',
                displayCallback: '=',
                offer: "="
            },
            controller: ['$scope', 'offerManager', 'requestManager', 'configService', 'alertService', 'vcRecaptchaService',
                function ($scope, offerManager, requestManager, configService, alertService, vcRecaptchaService) {
                    $scope.publicKey = "6LdqHQoTAAAAAAht2VhkrLGU26eBOjL-nK9zXxcn";
                    $scope.createOffer = function () {
                        if (vcRecaptchaService.getResponse() === "") {
                            $scope.offerCreationError("Por favor, haga click en el captcha para demostrar que no es un robot");
                            vcRecaptchaService.reload();
                        }
                        else {
                            $scope.processing=true;
                            requestManager.makePostCall($scope.offer, {recaptcha: vcRecaptchaService.getResponse()}, configService.getEndpoint('create.offer'))
                                .success(function (data, status, headers, config) {
                                    if (!alertService.isAllOk(data)) {
                                        $scope.offerCreationError(data.descriptionEsp);
                                    }
                                    else {
                                        alertService.sendErrorMessage(data.descriptionEsp);
                                        $scope.resetValues();
                                        $scope.customCloseCallback = function () {
                                            offerManager.requestNewestOffers();
                                        }
                                    }
                                }).error(function (data, status, headers, config) {
                                    $scope.offerCreationError(alertService.getDefaultMessage());
                                }).finally(function(){
                                    $scope.processing=false;
                                });
                            //$scope.overheadDisplay = false;
                        }
                    };

                    $scope.restartRecaptcha = function () {
                        vcRecaptchaService.reload();
                    }

                    $scope.offerCreationError = function (customMessage) {
                        alertService.sendErrorMessage(customMessage);
                        $scope.restartRecaptcha();
                        $scope.customCloseCallback = $scope.displayCallback;
                    }

                    $scope.bigDecimalsOnly = /^\-?\d+((\.|\,)\d+)?$/;
                    $scope.resetValues = function () {
                        $scope.restartRecaptcha();
                        $scope.offer = {};
                        $scope.brandNewCompany = false;
                        $scope.resetCategory = true;
                        $scope.resetCompany = true;
                    };
                }]
        }
    });