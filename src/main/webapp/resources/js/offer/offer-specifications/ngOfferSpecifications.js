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
            controller: ['offerManager', 'alertService', '$scope', '$rootScope', 'requestManager', 'configService',
                function (offerManager, alertService, $scope, $rootScope, requestManager, configService) {
                    $scope.sharePrefix = "www.aporlaoferta.com/start?sh=";
                    $scope.sharePrice = "€: ";
                    $scope.commentsCustomCloseCallback = {};
                    $scope.appliedOfferFilters = {};

                    $scope.votePositive = function (id) {
                        requestManager.makePostCall({}, {'offerId': id}, configService.getEndpoint('positive.feedback'))
                            .success(function (data, status, headers, config) {
                                $scope.sendMessageAndShowSpecifications(data, id, 'offerPositiveVote');
                            }).error(function (data, status, headers, config) {
                                $scope.sendDefaultErrorMessageAndShowSpecifications(id);
                            });
                    }
                    $scope.voteNegative = function (id) {
                        requestManager.makePostCall({}, {'offerId': id}, configService.getEndpoint('negative.feedback'))
                            .success(function (data, status, headers, config) {
                                $scope.sendMessageAndShowSpecifications(data, id, 'offerNegativeVote');
                            }).error(function (data, status, headers, config) {
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
                    $scope.expireOffer = function (theOffer) {
                        requestManager.makePostCall({}, {'id': theOffer.id}, configService.getEndpoint('expire.offer'))
                            .success(function (data, status, headers, config) {
                                if (!alertService.isAllOk(data)) {
                                    $scope.offerCreationError(data.descriptionEsp);
                                }
                                else {
                                    alertService.sendErrorMessage(data.descriptionEsp);
                                    $scope.resetValues();
                                    $scope.customCloseCallback = function () {
                                        offerManager.requestNewestOffers($scope.appliedOfferFilters);
                                    }
                                }
                            }).error(function (data, status, headers, config) {
                                $scope.offerCreationError(alertService.getDefaultMessage());
                            });
                    }

                    $scope.parsePrice = function (price) {
                        if(price){
                        return price.toString().replace(/\./, ',');
                        }
                    }

                    $scope.$on('commentsCustomCloseCallback', function (event, args) {
                        var customCallback = args;
                        $scope.customCloseCallback = customCallback;
                    });

                    $scope.$on('appliedOfferFilters', function (event, args) {
                        $scope.appliedOfferFilters = args;
                    });
                }]
        }
    });