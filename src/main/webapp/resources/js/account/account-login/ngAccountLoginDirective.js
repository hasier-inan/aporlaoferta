/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngAccountLogin', function () {
        return {
            restrict: 'A',
            templateUrl: 'view/login.jsp',
            link: function (scope, elem, attrs) {},
            controller:['$scope', 'accountController', function ($scope, accountController) {

            }]
        }
    });