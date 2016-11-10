/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngOfferList', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-list/offerList.html',
            scope: {
                defaultList: '='
            },
            controller: ['$scope', 'offerManager',
                function ($scope, offerManager) {
                    $scope.offerList = [];
                    $scope.moreOffersLoading = true;
                    $scope.lastOffer = 0;

                    $scope.showMoreOffers = function (lastOffer) {
                        $scope.moreOffersLoading = true;
                        $scope.offerFilter.hot = $scope.defaultList === 'hottestOffers';
                        if (!$scope.isCategorySelected()) {
                            $scope.offerFilter.selectedcategory = "";
                        }
                        offerManager.requestMoreOffers($scope.offerFilter, lastOffer,
                            function (data) {
                                $scope.offerList = $scope.offerList.concat(data);
                                $scope.moreOffersLoading = false;
                            }, function () {
                                $scope.moreOffersLoading = false;
                            });
                    }

                    $scope.updateIndex = function (index) {
                        $scope.lastOffer = index;
                    }

                    $scope.$on('offerList', function (event, args) {
                        $scope.moreOffersLoading = false;
                        $scope.offerList = args;
                    });

                    $scope.$on('updatedOffer', function (event, args) {
                        var updatedOffer = args;
                        for (var i = 0; i < $scope.offerList.length; i++) {
                            if ($scope.offerList[i].id == updatedOffer.id) {
                                $scope.offerList[i] = updatedOffer;
                                break;
                            }
                        }
                    });

                    $scope.$on('removedOffer', function (event, args) {
                        var id = args;
                        for (var i = 0; i < $scope.offerList.length; i++) {
                            if ($scope.offerList[i].id == id) {
                                $scope.offerList.splice(i, 1);
                                break;
                            }
                        }
                    });

                    $scope.showSpecifications = function (id) {
                        offerManager.showSpecifications(id);
                    }

                    $scope.initialiseScrollyButtons = function () {
                        $('.scrolly')
                            .scrolly({
                                speed: 1500
                            });
                    }

                    $scope.parsePrice = function (price) {
                        if (price || price == 0) {
                            return price.toString().replace(/\./, ',');
                        }
                    }

                    $scope.isCategorySelected = function () {
                        return !$scope.isEmptyCategory($scope.offerFilter.selectedcategory);
                    };

                    $scope.isEmptyCategory = function (category) {
                        return ["", null, undefined, "Categoría", "Todas", "CATEGORÍA", "TODAS"].indexOf(category) > -1;
                    }

                    $scope.offerFeedbackStyle = function (offer) {
                        var offerFeedback = offer.offerPositiveVote - offer.offerNegativeVote;
                        if (offerFeedback > 0 && offerFeedback <= 100) {
                            return 'hotFeedback';
                        }
                        else if (offerFeedback > 100) {
                            return 'veryHotFeedback';
                        }
                        else if (offerFeedback < 0) {
                            return 'coldFeedback';
                        }
                        return 'neutralFeedback';
                    }

                    $scope.$on('appliedOfferFilters', function () {
                        $scope.moreOffersLoading = true;
                    });

                    $scope.$watch('offerList', function () {
                        $scope.moreOffersLoading = false;
                    });

                    $scope.initialiseScrollyButtons();
                }]
        }
    });