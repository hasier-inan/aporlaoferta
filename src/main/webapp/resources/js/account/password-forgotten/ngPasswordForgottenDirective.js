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
                    $scope.validPassword = /^(?=.*?)(?=.*?[a-z])(?=.*?[0-9])(?=.*?).{8,}$/;
                    $scope.theUser = {userNickname: $scope.nick, track: $scope.uuid};
                    $scope.updatePassword = function (theUser) {
                        $scope.processing=true;
                        requestManager.makePostCall(theUser, {}, configService.getEndpoint('password.forgotten'))
                            .success(function (data) {
                                $scope.processPasswordResponse(data)
                            }).error(function () {
                                alertService.sendDefaultErrorMessage();
                            }).finally(function(){
                                $scope.processing=false;
                            });
                        $scope.theUser = {userNickname: $scope.nick, track: $scope.uuid};
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