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
                                    alertService.sendErrorMessage(data);
                                }).error(function (data, status, headers, config) {
                                    alertService.sendDefaultErrorMessage();
                                });
                            $scope.overheadDisplay = false;
                            $scope.resetValues();
                        }

                    };
                    $scope.bigDecimalsOnly = /^\-?\d+((\.|\,)\d+)?$/;
                    $scope.resetValues = function () {
                        vcRecaptchaService.reset();
                        $scope.offer = {};
                        $scope.brandNewCompany = false;
                        $scope.resetCategory = true;
                        $scope.resetCompany = true;

                    }
                }]
        }
    });