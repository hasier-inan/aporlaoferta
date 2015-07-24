var offerManager = angular.module('offerManager', []);

offerManager.service('offerManager', ['$rootScope', 'alertService', 'requestManager', 'configService',
    function ($rootScope, alertService, requestManager, configService) {
        var offerManagerController = {};
        offerManagerController.requestNewestOffers = function (offerList) {
            requestManager.makePostCall({}, {'number': 0}, configService.getEndpoint('get.offers'))
                .success(function (data, status, headers, config) {
                    offerList = data.theOffers;
                }).error(function (data, status, headers, config) {
                    alertService.sendDefaultErrorMessage();
                });
        }

        offerManagerController.requestHottestOffers = function (offerList) {
            requestManager.makePostCall({}, {'number': 0}, configService.getEndpoint('get.hottest.offers'))
                .success(function (data, status, headers, config) {
                    offerList = data.theOffers;
                }).error(function (data, status, headers, config) {
                    alertService.sendDefaultErrorMessage();
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


