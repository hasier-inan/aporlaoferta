/**
 * Created by hasiermetal on 19/06/15.
 */
aporlaofertaApp
    .directive('ngImageUploader', function () {
        return {
            restrict: 'A',
            scope: {
                finalUrl: '=',
                reset: '=',
                alreadyUploadedImage: '=',
                fileIsBeingUploaded: '='
            },
            templateUrl: 'resources/js/uploader/imageUpload.html',
            controller: ['$rootScope', '$scope', 'configService', '$timeout', function ($rootScope, $scope, configService, $timeout) {
                $scope.uploader = {};
                $scope.displayThumbnail = true;
                $scope.invalidSize = false;
                $scope.invalidImage = false;
                $scope.maxImageSize = configService.getEndpoint('max.image.size');
                $scope.fileIsBeingUploaded=false;
                $scope.fileAdded = function (file) {
                    var fileReader = new FileReader();
                    fileReader.onload = function (event) {
                        var img = new Image();
                        $scope.alreadyUploadedImage="";
                        img.onerror = function () {
                            $scope.displayInvalidImageMessage();
                            return false;
                        };
                        img.onload = function () {
                            if (this.width > $scope.maxImageSize ||
                                this.height > $scope.maxImageSize
                                ) {
                                $scope.displayInvalidImageSizeMessage();
                                return false;
                            }
                            $scope.uploader.flow.upload();
                            $scope.defaultThumbnailView();
                        };
                        img.src = event.target.result;
                    };
                    fileReader.readAsDataURL(file.file);
                };

                $scope.showProgress = function(file){
                    $scope.fileIsBeingUploaded = file.isUploading();
                }

                $scope.deleteImage=function(){
                    $scope.alreadyUploadedImage = "";
                    $scope.fileIsBeingUploaded=false;
                    $scope.uploader.flow.cancel();
                }

                $scope.changeImage=function(){
                    $scope.alreadyUploadedImage = "";
                    $scope.fileIsBeingUploaded=false;
                }

                $scope.imageUploadSet = function () {
                    return $scope.alreadyUploadedImage !== "";
                }

                $scope.imageProgress = function(){
                     return Math.round($scope.uploader.flow.progress()*100);
                }

                $scope.defaultThumbnailView = function () {
                    $scope.displayThumbnail = true;
                    $scope.invalidImage = false;
                    $scope.invalidSize = false;
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
                $scope.$watch('reset', function () {
                    if ($scope.reset==true) {
                        $scope.deleteImage();
                    }
                });

                $scope.isInvalidThumbnailShown = function () {
                    return $scope.invalidImage || $scope.invalidSize;
                };

                $scope.displayInvalidImageSizeMessage = function () {
                    $scope.defaultThumbnailView();
                    $scope.invalidSize = true;
                    $scope.displayThumbnail = false;
                    $timeout(function () {
                        $scope.uploader.flow.cancel();
                    }, 500);
                };
                $scope.displayInvalidImageMessage = function () {
                    $scope.defaultThumbnailView();
                    $scope.invalidImage = true;
                    $scope.displayThumbnail = false;
                    $timeout(function () {
                        $scope.uploader.flow.cancel();
                    }, 500);
                }
            }]
        }
    });