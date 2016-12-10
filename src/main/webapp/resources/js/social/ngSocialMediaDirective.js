/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngSocialMedia', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/social/socialMedia.html',
            scope: {
                shareUrl: '=',
                shareText: '='
            },
            controller: ['$scope',
                function ($scope) {
                    $scope.$watch('shareUrl', function () {
                        $scope.facebookUrl = "https://www.facebook.com/sharer/sharer.php?u=" + $scope.shareUrl;
                        $scope.twitterUrl = "https://twitter.com/intent/tweet?url=" +
                            $scope.shareUrl + "&text=" + encodeURIComponent($scope.shareText); + "&via=aporlaoferta";
                        $scope.googlePlusUrl = "https://plus.google.com/share?url=" + $scope.shareUrl;
                        $scope.whatsappHref = $scope.buildWhatsappUrl();
                    });

                    $scope.$watch('shareText', function () {
                        $scope.twitterUrl = "https://twitter.com/intent/tweet?url=" +
                            $scope.shareUrl + "&text=" + encodeURIComponent($scope.shareText); + "&via=aporlaoferta";
                        $scope.whatsappHref = $scope.buildWhatsappUrl();
                    });

                    $scope.buildWhatsappUrl = function(){
                         return "whatsapp://send?text=_"+encodeURIComponent($scope.shareText.replace(/_/, ' ')+"_ "+$scope.shareUrl);
                    }
                }]

        }
    });