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
            controller: ['$rootScope', '$scope', function ($rootScope, $scope) {
                $scope.uploader = {};
                $scope.fileAdded = function (file) {
                        var fileReader = new FileReader();
                        fileReader.onload = function (event) {
                            var img = new Image();
                            img.onload = function () {
                                if (this.width == 1600) {
                                    alert("in sinkronua metodua");
                                    return false;
                                }
                                $scope.uploader.flow.upload()
                            };
                            img.src = event.target.result;
                        };
                        fileReader.readAsDataURL(file.file);
                };

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
                    $rootScope.$broadcast('serverResponse', error);
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