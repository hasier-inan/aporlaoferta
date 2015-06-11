var configManager = angular.module('configService', []);

configManager.service('configService', [function () {
    var configService = {};
    var GET_OFFERS_ENDPOINT = "getOffers";
    var GET_HOTTEST_OFFERS_ENDPOINT = "getHottestOffers";
    var GET_OFFER_ENDPOINT = "getOffer";
    var CREATE_OFFER_ENDPOINT = "createOffer";
    var CREATE_ACCOUNT_ENDPOINT = "createUser";
    var GET_COMPANIES_ENDPOINT = "companyList";
    var GET_OFFER_CATEGORIES_ENDPOINT = "getOfferCategories";
    configService.getEndpoint = function (key) {
        return configMap[key];
    };
    var configMap = {
        'get.offers': GET_OFFERS_ENDPOINT,
        'get.hottest.offers': GET_HOTTEST_OFFERS_ENDPOINT,
        'get.offer': GET_OFFER_ENDPOINT,
        'create.offer': CREATE_OFFER_ENDPOINT,
        'create.account': CREATE_ACCOUNT_ENDPOINT,
        'get.companies': GET_COMPANIES_ENDPOINT,
        'get.offer.categories': GET_OFFER_CATEGORIES_ENDPOINT
    };
    return configService;
}]);