/**
 * Created by hasiermetal on 19/06/15.
 */
aporlaofertaApp
    .directive('ngImageUploader', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/uploader/imageUpload.html',
            link: function (scope, elem, attrs) {
            },
            controller: ['$scope', function ($scope) {

            }]
        }
    });