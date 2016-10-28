!function(e){function t(o){if(r[o])return r[o].exports;var n=r[o]={exports:{},id:o,loaded:!1};return e[o].call(n.exports,n,n.exports,t),n.loaded=!0,n.exports}var r={};return t.m=e,t.c=r,t.p="",t(0)}([function(e,t,r){r(1),r(2),r(3),r(4),r(5),r(6),r(7),r(8),r(9),r(10),r(11),r(12),r(13),r(14),r(15),r(16),r(17),r(18),r(19),r(20),r(21),r(22),r(23),r(24),r(25),r(26),r(27),r(28),r(29),r(30),e.exports=r(31)},function(e,t){var r=angular.module("requestManager",[]);r.service("requestManager",["$http",function(e){var t,r={};return r.initMainController=function(){t=$("meta[name='_csrf']").attr("content")},r.makePostCall=function(r,o,n){return o._csrf=t,e({method:"POST",url:n,headers:{"Content-Type":"application/json","X-CSRF-TOKEN":t},params:o,data:r})},r.getActualToken=function(){return t},r.initMainController(),r}])},function(e,t){var r=angular.module("configService",[]);r.service("configService",[function(){var e={},t="getOffers",r="getHottestOffers",o="getOffer",n="createOffer",a="updateOffer",i="expireOffer",s="removeOffer",c="createUser",l="updateUser",f="banUser",u="accountDetails",p="companyList",d="getOfferCategories",g="votePositive",m="voteNegative",C="createComment",y="deleteComment",h="quoteComment",v="getFilteredOffers",A="forgottenPassword",b="requestForgottenPassword",k="aporlaoferta-tut",E="aporlaoferta-cle",w=4500;e.getEndpoint=function(e){return O[e]};var O={"get.offers":t,"get.hottest.offers":r,"get.offer":o,"create.offer":n,"update.offer":a,"expire.offer":i,"remove.offer":s,"create.account":c,"update.account":l,"get.account.details":u,"get.companies":p,"get.offer.categories":d,"ban.user":f,"positive.feedback":g,"negative.feedback":m,"create.comment":C,"delete.comment":y,"quote.comment":h,"get.filtered.offers":v,"password.forgotten":A,"password.forgotten.request":b,"max.image.size":w,"tutorial.cookie":k,"law.cookie":E};return e}])},function(e,t){var r=angular.module("alertService",[]);r.service("alertService",["$rootScope",function(e){var t={},r="No se ha podido ejecutar la operación";return t.isAllOk=function(e){return 0==e.code},t.sendErrorMessage=function(t){var r={};r.descriptionEsp=t,r.responseResult="error",e.$broadcast("serverResponse",r)},t.sendDefaultErrorMessage=function(){var r={};r.descriptionEsp=t.getDefaultMessage(),r.responseResult="error",e.$broadcast("serverResponse",r)},t.getDefaultMessage=function(){return r},t}])},function(e,t){var r=angular.module("offerManager",[]);r.service("offerManager",["$rootScope","alertService","requestManager","configService",function(e,t,r,o){var n={};return n.requestNewestOffers=function(e){e.hot=!1,n.requestMoreOffers(e,void 0,n.broadcastOfferList,function(){})},n.requestHottestOffers=function(e){e.hot=!0,n.requestMoreOffers(e,void 0,n.broadcastOfferList,function(){})},n.broadcastOfferList=function(t){e.$broadcast("offerList",t)},n.requestMoreOffers=function(e,n,a,i){r.makePostCall(e,{number:n},o.getEndpoint("get.filtered.offers")).success(function(e,t,r,o){a(e.theOffers)}).error(function(e,r,o,n){t.sendDefaultErrorMessage(),i()})},n.showSpecifications=function(n){r.makePostCall({},{id:n},o.getEndpoint("get.offer")).success(function(t,r,o,n){e.$broadcast("offerSpecifications",t.theOffers)}).error(function(e,r,o,n){t.sendDefaultErrorMessage()})},n}])},function(e,t){aporlaofertaApp=angular.module("aPorLaOferta",["angular.filter","requestManager","alertService","configService","flow","ngAnimate","vcRecaptcha","offerManager","pwCheckModule","ngMaterial","ngMessages","ngDropdowns","truncate","angular-cookie-law","ngCookies","slick"]),aporlaofertaApp.controller("APorLaOfertaController",["$scope","$rootScope",function(e,t){e.keyHandler=function(e){t.$broadcast("keydownControl",e.keyCode)},e.time=(new Date).getTime(),angular.element(document.body).bind("mousemove keypress",function(){e.time=(new Date).getTime()}),e.minute=6e4,e.refresh=function(){(new Date).getTime()-e.time>=20*e.minute?window.location.reload():setTimeout(e.refresh,10*e.minute)},setTimeout(e.refresh,10*e.minute)}]),aporlaofertaApp.directive("ngConfirmClick",[function(){return{link:function(e,t,r){var o=r.ngConfirmClick||"",n=r.confirmedClick;t.bind("click",function(t){window.confirm(o)&&e.$eval(n)})}}}]),aporlaofertaApp.config(["flowFactoryProvider",function(e){e.defaults={target:"uploadImage",permanentErrors:[404,500,501],maxChunkRetries:1,chunkRetryInterval:5e3,simultaneousUploads:4,singleFile:!0}}])},function(e,t){aporlaofertaApp.directive("ngAccountCreation",function(){return{restrict:"A",templateUrl:"resources/js/account/account-signup/createAccount.jsp",scope:{overheadDisplay:"=",customCloseCallback:"=",displayCallback:"="},controller:["$scope","requestManager","configService","vcRecaptchaService","alertService",function(e,t,r,o,n){e.theUser={},e.widgetId=null,e.publicKey="6LdqHQoTAAAAAAht2VhkrLGU26eBOjL-nK9zXxcn",e.disableNickname=!1,e.passwordUpdateRequired=!0,e.validMail=/^[_a-z0-9-]+(\.[_a-z0-9-]+)*(\+[a-z0-9-]+)?@[a-z]+\.[a-z.]{2,5}$/,e.validPassword=/^(?=.*?)(?=.*?[a-z])(?=.*?[0-9])(?=.*?).{8,}$/,e.createAccount=function(n){""===o.getResponse(e.widgetId)?e.displayErrorMessageAndDisplayAccount():(delete e.theUser.oldPassword,e.processing=!0,t.makePostCall(n,{recaptcha:o.getResponse(e.widgetId)},r.getEndpoint("create.account")).success(function(t,r,o,n){e.processAccountResponse(t)}).error(function(t,r,o,n){e.accountDefaultError()})["finally"](function(){e.processing=!1}),e.theUser={},e.userPassword2="")},e.displayErrorMessageAndDisplayAccount=function(){n.sendErrorMessage("Por favor, haga click en el captcha para demostrar que no es un robot"),e.restartRecaptcha(),e.customCloseCallback=e.displayCallback},e.processAccountResponse=function(t){n.isAllOk(t)?(n.sendErrorMessage(t.descriptionEsp),e.customCloseCallback=!1):e.accountError(t.descriptionEsp),e.restartRecaptcha()},e.restartRecaptcha=function(){o.reload(e.widgetId)},e.accountDefaultError=function(){n.sendDefaultErrorMessage(),o.reload(e.widgetId),e.customCloseCallback=e.displayCallback},e.accountError=function(t){n.sendErrorMessage(t),e.restartRecaptcha(),e.customCloseCallback=e.displayCallback},e.setWidgetId=function(t){e.widgetId=t}}]}})},function(e,t){aporlaofertaApp.directive("ngAccountUpdate",function(){return{restrict:"A",templateUrl:"resources/js/account/account-signup/createAccount.jsp",scope:{overheadDisplay:"=",customCloseCallback:"=",displayCallback:"="},controller:["$scope","vcRecaptchaService","alertService","$http","requestManager","configService","$rootScope",function(e,t,r,o,n,a,i){e.disableNickname=!0,e.theUser={},e.widgetId=null,e.passwordUpdateRequired=!1,e.validMail=/^[_a-z0-9-]+(\.[_a-z0-9-]+)*(\+[a-z0-9-]+)?@[a-z]+\.[a-z.]{2,5}$/,e.validPassword=/^(?=.*?)(?=.*?[a-z])(?=.*?[0-9])(?=.*?).{8,}$/,e.publicKey="6LdqHQoTAAAAAAht2VhkrLGU26eBOjL-nK9zXxcn",e.createAccount=function(r){""===t.getResponse(e.widgetId)?e.displayErrorMessageAndDisplayAccount():(e.processing=!0,n.makePostCall(r,{recaptcha:t.getResponse(e.widgetId)},a.getEndpoint("update.account")).success(function(t,o,n,a){e.processAccountResponse(t)&&i.$broadcast("userAvatar",angular.copy(r))}).error(function(t,r,o,n){e.accountDefaultError()})["finally"](function(){e.processing=!1,e.getUserDetails(),e.passwordUpdateRequired=!1}))},e.processLogin=function(){i.$broadcast("userLoginRequest")},e.getUserDetails=function(){n.makePostCall({},{},a.getEndpoint("get.account.details")).success(function(t,r,o,n){e.theUser=t}).error(function(t,o,n,a){r.sendErrorMessage("No se ha podido obtener la información del usuario"),e.customCloseCallback=!1})},e.getUserDetails(),e.resetPassword=function(){e.theUser.oldPassword=null,e.theUser.userPassword=null,e.userPassword2=null},e.displayErrorMessageAndDisplayAccount=function(){r.sendErrorMessage("Por favor, haga click en el captcha para demostrar que no es un robot"),e.restartRecaptcha(),e.customCloseCallback=e.displayCallback},e.processAccountResponse=function(t){return r.isAllOk(t)?(r.sendErrorMessage(t.descriptionEsp),!0):(e.accountError(t.descriptionEsp),e.restartRecaptcha(),!1)},e.accountDefaultError=function(){r.sendDefaultErrorMessage(),e.restartRecaptcha(),e.customCloseCallback=e.displayCallback},e.accountError=function(t){r.sendErrorMessage(t),e.restartRecaptcha(),e.customCloseCallback=e.displayCallback},e.setWidgetId=function(t){e.widgetId=t},e.restartRecaptcha=function(){t.reload(e.widgetId)}}]}})},function(e,t){angular.module("pwCheckModule",[]).directive("pwCheck",[function(){return{require:"ngModel",link:function(e,t,r,o){var n="#"+r.pwCheck;t.add(n).on("keyup",function(){e.$apply(function(){var e=t.val()===$(n).val();o.$setValidity("pwmatch",e)})})}}}])},function(e,t){aporlaofertaApp.directive("ngAccountLogin",function(){return{restrict:"A",templateUrl:"view/login.jsp",controller:["$scope","requestManager","configService","alertService",function(e,t,r,o){e.validMail=/^[_a-z0-9-]+(\.[_a-z0-9-]+)*(\+[a-z0-9-]+)?@[a-z]+\.[a-z.]{2,5}$/,e.processLogin=function(){e.processing=!0,angular.element("#loginForm").trigger("submit")},e.requestPassword=function(o){e.processing=!0,t.makePostCall(o,{userEmail:o},r.getEndpoint("password.forgotten.request")).success(function(t,r,o,n){e.processAccountResponse(t)}).error(function(t,r,o,n){e.accountDefaultError()})["finally"](function(){e.processing=!1})},e.processAccountResponse=function(t){o.sendErrorMessage(t.descriptionEsp),e.restartRecaptcha&&e.restartRecaptcha()},e.accountDefaultError=function(){o.sendDefaultErrorMessage()}}]}})},function(e,t){aporlaofertaApp.directive("ngAccountLogout",function(){return{restrict:"A",templateUrl:"resources/js/account/account-logout/profile.jsp",scope:{accountUpdate:"="},controller:["$scope","alertService","requestManager","configService",function(e,t,r,o){e.profileOptions=[{text:"Actualizar perfil",value:"accountUpdate"},{text:"Cerrar sesión",value:"formSubmit"}],e.defaultProfileModel={text:"Opciones"},e.profileModel=angular.copy(e.defaultProfileModel),e.$watch("profileModel",function(){"Opciones"!==e.profileModel.text&&(e[e.profileModel.value](),e.profileModel=angular.copy(e.defaultProfileModel))}),e.getUserDetails=function(){r.makePostCall({},{},o.getEndpoint("get.account.details")).success(function(t,r,o,n){e.userAvatar=t.userAvatar}).error(function(r,o,n,a){t.sendErrorMessage("No se ha podido obtener la información del usuario"),e.customCloseCallback=!1})},e.getUserDetails(),e.formSubmit=function(){e.logoutForm.submit()},e.$on("userAvatar",function(t,r){e.userAvatar=r.userAvatar}),e.formSubmit=function(){document.getElementById("logoutForm").submit()}}]}})},function(e,t){aporlaofertaApp.directive("ngOfferList",function(){return{restrict:"A",templateUrl:"resources/js/offer/offer-list/offerList.html",scope:{defaultList:"="},controller:["$scope","offerManager",function(e,t){e.offerList=[],e.moreOffersLoading=!0,e.lastOffer=0,e.showMoreOffers=function(r){e.moreOffersLoading=!0,e.offerFilter.hot="hottestOffers"===e.defaultList,e.isCategorySelected()||(e.offerFilter.selectedcategory=""),t.requestMoreOffers(e.offerFilter,r,function(t){e.offerList=e.offerList.concat(t),e.moreOffersLoading=!1},function(){e.moreOffersLoading=!1})},e.updateIndex=function(t){e.lastOffer=t},e.$on("offerList",function(t,r){e.moreOffersLoading=!1,e.offerList=r}),e.showSpecifications=function(e){t.showSpecifications(e)},e.initialiseScrollyButtons=function(){$(".scrolly").scrolly({speed:1500})},e.parsePrice=function(e){if(e||0==e)return e.toString().replace(/\./,",")},e.isCategorySelected=function(){return!e.isEmptyCategory(e.offerFilter.selectedcategory)},e.isEmptyCategory=function(e){return["",null,void 0,"Categoría","Todas","CATEGORÍA","TODAS"].indexOf(e)>-1},e.offerFeedbackStyle=function(e){var t=e.offerPositiveVote-e.offerNegativeVote;return t>0&&t<=100?"hotFeedback":t>100?"veryHotFeedback":t<0?"coldFeedback":"neutralFeedback"},e.$on("appliedOfferFilters",function(){e.moreOffersLoading=!0}),e.$watch("offerList",function(){e.moreOffersLoading=!1}),e.initialiseScrollyButtons()}]}})},function(e,t){aporlaofertaApp.directive("ngPromotionList",function(){return{restrict:"A",templateUrl:"resources/js/offer/offer-list/promotionList.html",controller:["$scope","$rootScope","offerManager",function(e,t,r){e.shareUrl="www.aporlaoferta.com",e.shareText=$("meta[property='og:description']").attr("content"),e.defaultList="",e.appliedOfferFilters={},e.$on("appliedOfferFilters",function(t,r){e.appliedOfferFilters=r}),e.requestNewestOffers=function(){r.requestNewestOffers(e.appliedOfferFilters),e.defaultList="newestOffers"},e.requestHottestOffers=function(){r.requestHottestOffers(e.appliedOfferFilters),e.defaultList="hottestOffers"},e.displayTutorialModal=function(){t.$broadcast("displayTutorial")}}]}})},function(e,t){aporlaofertaApp.directive("ngOfferSpecifications",function(){return{restrict:"A",templateUrl:"resources/js/offer/offer-specifications/offerSpecifications.jsp",scope:{theOffer:"=",customCloseCallback:"="},controller:["offerManager","alertService","$scope","$rootScope","requestManager","configService",function(e,t,r,o,n,a){r.sharePrefix="www.aporlaoferta.com/start?sh=",r.sharePrice="€: ",r.commentsCustomCloseCallback={},r.appliedOfferFilters={},r.votePositive=function(e){n.makePostCall({},{offerId:e},a.getEndpoint("positive.feedback")).success(function(t){r.sendMessageAndShowSpecifications(t,e,"offerPositiveVote")}).error(function(){r.sendDefaultErrorMessageAndShowSpecifications(e)})},r.voteNegative=function(e){n.makePostCall({},{offerId:e},a.getEndpoint("negative.feedback")).success(function(t){r.sendMessageAndShowSpecifications(t,e,"offerNegativeVote")}).error(function(){r.sendDefaultErrorMessageAndShowSpecifications(e)})},r.sendMessageAndShowSpecifications=function(o,n,a){t.isAllOk(o)?r.theOffer[0][a]=r.theOffer[0][a]+1:(t.sendErrorMessage(o.descriptionEsp),r.customCloseCallback=function(){e.showSpecifications(n)})},r.sendDefaultErrorMessageAndShowSpecifications=function(o){t.sendDefaultErrorMessage(),r.customCloseCallback=function(){e.showSpecifications(o)}},r.updateOffer=function(e){o.$broadcast("updateTheOffer",angular.copy(e))},r.expireOffer=function(o){n.makePostCall({},{id:o.id},a.getEndpoint("expire.offer")).success(function(o){t.isAllOk(o)?(t.sendErrorMessage(o.descriptionEsp),r.customCloseCallback=function(){e.requestNewestOffers(r.appliedOfferFilters)}):t.sendErrorMessage(o.descriptionEsp)}).error(function(){t.sendDefaultErrorMessage()})},r.banUser=function(e){n.makePostCall({},{nickname:e},a.getEndpoint("ban.user")).success(function(e){t.sendErrorMessage(e.descriptionEsp)}).error(function(){t.sendDefaultErrorMessage()})},r.removeOffer=function(o){n.makePostCall({},{id:o},a.getEndpoint("remove.offer")).success(function(o){t.sendErrorMessage(o.descriptionEsp),r.customCloseCallback=function(){e.requestNewestOffers(r.appliedOfferFilters)}}).error(function(){t.sendDefaultErrorMessage()})},r.parsePrice=function(e){if(e||0==e)return e.toString().replace(/\./,",")},r.offerFeedbackStyle=function(e){var t=e.offerPositiveVote-e.offerNegativeVote;return t>0&&t<=100?"hotFeedback":t>100?"veryHotFeedback":t<0?"coldFeedback":"neutralFeedback"},r.processLogin=function(){o.$broadcast("userLoginRequest")},r.processRegister=function(){o.$broadcast("userRegisterRequest")},r.$on("commentsCustomCloseCallback",function(e,t){var o=t;r.customCloseCallback=o}),r.$on("appliedOfferFilters",function(e,t){r.appliedOfferFilters=t})}]}})},function(e,t){aporlaofertaApp.directive("ngQuickTutorial",function(){return{restrict:"A",scope:{isDisplayed:"="},templateUrl:"resources/js/header-display/tutorial/quickTutorial.jsp",controller:["$scope","$element",function(e,t){e.shareUrl="www.aporlaoferta.com",e.shareText=$("meta[property='og:description']").attr("content"),e.$watch("isDisplayed",function(){e.isDisplayed&&(t.resize(),t.find(".slick-dots li:first a").trigger("click"),e.isDisplayed=0)})}]}})},function(e,t){aporlaofertaApp.directive("ngFooter",function(){return{restrict:"A",templateUrl:"resources/js/footer/footer.jsp",controller:["$scope","$rootScope",function(e,t){e.displayTutorialModal=function(){t.$broadcast("displayTutorial")}}]}})},function(e,t){aporlaofertaApp.directive("ngOfferComments",function(){return{restrict:"A",templateUrl:"resources/js/offer/offer-specifications/offer-comments/offerComments.jsp",scope:{theComments:"=",theOffer:"=",commentsCustomCloseCallback:"="},controller:["$filter","alertService","$scope","$rootScope","requestManager","configService","offerManager",function(e,t,r,o,n,a,i){r.quoteActionEnable=-1,r.qComment={},r.comment={},r.getQuotedComment=function(t,r){return e("filter")(r,{id:t})[0]},r.quoteAction=function(e){r.quoteActionEnable==e?r.quoteActionEnable=-1:r.quoteActionEnable=e},r.isQuoteActionEnabled=function(e){return r.quoteActionEnable==e},r.writeComment=function(e,t){r.processing=!0,n.makePostCall(e,{offer:t},a.getEndpoint("create.comment")).success(function(e,t,o,n){r.sendMessageAndShowSpecifications(e.descriptionEsp)}).error(function(e,t,o,n){r.sendDefaultErrorMessageAndShowSpecifications()})["finally"](function(){r.processing=!1})},r.quoteComment=function(e,t){r.processing=!0,n.makePostCall(e,{quotedComment:t},a.getEndpoint("quote.comment")).success(function(e,t,o,n){r.sendMessageAndShowSpecifications(e.descriptionEsp)}).error(function(e,t,o,n){r.sendDefaultErrorMessageAndShowSpecifications()})["finally"](function(){r.processing=!1})},r.sendMessageAndShowSpecifications=function(e){r.restartCommentTexts(),i.showSpecifications(r.theOffer)},r.sendDefaultErrorMessageAndShowSpecifications=function(){t.sendDefaultErrorMessage(),r.updateViewAndCloseCallback()},r.updateViewAndCloseCallback=function(){r.commentsCustomCloseCallback=function(){i.showSpecifications(r.theOffer)},o.$broadcast("commentsCustomCloseCallback",r.commentsCustomCloseCallback),r.restartCommentTexts()},r.restartCommentTexts=function(){r.comment.commentText="",r.qComment.commentText=""},r.processLogin=function(){o.$broadcast("userLoginRequest")},r.processRegister=function(){o.$broadcast("userRegisterRequest")},r.deleteComment=function(e){n.makePostCall({},{comment:e},a.getEndpoint("delete.comment")).success(function(e){r.sendMessageAndShowSpecifications(e.descriptionEsp)}).error(function(){r.sendDefaultErrorMessageAndShowSpecifications()})},r.banUser=function(e){n.makePostCall({},{nickname:e},a.getEndpoint("ban.user")).success(function(e){t.sendErrorMessage(e.descriptionEsp)}).error(function(){t.sendErrorMessage(data.descriptionEsp)})}}]}})},function(e,t){aporlaofertaApp.directive("ngOfferCommentsQuotes",function(){return{restrict:"A",templateUrl:"resources/js/offer/offer-specifications/offer-comments/commentsQuotes.jsp",scope:{theComments:"=",quotedComment:"="},controller:["$rootScope","$scope","requestManager","configService",function(e,t,r,o){t.quoteActionEnable=-1,t.quoteAction=function(e){t.quoteActionEnable==e?t.quoteActionEnable=-1:t.quoteActionEnable=e},t.isQuoteActionEnabled=function(e){return t.quoteActionEnable==e},t.quoteComment=function(n,a){r.makePostCall(n,{quotedComment:a},o.getEndpoint("quote.comment")).success(function(r,o,n,a){e.$broadcast("serverResponse",r),t.qComment.commentText=""}).error(function(t,r,o,n){var a={};a.description=t,a.responseResult="error",e.$broadcast("serverResponse",a)})}}]}})},function(e,t){aporlaofertaApp.directive("ngOfferCreation",function(){return{restrict:"A",templateUrl:"resources/js/offer/offer-creation/offerCreation.html",scope:{overheadDisplay:"=",customCloseCallback:"=",displayCallback:"="},controller:["$scope","offerManager","requestManager","configService","alertService","vcRecaptchaService",function(e,t,r,o,n,a){e.offer={},e.widgetId=null,e.publicKey="6LdqHQoTAAAAAAht2VhkrLGU26eBOjL-nK9zXxcn",e.resetCategory=!0,e.appliedOfferFilters={},e.createOffer=function(){""===a.getResponse(e.widgetId)?(e.offerCreationError("Por favor, haga click en el captcha para demostrar que no es un robot"),a.reload(e.widgetId)):(e.processing=!0,r.makePostCall(e.parseOffer(),{recaptcha:a.getResponse(e.widgetId)},o.getEndpoint("create.offer")).success(function(r,o,a,i){n.isAllOk(r)?(n.sendErrorMessage(r.descriptionEsp),e.resetValues(),e.customCloseCallback=function(){t.requestNewestOffers(e.appliedOfferFilters)}):e.offerCreationError(r.descriptionEsp)}).error(function(t,r,o,a){e.offerCreationError(n.getDefaultMessage())})["finally"](function(){e.processing=!1}))},e.$on("appliedOfferFilters",function(t,r){e.appliedOfferFilters=r}),e.parseOffer=function(){var t=angular.copy(e.offer);try{t.originalPrice=parseFloat(t.originalPrice.replace(/,/,"."))}catch(r){}try{t.finalPrice=parseFloat(t.finalPrice.replace(/,/,"."))}catch(r){}return t},e.restartRecaptcha=function(){a.reload(e.widgetId)},e.offerCreationError=function(t){n.sendErrorMessage(t),e.restartRecaptcha(),e.customCloseCallback=e.displayCallback},e.bigDecimalsOnly=/^\-?\d+((\.|\,)\d+)?$/,e.resetValues=function(){e.restartRecaptcha(),e.offer={},e.brandNewCompany=!1,e.resetCategory=!0,e.resetCompany=!0},e.setWidgetId=function(t){e.widgetId=t},e.isCompanyDefined=function(){if(e.offer)return void 0!=e.offer.offerCompany&&""!=e.selectedcompany},e.isCategorySelected=function(){return!e.isEmptyCategory(e.offer.offerCategory)},e.isEmptyCategory=function(e){return["",null,void 0,"Categoría","Todas","CATEGORÍA","TODAS"].indexOf(e)>-1},e.selectionPerformed=function(){return e.isCompanyDefined()&&e.isCategorySelected()}}]}})},function(e,t){aporlaofertaApp.directive("ngOfferUpdate",function(){return{restrict:"A",templateUrl:"resources/js/offer/offer-creation/offerCreation.html",scope:{overheadDisplay:"=",customCloseCallback:"=",displayCallback:"=",offer:"="},controller:["$scope","offerManager","requestManager","configService","alertService","vcRecaptchaService",function(e,t,r,o,n,a){e.widgetId=null,e.publicKey="6LdqHQoTAAAAAAht2VhkrLGU26eBOjL-nK9zXxcn",e.createOffer=function(){""===a.getResponse(e.widgetId)?(e.offerCreationError("Por favor, haga click en el captcha para demostrar que no es un robot"),a.restartRecaptcha()):(e.processing=!0,r.makePostCall(e.parseOffer(),{recaptcha:a.getResponse(e.widgetId)},o.getEndpoint("update.offer")).success(function(r,o,a,i){n.isAllOk(r)?(n.sendErrorMessage(r.descriptionEsp),e.resetValues(),e.customCloseCallback=function(){t.requestNewestOffers(e.appliedOfferFilters)}):e.offerCreationError(r.descriptionEsp)}).error(function(t,r,o,a){e.offerCreationError(n.getDefaultMessage())})["finally"](function(){e.processing=!1}))},e.parseOffer=function(){var t=angular.copy(e.offer);try{t.originalPrice=parseFloat(t.originalPrice.replace(/,/,"."))}catch(r){}try{t.finalPrice=parseFloat(t.finalPrice.replace(/,/,"."))}catch(r){}return t},e.restartRecaptcha=function(){a.reload(e.widgetId)},e.offerCreationError=function(t){n.sendErrorMessage(t),e.restartRecaptcha(),e.customCloseCallback=e.displayCallback},e.bigDecimalsOnly=/^\-?\d+((\.|\,)\d+)?$/,e.resetValues=function(){e.restartRecaptcha(),e.offer={},e.brandNewCompany=!1,e.resetCategory=!0,e.resetCompany=!0},e.setWidgetId=function(t){e.widgetId=t},e.isCompanyDefined=function(){if(e.offer)return void 0!=e.offer.offerCompany&&""!=e.selectedcompany},e.isCategorySelected=function(){return e.offer&&""!=e.offer.offerCategory&&null!=e.offer.offerCategory&&void 0!=e.offer.offerCategory&&"Categoría"!=e.offer.offerCategory&&"CATEGORÍA"!=e.offer.offerCategory},e.selectionPerformed=function(){return e.isCompanyDefined()&&e.isCategorySelected()}}]}})},function(e,t){aporlaofertaApp.directive("ngCompanyManager",function(){return{restrict:"A",templateUrl:"resources/js/offer/offer-creation/companyManagement.html",scope:{reset:"=",selectedcompany:"="},controller:["$rootScope","$scope","requestManager","configService",function(e,t,r,o){t.offerCompanies={},t.populateCompanyList=function(){r.makePostCall({},{},o.getEndpoint("get.companies")).success(function(e,r,o,n){t.offerCompanies=angular.copy(e)}).error(function(t,r,o,n){var a={};a.description=t,a.responseResult="error",e.$broadcast("serverResponse",a)})},t.searchCompanyChange=function(e){""!=e&&(t.selectedcompany={companyName:e})},t.selectedItemChange=function(e){t.selectedcompany=e},t.$watch("reset",function(){t.reset&&(t.selectedcompany="",t.searchCompany=""),t.reset=!1}),t.querySearch=function(e){if(t.offerCompanies.length>0)return e?t.offerCompanies.filter(t.createFilterFor(e)):t.offerCompanies},t.createFilterFor=function(e){var t=angular.lowercase(e);return function(e){return 0===angular.lowercase(e.companyName).indexOf(t)}},t.populateCompanyList()}]}})},function(e,t){aporlaofertaApp.directive("ngOfferCategoryManager",function(){return{restrict:"A",templateUrl:"resources/js/offer/offer-creation/offerCategoryManagement.html",scope:{reset:"=",selectedcategory:"=",includeAll:"="},controller:["$rootScope","$scope","requestManager","configService",function(e,t,r,o){t.offerCategories={},t.offerCategory={text:"CATEGORÍA",display:"Categoría"},t.populateAllCategories=function(){r.makePostCall({},{},o.getEndpoint("get.offer.categories")).success(function(e){t.offerCategories=angular.copy(e),t.includeAll&&t.offerCategories.push({text:"TODAS",value:"",display:"Todas"})}).error(function(t){var r={};r.description=t,r.responseResult="error",e.$broadcast("serverResponse",r)})},t.onCategoryChange=function(e){t.selectedcategory=e},t.populateAllCategories(),t.$watch("reset",function(){t.reset&&(t.offerCategory={text:"CATEGORÍA",display:"Categoría"}),t.reset=!1}),t.$watch("offerCategory",function(){t.selectedcategory=t.offerCategory.text}),t.isCategorySelected=function(){return""!=t.category&&null!=t.category&&void 0!=t.category}}]}})},function(e,t){aporlaofertaApp.directive("ngOfferFilter",function(){return{restrict:"A",templateUrl:"resources/js/offer/offer-filter/offerFilter.html",scope:{offerList:"=",selection:"=",offerFilter:"="},controller:["$scope","$rootScope","requestManager","configService",function(e,t,r,o){e.filter={},e.displayFilterContent="",e.previousCategory="",e.requestFilterApply=function(){e.processing=!0,e.filter.hot="hottestOffers"===e.selection,e.isCategorySelected()||(e.filter.selectedcategory=""),e.offerFilter=angular.copy(e.filter),e.offerFilter.text.length<3&&(e.offerFilter.text=""),t.$broadcast("appliedOfferFilters",angular.copy(e.offerFilter)),r.makePostCall(e.offerFilter,{},o.getEndpoint("get.filtered.offers")).success(function(t,r,o,n){e.offerList=t.theOffers}).error(function(e,r,o,n){var a={};a.description=e,a.responseResult="error",t.$broadcast("serverResponse",a)})["finally"](function(){e.processing=!1})},e.$watch("filter.dateRange",function(){e.requestFilterApply()}),e.searchCriteriaChanged=function(){e.offerFilter=angular.copy(e.filter),(e.filter.text.length>2||0==e.filter.text.length)&&e.requestFilterApply()},e.$watch("filter.selectedcategory",function(){e.isEmptyCategory(e.previousCategory)&&e.isEmptyCategory(e.filter.selectedcategory)||e.previousCategory===e.filter.selectedcategory||(e.previousCategory=e.filter.selectedcategory,e.requestFilterApply())}),e.cleanFilters=function(){e.filter.expired=!1,e.filter.text="",e.resetCategory=!0,e.filter.selectedcategory="",e.filter.selectedcategory="",e.filter.dateRange=1,t.$broadcast("appliedOfferFilters",angular.copy(e.offerFilter))},e.displayFilterContents=function(){e.displayFilterContent="filters-displayed"==e.displayFilterContent?"filters-hidden":"filters-displayed"},e.isCategorySelected=function(){return!e.isEmptyCategory(e.filter.selectedcategory)},e.isEmptyCategory=function(e){return["",null,void 0,"Categoría","Todas","CATEGORÍA","TODAS"].indexOf(e)>-1},e.cleanFilters()}]}})},function(e,t){aporlaofertaApp.directive("ngOverheadDisplay",function(){return{restrict:"A",templateUrl:"resources/js/header-display/headDisplay.jsp",scope:{specificOffer:"@",overheadVisible:"=",noAccounts:"="},controller:["$scope","offerManager","$timeout","$cookies","configService",function(e,t,r,o,n){e.customCloseCallback={},e.theResponse={},e.fullscreen=!1,e.tutorialIsDisplayed=!1,e.displayLogin=function(){e.setDefaultVisibility(),e.displayAccountLogin=!0,e.overheadVisible=!0},e.displaySignup=function(){e.setDefaultVisibility(),e.displayAccountCreation=!0,e.overheadVisible=!0},e.displayAccountUpdateForm=function(){e.setDefaultVisibility(),e.displayAccountUpdate=!0,e.overheadVisible=!0},e.displayOfferCreate=function(){e.setDefaultVisibility(),e.displayOfferCreation=!0,e.overheadVisible=!0},e.displayOfferUpdate=function(){e.setDefaultVisibility(),e.displayOfferToBeUpdate=!0,e.overheadVisible=!0},e.displayOfferDetails=function(){e.setDefaultVisibility(),e.displayOfferSpecifications=!0,e.overheadVisible=!0},e.displayServerResponse=function(){e.setDefaultVisibility(),e.displayResponseFromServer=!0,e.overheadVisible=!0},e.displayTutorialDiagram=function(){e.customCloseCallback=function(){e.setTutorialCookie()},e.setDefaultVisibility(),e.displayTutorial=!0,e.overheadVisible=!0,e.tutorialIsDisplayed=!0},e.setDefaultVisibility=function(){e.overheadVisible=!1,e.displayOfferCreation=!1,e.displayOfferToBeUpdate=!1,e.displayAccountLogin=!1,e.displayAccountCreation=!1,e.displayAccountUpdate=!1,e.displayOfferSpecifications=!1,e.displayResponseFromServer=!1,e.displayTutorial=!1,e.tutorialIsDisplayed=!1},e.closeOverheadDisplay=function(t){e.setDefaultVisibility(),"function"==typeof t&&(t(),e.customCloseCallback={})},e.$watch("specificOffer",function(){null!=/^\d+$/.exec(e.specificOffer)&&(e.fullscreen=!0,e.customCloseCallback=function(){e.fullscreen=!1},t.showSpecifications(e.specificOffer))}),e.$on("offerSpecifications",function(t,o){e.offerSpecifications=o,e.displayOfferDetails(),e.customCloseCallback=function(){r(function(){e.offerSpecifications=[]},0),e.fullscreen=!1}}),e.$on("updateTheOffer",function(t,r){e.offerSpecifications=[r],e.customCloseCallback=function(){e.displayOfferDetails()},e.displayOfferUpdate()}),e.$on("serverResponse",function(t,r){e.theResponse=r,e.displayServerResponse()}),e.$on("displayTutorial",function(){e.displayTutorialDiagram()}),e.$on("userLoginRequest",function(){e.displayLogin()}),e.$on("userRegisterRequest",function(){e.displaySignup()}),e.$on("keydownControl",function(t,r){var o=r;27==o&&e.justCloseOverheadDisplay()}),e.justCloseOverheadDisplay=function(){e.customCloseCallback={},e.closeOverheadDisplay(),e.fullscreen=!1},e.checkForErrors=function(){if(document.getElementById("errorMessage")){var t=document.getElementById("errorMessage").value;if(void 0!=typeof t&&""!=t){var r={};r.descriptionEsp=t,r.responseResult="error",e.theResponse=r,e.displayServerResponse()}}},e.setTutorialCookie=function(){var e=new Date;e.setDate(e.getDate()+365),o.put(n.getEndpoint("tutorial.cookie"),"true",{expires:e})},e.setDefaultVisibility(),e.checkForErrors(),r(function(){angular.element($("#overheadSubContainer")).removeClass("hiddencontainer")},100),"true"!=o.get(n.getEndpoint("tutorial.cookie"))&&r(function(){e.displayTutorialDiagram()},2e3)}]}})},function(e,t){aporlaofertaApp.directive("ngHeadAccountAndOfferManagement",["$window","$anchorScroll",function(e,t){return{restrict:"A",templateUrl:"resources/js/header-display/headAccountAndOfferManagement.jsp",scope:{displayLogin:"=",displaySignup:"=",displayOfferCreate:"=",displayAccountUpdateForm:"="},link:function(r){r.scrollPosition=!0,angular.element(e).bind("scroll",function(){r.scrollPosition=this.pageYOffset<=50,r.$apply()}),r.toTheTop=function(){t()}}}}])},function(e,t){aporlaofertaApp.directive("ngMainSlogan",function(){return{restrict:"A",templateUrl:"resources/js/header-display/mainSlogan.html",controller:["$scope","$timeout",function(e,t){t(function(){e.faded=!0},1200)}]}})},function(e,t){aporlaofertaApp.directive("ngImageUploader",function(){return{restrict:"A",scope:{finalUrl:"=",alreadyUploadedImage:"="},templateUrl:"resources/js/uploader/imageUpload.html",controller:["$rootScope","$scope","configService","$timeout",function(e,t,r,o){t.uploader={},t.displayThumbnail=!0,t.invalidSize=!1,t.invalidImage=!1,t.maxImageSize=r.getEndpoint("max.image.size"),t.alreadyUploadedImage="",t.fileAdded=function(e){var r=new FileReader;r.onload=function(e){var r=new Image;t.alreadyUploadedImage="",r.onerror=function(){
return t.displayInvalidImageMessage(),!1},r.onload=function(){return this.width>t.maxImageSize||this.height>t.maxImageSize?(t.displayInvalidImageSizeMessage(),!1):(t.uploader.flow.upload(),void t.defaultThumbnailView())},r.src=e.target.result},r.readAsDataURL(e.file)},t.deleteImage=function(){t.alreadyUploadedImage="",t.uploader.flow.cancel()},t.changeImage=function(){t.alreadyUploadedImage=""},t.alreadyUploaded=function(){return""!==t.alreadyUploadedImage},t.defaultThumbnailView=function(){t.displayThumbnail=!0,t.invalidImage=!1,t.invalidSize=!1},t.filesIsUploaded=function(e){var r=JSON.parse(e);0==r.code?t.finalUrl=r.description:t.handleUploadError(r)},t.handleUploadError=function(t){e.$broadcast("serverResponse",t)},t.$watch("finalUrl",function(e,r){""!==e&&"undefined"!=typeof e||t.uploader.flow.cancel()}),t.isInvalidThumbnailShown=function(){return t.invalidImage||t.invalidSize},t.displayInvalidImageSizeMessage=function(){t.defaultThumbnailView(),t.invalidSize=!0,t.displayThumbnail=!1,o(function(){t.uploader.flow.cancel()},500)},t.displayInvalidImageMessage=function(){t.defaultThumbnailView(),t.invalidImage=!0,t.displayThumbnail=!1,o(function(){t.uploader.flow.cancel()},500)}}]}})},function(e,t){aporlaofertaApp.directive("ngResponseFromServer",function(){return{restrict:"A",templateUrl:"resources/js/response/serverResponse.html",scope:{theResponse:"="}}})},function(e,t){aporlaofertaApp.directive("ngSocialMedia",function(){return{restrict:"A",templateUrl:"resources/js/social/socialMedia.html",scope:{shareUrl:"=",shareText:"="},controller:["$scope",function(e){e.facebookUrl="https://www.facebook.com/sharer/sharer.php?u="+e.shareUrl,e.twitterUrl="https://twitter.com/intent/tweet?url="+e.shareUrl+"&text="+e.shareText+"&via=aporlaoferta",e.googlePlusUrl="https://plus.google.com/share?url="+e.shareUrl}]}})},function(e,t){aporlaofertaApp.directive("ngRedirectComponents",function(){return{restrict:"A",templateUrl:"resources/js/error/redirectComponents.html"}})},function(e,t){aporlaofertaApp.directive("ngPasswordForgotten",function(){return{restrict:"A",templateUrl:"resources/js/account/password-forgotten/forgottenForm.html",scope:{uuid:"=",nick:"=",customCloseCallback:"="},controller:["$scope","$http","requestManager","configService","alertService","$timeout",function(e,t,r,o,n,a){e.validPassword=/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d!$%@#£€*?&]{8,}$/,e.theUser={userNickname:e.nick,uuid:e.uuid},e.updatePassword=function(t){e.processing=!0,r.makePostCall(t,{},o.getEndpoint("password.forgotten")).success(function(t,r,o,n){e.processPasswordResponse(t)}).error(function(e,t,r,o){n.sendDefaultErrorMessage()})["finally"](function(){e.processing=!1}),e.theUser={userNickname:e.nick,uuid:e.uuid}},e.processPasswordResponse=function(e){n.sendErrorMessage(e.descriptionEsp),n.isAllOk(e)&&a(function(){window.location.replace("/")},2e3)}}]}})},function(e,t,r){r(32)},function(e,t){}]);
//# sourceMappingURL=bundle.js.map