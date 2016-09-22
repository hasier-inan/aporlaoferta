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
                    $scope.moreOffersLoading = false;
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
                        $scope.offerList = args;
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
                        if (price) {
                            return price.toString().replace(/\./, ',');
                        }
                    }

                    $scope.isCategorySelected = function () {
                        return $scope.offerFilter.selectedcategory != ""
                            && $scope.offerFilter.selectedcategory != null
                            && $scope.offerFilter.selectedcategory != undefined
                            && $scope.offerFilter.selectedcategory != "Categoría"
                            && $scope.offerFilter.selectedcategory != "CATEGORÍA";
                    };

                    $scope.offerFeedbackStyle=function(offer){
                        if (offer.offerPositiveVote - offer.offerNegativeVote>0){
                          return 'hotFeedback';
                        }
                        else if(offer.offerPositiveVote - offer.offerNegativeVote <0){
                             return 'coldFeedback';
                        }
                        return 'neutralFeedback';
                    }

                    $scope.initialiseScrollyButtons();
                }]
        }
    });