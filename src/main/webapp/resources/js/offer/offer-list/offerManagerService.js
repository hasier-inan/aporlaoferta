var offerManager = angular.module('offerManager', []);

offerManager.service('offerManager', ['$rootScope', 'alertService', 'requestManager', 'configService',
    function ($rootScope, alertService, requestManager, configService) {
        var offerManagerController = {};
        offerManagerController.requestNewestOffers = function (offerFilter) {
            offerFilter.hot=false;
            offerManagerController.requestMoreOffers(offerFilter, undefined, offerManagerController.broadcastOfferList,
                function () {
                });
        }

        offerManagerController.requestHottestOffers = function (offerFilter) {
            offerFilter.hot=true;
            offerManagerController.requestMoreOffers(offerFilter, undefined, offerManagerController.broadcastOfferList,
                function () {
                });
        }

        offerManagerController.broadcastOfferList = function (offerList) {
            $rootScope.$broadcast('offerList', offerList);
        }

        offerManagerController.requestMoreOffers = function (offerFilter, lastOffer, callback, errorCallback) {
            requestManager.makePostCall(offerFilter, {'number': lastOffer}, configService.getEndpoint('get.filtered.offers'))
                .success(function (data, status, headers, config) {
                    callback(data.theOffers);
                    //$rootScope.$broadcast('offerList', data.theOffers);
                }).error(function (data, status, headers, config) {
                    alertService.sendDefaultErrorMessage();
                    errorCallback();
                });
        }
        offerManagerController.showSpecifications = function (id) {
            requestManager.makePostCall({}, {'id': id}, configService.getEndpoint('get.offer'))
                .success(function (data, status, headers, config) {
                    $rootScope.$broadcast('offerSpecifications', data.theOffers);
                }).error(function (data, status, headers, config) {
                    alertService.sendDefaultErrorMessage();
                });
        }
        return offerManagerController;
    }]);


