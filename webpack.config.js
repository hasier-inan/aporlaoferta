const webpack = require('webpack');
const autoPrefixer = require('autoprefixer');
const ExtractTextPlugin = require("extract-text-webpack-plugin");

module.exports = {
    entry: [
        './src/main/webapp/resources/js/request-handler/requestManagerService.js',
        './src/main/webapp/resources/js/config/configService.js',
        './src/main/webapp/resources/js/response/alertService.js',
        './src/main/webapp/resources/js/offer/offer-list/offerManagerService.js',
        './src/main/webapp/resources/js/offer/offer-specifications/offerHelper.js',
        './src/main/webapp/resources/js/main-controller/mainController.js',
        './src/main/webapp/resources/js/account/account-signup/ngAccountCreationDirective.js',
        './src/main/webapp/resources/js/account/account-signup/ngAccountUpdateDirective.js',
        './src/main/webapp/resources/js/account/account-signup/pwdCheck.js',
        './src/main/webapp/resources/js/account/account-signup/accountHelper.js',
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
        './src/main/webapp/resources/js/offer/offer-creation/ngOfferCategoryManagerDirective.js',
        './src/main/webapp/resources/js/offer/offer-filter/ngOfferFilterDirective.js',
        './src/main/webapp/resources/js/offer/offer-filter/ngDummyOfferFilterDirective.js',
        './src/main/webapp/resources/js/header-display/ngHeadDisplayDirective.js',
        './src/main/webapp/resources/js/header-display/ngHeadAccountAndOfferManagementDirective.js',
        './src/main/webapp/resources/js/header-display/ngMainSlogan.js',
        './src/main/webapp/resources/js/uploader/ngImageUploadDirective.js',
        './src/main/webapp/resources/js/response/ngResponseFromServer.js',
        './src/main/webapp/resources/js/social/ngSocialMediaDirective.js',
        './src/main/webapp/resources/js/error/ngRedirectComponents.js',
        './src/main/webapp/resources/js/account/password-forgotten/ngPasswordForgottenDirective.js',
        './src/main/webapp/resources/sass/loader.js'
    ],
    output: {
        path: 'src/main/webapp/resources/assets/js',
        filename: "bundle.js"
    },
    devtool: 'source-map',
    module: {
        loaders: [
            {
                test: /\.scss$/,
                exclude: /node_modules/,
                loader: ExtractTextPlugin.extract("style-loader", "css-loader!postcss-loader!sass-loader")
            },
            {
                test: /\.(jpg|jpeg|gif|png|woff|woff2|eot|ttf|svg)$/,
                exclude: /node_modules/,
                loader: 'url-loader?limit=1024&name=../css/images/[name].[ext]'
            }
        ]
    },
    plugins: [
        new webpack.optimize.UglifyJsPlugin({
            minimize: true,
            compress: true,
            output: {
                comments: false
            }}),
        new ExtractTextPlugin('../css/styles.css', {
            allChunks: true
        })
    ],
    postcss: [autoPrefixer({browsers: ['last 10 versions']})],
    stats: {
        colors: true
    },
    sassLoader: {
        outputStyle: 'compressed',
        includePaths: ['node_modules']
    }

};
