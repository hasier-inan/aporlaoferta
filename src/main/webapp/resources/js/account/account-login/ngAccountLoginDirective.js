/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngAccountLogin', function () {
        return {
            restrict: 'A',
            templateUrl: 'view/login.jsp',
            controller: ['$scope', 'requestManager', 'configService', 'alertService',
                function ($scope, requestManager, configService, alertService) {
                    $scope.validMail = /^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/;
                    $scope.requestPassword = function (userEmail) {
                        $scope.processing = true;

                        requestManager.makePostCall(userEmail, {userEmail: userEmail}, configService.getEndpoint('password.forgotten.request'))
                            .success(function (data, status, headers, config) {
                                $scope.processAccountResponse(data);
                            }).error(function (data, status, headers, config) {
                                $scope.accountDefaultError();
                            }).finally(function () {
                                $scope.processing = false;
                            });
                    }
                    $scope.processAccountResponse = function (data) {
                        alertService.sendErrorMessage(data.descriptionEsp);
                        if ($scope.restartRecaptcha) {
                            $scope.restartRecaptcha();
                        }
                    }
                    $scope.accountDefaultError = function () {
                        alertService.sendDefaultErrorMessage();
                    }

                }]
        }
    });