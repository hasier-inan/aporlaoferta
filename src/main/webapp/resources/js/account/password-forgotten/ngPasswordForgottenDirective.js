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
            link: function (scope, elem, attrs) {
            },
            controller: ['$scope', '$http', 'requestManager', 'configService', 'alertService',
                function ($scope, http, requestManager, configService, alertService) {
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
                        if(alertService.isAllOk(data)){
                            $scope.customCloseCallback = function(){
                                window.location.replace("/aporlaoferta/");
                            };
                        }
                        alertService.sendErrorMessage(data.descriptionEsp);
                    }
                }]
        }
    });