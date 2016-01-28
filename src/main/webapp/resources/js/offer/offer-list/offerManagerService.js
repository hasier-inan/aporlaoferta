var offerManager = angular.module('offerManager', []);

offerManager.service('offerManager', ['$rootScope', 'alertService', 'requestManager', 'configService',
    function ($rootScope, alertService, requestManager, configService) {
        var offerManagerController = {};
        offerManagerController.requestNewestOffers = function () {
            offerManagerController.makeRequest(configService.getEndpoint('get.offers'));
        }

        offerManagerController.requestHottestOffers = function () {
            offerManagerController.makeRequest(configService.getEndpoint('get.hottest.offers'));
        }
        offerManagerController.makeRequest = function (endpoint) {
            requestManager.makePostCall({}, {}, endpoint)
                .success(function (data, status, headers, config) {
                    $rootScope.$broadcast('offerList', data.theOffers);
                }).error(function (data, status, headers, config) {
                    alertService.sendDefaultErrorMessage();
                });
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


