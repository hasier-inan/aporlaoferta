/**
 * Created by hasiermetal on 19/06/16.
 */
aporlaofertaApp
    .directive('ngHeadAccountAndOfferManagement', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/header-display/headAccountAndOfferManagement.jsp',
            scope: {
                displayLogin: '=',
                displaySignup: '=',
                displayOfferCreate: '=',
                displayAccountUpdateForm: '='
            }
        }
    });