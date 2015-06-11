var offerCreationService = angular.module('offerCreationService', []);

offerCreationService.service('offerCreationService', ['$rootScope',
    function ($rootScope) {
        var offerCreation = {};
        offerCreation.setSelectedCompany = function (company) {
            $rootScope.$broadcast('selectedCompany', { company: company });
        };
        offerCreation.setSelectedCategory = function (category) {
            $rootScope.$broadcast('selectedCategory', { category: category });
        };
        return offerCreation;
    }]);


