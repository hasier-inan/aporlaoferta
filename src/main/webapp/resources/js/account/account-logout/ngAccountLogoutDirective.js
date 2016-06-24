/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngAccountLogout', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/account/account-logout/profile.jsp',
            scope: {
                accountUpdate: '='
            },
            controller: ['$scope', 'alertService', 'requestManager', 'configService', function ($scope, alertService, requestManager, configService) {
                $scope.profileOptions = [
                    {text: 'Actualizar perfil', value: 'accountUpdate'},
                    {text: 'Cerrar sesión', value: 'formSubmit'}
                ];
                $scope.defaultProfileModel = {
                    text: "Opciones"
                };
                $scope.profileModel = angular.copy($scope.defaultProfileModel);

                $scope.$watch('profileModel', function () {
                    if ($scope.profileModel.text !== "Opciones") {
                        $scope[$scope.profileModel.value]();
                        $scope.profileModel = angular.copy($scope.defaultProfileModel);
                    }
                });

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

                $scope.formSubmit = function () {
                    $scope.logoutForm.submit();
                }

                $scope.$on('userAvatar', function (event, args) {
                    $scope.userAvatar = args.userAvatar;
                });

                $scope.formSubmit = function () {
                    document.getElementById("logoutForm").submit();
                }
            }]
        }
    });
