/**
 * Created by hasiermetal on 19/06/15.
 */
aporlaofertaApp
    .directive('ngImageUploader', function () {
        return {
            restrict: 'A',
            scope:{
                finalurl:'='
            },
            templateUrl: 'resources/js/uploader/imageUpload.html',
            link: function (scope, elem, attrs) {
            },
            controller: ['$scope', function ($scope) {
                $scope.filesIsUploaded = function (message) {
                    var responseResult = JSON.parse(message);
                    if (responseResult.code == 0) {
                        $scope.finalurl= responseResult.description;
                    }
                    else {
                        $scope.handleUploadError(responseResult);
                    }
                };
                $scope.handleUploadError=function(error){
                       alert("REsponse result from server shows unexpected error");
                };
                $scope.handleProcessError=function(){
                  alert("handle error while uploading image");
                };
            }]
        }
    });