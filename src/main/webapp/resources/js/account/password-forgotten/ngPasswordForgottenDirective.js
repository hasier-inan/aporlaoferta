/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngPasswordForgotten', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/account/password-forgotten/forgottenForm.html',
            scope: {
                uuid: '=',
                nick: '=',
                customCloseCallback: '='
            },
            controller: ['$scope', '$http', 'requestManager', 'configService', 'alertService','$timeout',
                function ($scope, http, requestManager, configService, alertService,$timeout) {
                    $scope.validPassword = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d!$%@#£€*?&]{8,}$/;
                    $scope.theUser = {userNickname: $scope.nick, uuid: $scope.uuid};
                    $scope.updatePassword = function (theUser) {
                        $scope.processing=true;
                        requestManager.makePostCall(theUser, {}, configService.getEndpoint('password.forgotten'))
                            .success(function (data, status, headers, config) {
                                $scope.processPasswordResponse(data)
                            }).error(function (data, status, headers, config) {
                                alertService.sendDefaultErrorMessage();
                            }).finally(function(){
                                $scope.processing=false;
                            });
                        $scope.theUser = {userNickname: $scope.nick, uuid: $scope.uuid};
                    }
                    $scope.processPasswordResponse = function (data) {
                        alertService.sendErrorMessage(data.descriptionEsp);
                        if(alertService.isAllOk(data)){
                            $timeout(function () {
                                window.location.replace("/");
                            }, 2000);
                        }
                    }
                }]
        }
    });