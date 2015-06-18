/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngOfferList', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-list/offerList.html',
            link: function (scope, elem, attrs) {},
            controller:['$scope', '$rootScope', 'requestManager', 'configService', function ($scope, $rootScope, requestManager, configService) {
                $scope.requestNewestOffers = function () {
                    requestManager.makePostCall({}, {'number': 0}, configService.getEndpoint('get.offers'))
                        .success(function (data, status, headers, config) {
                            $scope.offerList=data.theOffers;
                        }).error(function (data, status, headers, config) {
                            //TODO: handle error;
                            alert("handle this error while retrieving data from newest offers");
                        });
                }

                $scope.requestHottestOffers = function () {
                    requestManager.makePostCall({}, {'number': 0}, configService.getEndpoint('get.hottest.offers'))
                        .success(function (data, status, headers, config) {
                            $scope.offerList= data.theOffers;
                        }).error(function (data, status, headers, config) {
                            //TODO: handle error;
                            alert("handle this error while retrieving data from newest offers");
                        });
                }

                $scope.showSpecifications=function(id){
                    requestManager.makePostCall({}, {'id': id}, configService.getEndpoint('get.offer'))
                        .success(function (data, status, headers, config) {
                            $rootScope.$broadcast('offerSpecifications', data.theOffers);
                        }).error(function (data, status, headers, config) {
                            //TODO: handle error;
                            alert("handle this error while retrieving data from newest offers");
                        });
                }
                //initialise with newest offers
                $scope.requestNewestOffers();
            }]
        }
    });