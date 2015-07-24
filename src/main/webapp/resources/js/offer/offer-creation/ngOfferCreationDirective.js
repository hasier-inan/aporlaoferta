/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngOfferCreation', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-creation/offerCreation.html',
            scope: {
                overheadDisplay: '='
            },
            controller: ['$scope', 'requestManager', 'configService', 'alertService', 'vcRecaptchaService',
                function ($scope, requestManager, configService, alertService, vcRecaptchaService) {
                    $scope.offer = {};
                    $scope.publicKey = "6LdqHQoTAAAAAAht2VhkrLGU26eBOjL-nK9zXxcn";
                    $scope.resetCategory = true;
                    $scope.createOffer = function () {
                        if (vcRecaptchaService.getResponse() === "") {
                            alertService.sendErrorMessage("Por favor, haga click en el captcha para demostrar que no es un robot");
                        }
                        else {
                            requestManager.makePostCall($scope.offer, {recaptcha:vcRecaptchaService.getResponse()}, configService.getEndpoint('create.offer'))
                                .success(function (data, status, headers, config) {
                                    alertService.sendErrorMessage(data.descriptionEsp);
                                    $scope.resetValues();
                                }).error(function (data, status, headers, config) {
                                    alertService.sendDefaultErrorMessage();
                                    vcRecaptchaService.reload();
                                });
                            $scope.overheadDisplay = false;
                        }
                    };
                    $scope.bigDecimalsOnly = /^\-?\d+((\.|\,)\d+)?$/;
                    $scope.resetValues = function () {
                        vcRecaptchaService.reload();
                        $scope.offer = {};
                        $scope.brandNewCompany = false;
                        $scope.resetCategory = true;
                        $scope.resetCompany = true;

                    }
                }]
        }
    });