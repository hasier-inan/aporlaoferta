var offerManager = angular.module('offerManager', []);

offerManager.service('offerManager', ['$rootScope', 'alertService', 'requestManager', 'configService',
    function ($rootScope, alertService, requestManager, configService) {
        var offerManagerController = {};
        offerManagerController.appliedOfferFilters = {};

        offerManagerController.requestNewestOffers = function () {
            var offerFilter = angular.copy(offerManagerController.appliedOfferFilters);
            offerFilter.hot = false;
            offerManagerController.requestMoreOffers(offerFilter, undefined, offerManagerController.broadcastOfferList,
                function () {
                });
        }

        offerManagerController.requestHottestOffers = function () {
            var offerFilter = angular.copy(offerManagerController.appliedOfferFilters);
            offerFilter.hot = true;
            offerManagerController.requestMoreOffers(offerFilter, undefined, offerManagerController.broadcastOfferList,
                function () {
                });
        }

        $rootScope.$on('appliedOfferFilters', function (event, args) {
            offerManagerController.appliedOfferFilters = args;
        });

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
            $rootScope.$broadcast('loadingOfferSpecifications');
            requestManager.makePostCall({}, {'id': id}, configService.getEndpoint('get.offer'))
                .success(function (data, status, headers, config) {
                    if (alertService.isAllOk(data)) {
                        $rootScope.$broadcast('offerSpecifications', data.theOffers);
                    }
                    else {
                        alertService.sendErrorMessage(data.descriptionEsp);
                    }
                }).error(function (data, status, headers, config) {
                alertService.sendDefaultErrorMessage();
            });
        }
        return offerManagerController;
    }]);


