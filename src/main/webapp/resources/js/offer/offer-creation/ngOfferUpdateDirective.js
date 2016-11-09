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
                    $scope.widgetId = null;
                    $scope.publicKey = "6LdqHQoTAAAAAAht2VhkrLGU26eBOjL-nK9zXxcn";
                    $scope.createOffer = function () {
                        if (vcRecaptchaService.getResponse($scope.widgetId) === "") {
                            $scope.offerCreationError("Por favor, haga click en el captcha para demostrar que no es un robot");
                            vcRecaptchaService.restartRecaptcha();
                        }
                        else {
                            $scope.processing = true;
                            requestManager.makePostCall($scope.parseOffer(), {recaptcha: vcRecaptchaService.getResponse($scope.widgetId)}, configService.getEndpoint('update.offer'))
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
                                }).finally(function () {
                                    $scope.processing = false;
                                });
                            //$scope.overheadDisplay = false;
                        }
                    };

                    $scope.parseOffer = function () {
                        var theOffer = angular.copy($scope.offer);
                        try {
                            theOffer.originalPrice = parseFloat(theOffer.originalPrice.replace(/,/, '.'));
                        } catch (e) {/*float already*/
                        }
                        try {
                            theOffer.finalPrice = parseFloat(theOffer.finalPrice.replace(/,/, '.'));
                        } catch (e) {/*float already*/
                        }
                        return theOffer;
                    }

                    $scope.restartRecaptcha = function () {
                        vcRecaptchaService.reload($scope.widgetId);
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
                        $scope.resetImage = true;
                    };
                    $scope.setWidgetId = function (widgetId) {
                        $scope.widgetId = widgetId;
                    };

                    $scope.isCompanyDefined = function () {
                        if ($scope.offer) {
                            return $scope.offer.offerCompany != undefined && $scope.selectedcompany != "";
                        }
                    };
                    $scope.isCategorySelected = function () {
                        return $scope.offer
                            && $scope.offer.offerCategory != ""
                            && $scope.offer.offerCategory != null
                            && $scope.offer.offerCategory != undefined
                            && $scope.offer.offerCategory != "Categoría"
                            && $scope.offer.offerCategory != "CATEGORÍA";
                    };
                    $scope.selectionPerformed = function () {
                        return $scope.isCompanyDefined() && $scope.isCategorySelected();
                    }
                }]
        }
    });