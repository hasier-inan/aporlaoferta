/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngOfferCreation', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-creation/offerCreation.html',
            scope: {
                overheadDisplay: '=',
                customCloseCallback: '=',
                displayCallback: '='
            },
            controller: ['$scope', 'offerManager', 'requestManager', 'configService', 'alertService', 'vcRecaptchaService',
                function ($scope, offerManager, requestManager, configService, alertService, vcRecaptchaService) {
                    $scope.offer = {};
                    $scope.publicKey = "6LdqHQoTAAAAAAht2VhkrLGU26eBOjL-nK9zXxcn";
                    $scope.resetCategory = true;
                    $scope.createOffer = function () {
                        if (vcRecaptchaService.getResponse() === "") {
                            $scope.offerCreationError("Por favor, haga click en el captcha para demostrar que no es un robot");
                        }
                        else {
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
                                });
                            $scope.overheadDisplay = false;
                        }
                    };

                    $scope.offerCreationError = function (customMessage) {
                        alertService.sendErrorMessage(customMessage);
                        vcRecaptchaService.reload();
                        $scope.customCloseCallback = $scope.displayCallback;
                    }

                    $scope.bigDecimalsOnly = /^\-?\d+((\.|\,)\d+)?$/;
                    $scope.resetValues = function () {
                        vcRecaptchaService.reload();
                        $scope.offer = {};
                        $scope.brandNewCompany = false;
                        $scope.resetCategory = true;
                        $scope.resetCompany = true;

                    }
                }]
        }
    });