var webpack = require('webpack');

module.exports = {
    entry: [
        './src/main/webapp/resources/js/request-handler/requestManagerService.js',
        './src/main/webapp/resources/js/config/configService.js',
        './src/main/webapp/resources/js/response/alertService.js',
        './src/main/webapp/resources/js/offer/offer-list/offerManagerService.js',
        './src/main/webapp/resources/js/main-controller/mainController.js',
        './src/main/webapp/resources/js/account/account-signup/ngAccountCreationDirective.js',
        './src/main/webapp/resources/js/account/account-signup/ngAccountUpdateDirective.js',
        './src/main/webapp/resources/js/account/account-signup/pwdCheck.js',
        './src/main/webapp/resources/js/account/account-login/ngAccountLoginDirective.js',
        './src/main/webapp/resources/js/account/account-logout/ngAccountLogoutDirective.js',
        './src/main/webapp/resources/js/offer/offer-list/ngOfferListDirective.js',
        './src/main/webapp/resources/js/offer/offer-list/ngPromotionListDirective.js',
        './src/main/webapp/resources/js/offer/offer-specifications/ngOfferSpecifications.js',
        './src/main/webapp/resources/js/header-display/tutorial/ngQuickTutorial.js',
        './src/main/webapp/resources/js/footer/ngFooterDirective.js',
        './src/main/webapp/resources/js/offer/offer-specifications/offer-comments/ngOfferComments.js',
        './src/main/webapp/resources/js/offer/offer-specifications/offer-comments/ngOfferCommentsQuotes.js',
        './src/main/webapp/resources/js/offer/offer-creation/ngOfferCreationDirective.js',
        './src/main/webapp/resources/js/offer/offer-creation/ngOfferUpdateDirective.js',
        './src/main/webapp/resources/js/offer/offer-creation/ngCompanyManagerDirective.js',
        './src/main/webapp/resources/js/offer/offer-creation/ngOfferCategoryManagerDirective.js',
        './src/main/webapp/resources/js/offer/offer-filter/ngOfferFilterDirective.js',
        './src/main/webapp/resources/js/header-display/ngHeadDisplayDirective.js',
        './src/main/webapp/resources/js/header-display/ngHeadAccountAndOfferManagementDirective.js',
        './src/main/webapp/resources/js/header-display/ngMainSlogan.js',
        './src/main/webapp/resources/js/uploader/ngImageUploadDirective.js',
        './src/main/webapp/resources/js/response/ngResponseFromServer.js',
        './src/main/webapp/resources/js/social/ngSocialMediaDirective.js',
        './src/main/webapp/resources/js/error/ngRedirectComponents.js',
        './src/main/webapp/resources/js/account/password-forgotten/ngPasswordForgottenDirective.js',
        './src/main/webapp/resources/js/analytics/loader.js'
    ],
    output: {
        path: 'src/main/webapp/resources/assets/js',
        filename: "bundle.js"
    },
    plugins: [
        new webpack.optimize.UglifyJsPlugin({
            minimize: true,
            compress: true,
            output: {
                comments: false
            }})
    ]
};
