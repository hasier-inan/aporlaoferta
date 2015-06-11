var requestManager = angular.module('requestManager', []);

requestManager.service('requestManager', ['$http',
    function ($http) {
        var requestControllerService = {};
        var csrfToken;
        requestControllerService.initMainController = function () {
            csrfToken = $("meta[name='_csrf']").attr("content");
        };
        requestControllerService.makePostCall=function(data, params, url){
            params._csrf= csrfToken;
            return $http({
                method: 'POST',
                url: url,
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': csrfToken
                },
                params: params,
                data: data
            });
        };
        requestControllerService.initMainController();
        return requestControllerService;
    }]);


