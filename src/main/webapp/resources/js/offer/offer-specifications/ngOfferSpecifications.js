/**
 * Created by hasiermetal on 11/06/15.
 */
aporlaofertaApp
    .directive('ngOfferSpecifications', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-specifications/offerSpecifications.jsp',
            scope: {
                theOffer: '=',
                customCloseCallback: '='
            },
            controller: ['offerManager', 'alertService', '$scope', '$rootScope', 'requestManager', 'configService', 'offerHelper',
                function (offerManager, alertService, $scope, $rootScope, requestManager, configService, offerHelper) {
                    $scope.sharePrefix = "www.aporlaoferta.com/offer?sh=";
                    $scope.sharePrice = "€: ";
                    $scope.offer = {offerImage: "/resources/images/offer.png"}

                    $scope.commentsCustomCloseCallback = {};

                    $scope.votePositive = function (id) {
                        requestManager.makePostCall({}, {'offerId': id}, configService.getEndpoint('positive.feedback'))
                            .success(function (data) {
                                $scope.sendMessageAndShowSpecifications(data, id, 'offerPositiveVote');
                            }).error(function () {
                            $scope.sendDefaultErrorMessageAndShowSpecifications(id);
                        });
                    }
                    $scope.voteNegative = function (id) {
                        requestManager.makePostCall({}, {'offerId': id}, configService.getEndpoint('negative.feedback'))
                            .success(function (data) {
                                $scope.sendMessageAndShowSpecifications(data, id, 'offerNegativeVote');
                            }).error(function () {
                            $scope.sendDefaultErrorMessageAndShowSpecifications(id);
                        });
                    }
                    $scope.sendMessageAndShowSpecifications = function (data, id, feedback) {
                        if (!alertService.isAllOk(data)) {
                            alertService.sendErrorMessage(data.descriptionEsp);
                            $scope.customCloseCallback = function () {
                                offerManager.showSpecifications(id);
                            }
                        }
                        else {
                            $scope.theOffer[0][feedback] = $scope.theOffer[0][feedback] + 1;
                            offerManager.requestUpdatedOffer(id);
                        }
                    }
                    $scope.sendDefaultErrorMessageAndShowSpecifications = function (id) {
                        alertService.sendDefaultErrorMessage();
                        $scope.customCloseCallback = function () {
                            offerManager.showSpecifications(id);
                        }
                    }
                    $scope.updateOffer = function (theOffer) {
                        $rootScope.$broadcast('updateTheOffer', angular.copy(theOffer));
                    }

                    $scope.$watch('theOffer', function () {
                        if ($scope.theOffer) {
                            $scope.offer = $scope.theOffer[0];
                        }
                    });

                    $scope.expireOffer = function (theOffer) {
                        requestManager.makePostCall({}, {'id': theOffer.id}, configService.getEndpoint('expire.offer'))
                            .success(function (data) {
                                if (!alertService.isAllOk(data)) {
                                    alertService.sendErrorMessage(data.descriptionEsp);
                                }
                                else {
                                    alertService.sendErrorMessage(data.descriptionEsp);
                                    $scope.customCloseCallback = function () {
                                        offerManager.requestUpdatedOffer(theOffer.id);
                                    }
                                }
                            }).error(function () {
                            alertService.sendDefaultErrorMessage();
                        });
                    }

                    $scope.banUser = function (nickname) {
                        requestManager.makePostCall({}, {'nickname': nickname}, configService.getEndpoint('ban.user'))
                            .success(function (data) {
                                alertService.sendErrorMessage(data.descriptionEsp);
                            }).error(function () {
                            alertService.sendDefaultErrorMessage();
                        });
                    }

                    $scope.removeOffer = function (id) {
                        requestManager.makePostCall({}, {'id': id}, configService.getEndpoint('remove.offer'))
                            .success(function (data) {
                                alertService.sendErrorMessage(data.descriptionEsp);
                                $scope.customCloseCallback = function () {
                                    $rootScope.$broadcast('removedOffer', id);
                                }
                            }).error(function () {
                            alertService.sendDefaultErrorMessage();
                        });
                    }

                    $scope.parsePrice = function (price) {
                        if (price || price == 0) {
                            return price.toString().replace(/\./, ',');
                        }
                    }

                    $scope.offerFeedbackStyle = function (offer) {
                        return offerHelper.offerFeedbackStyle(offer);
                    }

                    $scope.processLogin = function () {
                        $rootScope.$broadcast('userLoginRequest');
                    }

                    $scope.processRegister = function () {
                        $rootScope.$broadcast('userRegisterRequest');
                    }

                    $scope.$on('commentsCustomCloseCallback', function (event, args) {
                        var customCallback = args;
                        $scope.customCloseCallback = customCallback;
                    });
                }]
        }
    })
;