/**
 * Created by hasiermetal on 22/06/15.
 */
aporlaofertaApp
    .directive('ngResponseFromServer', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/response/serverResponse.html',
            scope: {
                theResponse: '='
            }
        }
    });