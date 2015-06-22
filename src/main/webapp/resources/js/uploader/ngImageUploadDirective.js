/**
 * Created by hasiermetal on 19/06/15.
 */
aporlaofertaApp
    .directive('ngImageUploader', function () {
        return {
            restrict: 'A',
            scope: {
                finalUrl: '='
            },
            templateUrl: 'resources/js/uploader/imageUpload.html',
            link: function (scope, elem, attrs) {
            },
            controller: ['$scope', function ($scope) {
                $scope.uploader = {};
                $scope.filesIsUploaded = function (message) {
                    var responseResult = JSON.parse(message);
                    if (responseResult.code == 0) {
                        $scope.finalUrl = responseResult.description;
                    }
                    else {
                        $scope.handleUploadError(responseResult);
                    }
                };
                $scope.handleUploadError = function (error) {
                    alert("Response result from server shows unexpected error");
                };
                $scope.$watch('finalUrl', function (newValue, oldValue) {
                    if (newValue === "" || typeof newValue === 'undefined') {
                        $scope.uploader.flow.cancel();
                    }
                });
                $scope.handleProcessError = function () {
                    alert("handle error while uploading image");
                };
            }]
        }
    });