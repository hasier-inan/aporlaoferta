var offerControllerModule = angular.module('offerController', []);

offerControllerModule.factory('offerController', ['$http', 'requestManager','configService',
    function ($http, requestManager,configService) {
        var offerControllerService = {};
        offerControllerService.getNewestOffers = function () {
            requestManager.makePostCall({}, {'number': 0}, configService.getEndpoint('get.offers'))
                .success(function (data, status, headers, config) {
                    return data;
                }).error(function (data, status, headers, config) {
                    //TODO: handle error;
                    alert("handle this error while retrieving data from newest offers");
                });
        };
        offerControllerService.createOffer = function (offer) {
            requestManager.makePostCall(offer, {}, configService.getEndpoint('create.offer'))
                .success(function (data, status, headers, config) {
                    return data;
                }).error(function (data, status, headers, config) {
                    //TODO: handle error;
                    alert("handle this error while retrieving data from newest offers");
                });
        };
        offerControllerService.getHottestOffers = function () {
            requestManager.makePostCall({}, {'number': 0}, configService.getEndpoint('get.hottest.offers'))
                .success(function (data, status, headers, config) {
                    return data;
                }).error(function (data, status, headers, config) {
                    //TODO: handle error;
                    alert("handle this error while retrieving data from newest offers");
                });
        };
        offerControllerService.getOfferCategories = function () {
            requestManager.makePostCall({}, {}, configService.getEndpoint('get.offer.categories'))
                .success(function (data, status, headers, config) {
                    return data;
                }).error(function (data, status, headers, config) {
                    //TODO: handle error;
                    alert("handle this error while retrieving data from categories");
                });
        };
        return offerControllerService;
    }]);

