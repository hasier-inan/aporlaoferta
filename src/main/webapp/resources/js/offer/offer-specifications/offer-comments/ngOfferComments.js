/**
 * Created by hasiermetal on 11/06/15.
 */
aporlaofertaApp
    .directive('ngOfferComments', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/offer/offer-specifications/offer-comments/' +
                'offerComments.jsp',
            scope: {
                theComments: '=',
                theOffer: '='
            },
            controller: ['$scope', '$timeout', 'requestManager', 'configService', function ($scope, $timeout, requestManager, configService) {
                $scope.writeComment=function(comment, id){
                    requestManager.makePostCall(comment, {'offer': id}, configService.getEndpoint('create.comment'))
                        .success(function (data, status, headers, config) {

                        }).error(function (data, status, headers, config) {
                            //TODO: handle error;
                            alert("handle this error while retrieving data from newest offers");
                        });
                };
                $scope.quoteComment=function(comment, id){
                    requestManager.makePostCall(comment, {'quotedComment': id}, configService.getEndpoint('quote.comment'))
                        .success(function (data, status, headers, config) {

                        }).error(function (data, status, headers, config) {
                            //TODO: handle error;
                            alert("handle this error while retrieving data from newest offers");
                        });
                };
            }]
        }
    });