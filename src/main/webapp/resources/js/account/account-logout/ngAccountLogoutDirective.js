/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngAccountLogout', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/account/account-logout/profile.jsp',
            controller: ['$scope', 'alertService', 'requestManager', 'configService', function ($scope, alertService, requestManager, configService) {

                $scope.getUserDetails = function () {
                    requestManager.makePostCall({}, {}, configService.getEndpoint('get.account.details'))
                        .success(function (data, status, headers, config) {
                            $scope.userAvatar = data.userAvatar;
                        }).error(function (data, status, headers, config) {
                            alertService.sendErrorMessage("No se ha podido obtener la información del usuario");
                            $scope.customCloseCallback = false;
                        });
                }

                $scope.getUserDetails();

                $scope.$on('userAvatar', function (event, args) {
                    $scope.userAvatar = args.userAvatar;
                });
            }]
        }
    })
;