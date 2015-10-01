var configManager = angular.module('configService', []);

configManager.service('configService', [function () {
    var configService = {};
    var GET_OFFERS_ENDPOINT = "getOffers";
    var GET_HOTTEST_OFFERS_ENDPOINT = "getHottestOffers";
    var GET_OFFER_ENDPOINT = "getOffer";
    var CREATE_OFFER_ENDPOINT = "createOffer";
    var CREATE_ACCOUNT_ENDPOINT = "createUser";
    var UPDATE_ACCOUNT_ENDPOINT = "updateUser";
    var GET_ACCOUNT_DETAILS_ENDPOINT = "accountDetails";
    var GET_COMPANIES_ENDPOINT = "companyList";
    var GET_OFFER_CATEGORIES_ENDPOINT = "getOfferCategories";
    var POSITIVE_FEEDBACK_ENDPOINT = "votePositive";
    var NEGATIVE_FEEDBACK_ENDPOINT = "voteNegative";
    var CREATE_COMMENT_ENDPOINT = "createComment";
    var QUOTE_COMMENT_ENDPOINT = "quoteComment";
    var GET_FILTERED_ENDPOINT = "getFilteredOffers";
    var REQUEST_PASSWORD_UPDATE = "forgottenPassword";
    var REQUEST_PASSWORD = "requestForgottenPassword";
    var IMAGE_MAX_SIZE = 2000;
    configService.getEndpoint = function (key) {
        return configMap[key];
    };
    var configMap = {
        'get.offers': GET_OFFERS_ENDPOINT,
        'get.hottest.offers': GET_HOTTEST_OFFERS_ENDPOINT,
        'get.offer': GET_OFFER_ENDPOINT,
        'create.offer': CREATE_OFFER_ENDPOINT,
        'create.account': CREATE_ACCOUNT_ENDPOINT,
        'update.account': UPDATE_ACCOUNT_ENDPOINT,
        'get.account.details': GET_ACCOUNT_DETAILS_ENDPOINT,
        'get.companies': GET_COMPANIES_ENDPOINT,
        'get.offer.categories': GET_OFFER_CATEGORIES_ENDPOINT,
        'positive.feedback': POSITIVE_FEEDBACK_ENDPOINT,
        'negative.feedback': NEGATIVE_FEEDBACK_ENDPOINT,
        'create.comment': CREATE_COMMENT_ENDPOINT,
        'quote.comment': QUOTE_COMMENT_ENDPOINT,
        'get.filtered.offers': GET_FILTERED_ENDPOINT,
        'password.forgotten': REQUEST_PASSWORD_UPDATE,
        'password.forgotten.request': REQUEST_PASSWORD,
        'max.image.size': IMAGE_MAX_SIZE
    };
    return configService;
}]);