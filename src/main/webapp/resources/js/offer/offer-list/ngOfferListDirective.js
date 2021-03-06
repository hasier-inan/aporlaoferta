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
            controller: ['$scope', 'offerManager', 'offerHelper',
                function ($scope, offerManager,offerHelper) {
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
                        return offerHelper.parsePrice(price);
                    }

                    $scope.isCategorySelected = function () {
                        return !offerHelper.isEmptyCategory($scope.offerFilter.selectedcategory);
                    };

                    $scope.offerFeedbackStyle = function (offer) {
                       return offerHelper.offerFeedbackStyle(offer);
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