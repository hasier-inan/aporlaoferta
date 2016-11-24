var accountHelper = angular.module('accountHelper', []);

accountHelper.service('accountHelper', [
    function () {
        var accountUtils = {};

        accountUtils.validMail = /^[_a-z0-9-]+(\.[_a-z0-9-]+)*(\+[a-z0-9-]+)?@[a-z]+\.[a-z.]{2,5}$/;
        accountUtils.validPassword = /^(?=.*?)(?=.*?[a-z])(?=.*?[0-9])(?=.*?).{8,}$/;

        return accountUtils;
    }]);


