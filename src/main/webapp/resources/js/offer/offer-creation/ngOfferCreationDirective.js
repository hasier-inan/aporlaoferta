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
            controller: ['$scope', '$rootScope', 'offerManager', 'requestManager', 'configService', 'alertService', 'vcRecaptchaService',
                function ($scope, $rootScope, offerManager, requestManager, configService, alertService, vcRecaptchaService) {
                    $scope.offer = {};
                    $scope.widgetId = null;
                    $scope.publicKey = "6LdqHQoTAAAAAAht2VhkrLGU26eBOjL-nK9zXxcn";
                    $scope.resetCategory = true;

                    $scope.createOffer = function () {
                        if (vcRecaptchaService.getResponse($scope.widgetId) === "") {
                            $scope.offerCreationError("Por favor, haga click en el captcha para demostrar que no es un robot");
                            vcRecaptchaService.reload($scope.widgetId);
                        }
                        else {
                            $scope.processing = true;
                            requestManager.makePostCall($scope.parseOffer(), {recaptcha: vcRecaptchaService.getResponse($scope.widgetId)}, configService.getEndpoint('create.offer'))
                                .success(function (data) {
                                    if (!alertService.isAllOk(data)) {
                                        $scope.offerCreationError(data.descriptionEsp);
                                    }
                                    else {
                                        alertService.sendErrorMessage(data.descriptionEsp);
                                        $scope.resetValues();
                                        offerManager.requestUpdatedOffer(data.other);
                                        $scope.customCloseCallback = function () {
                                        }
                                    }
                                }).error(function () {
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
                        $scope.resetImage = true;
                    };
                    $scope.setWidgetId = function (widgetId) {
                        $scope.widgetId = widgetId;
                    };

                    $scope.isCompanyDefined = function () {
                        if ($scope.offer) {
                            return $scope.offer.offerCompany && $scope.offer.offerCompany.companyName;
                        }
                    };

                    $scope.isCategorySelected = function () {
                        return !$scope.isEmptyCategory($scope.offer.offerCategory);
                    };

                    $scope.isEmptyCategory = function (category) {
                        return ["", null, undefined, "Categoría", "Todas", "CATEGORÍA", "TODAS"].indexOf(category) > -1;
                    }

                    $scope.selectionPerformed = function () {
                        return $scope.isCompanyDefined() && $scope.isCategorySelected();
                    }

                    $scope.offerCompanies = {};

                    $scope.suggest_company=function(term) {
                        var q = term.toLowerCase().trim();
                        var results = [];
                        for (var i = 0; i < $scope.offerCompanies.length && results.length < 10; i++) {
                            var company = $scope.offerCompanies[i].companyName;
                            if (company.toLowerCase().indexOf(q) === 0)
                                results.push({ label: company, value: company });
                        }
                        return results;
                    }

                    $scope.autocomplete_options = {
                        suggest: $scope.suggest_company
                    };

                    $scope.populateCompanyList = function () {
                        requestManager.makePostCall({}, {}, configService.getEndpoint('get.companies'))
                            .success(function (data, status, headers, config) {
                                $scope.offerCompanies = angular.copy(data);
                            }).error(function (data, status, headers, config) {
                            var theResponse = {};
                            theResponse.description = data;
                            theResponse.responseResult = "error";
                            $rootScope.$broadcast('serverResponse', theResponse);
                        });
                    };

                    $scope.populateCompanyList();
                }]
        }
    });