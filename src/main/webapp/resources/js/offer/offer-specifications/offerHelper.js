var offerHelper = angular.module('offerHelper', []);

offerHelper.service('offerHelper', [
    function () {
        var offerUtils = {};
        offerUtils.appliedOfferFilters = {};

        offerUtils.offerFeedbackStyle = function (offer) {
            if (offer) {
                var offerFeedback = offer.offerPositiveVote - offer.offerNegativeVote;
                if (offerFeedback > 0 && offerFeedback <= 100) {
                    return 'hotFeedback';
                }
                else if (offerFeedback > 100) {
                    return 'veryHotFeedback';
                }
                else if (offerFeedback < 0) {
                    return 'coldFeedback';
                }
            }
            return 'neutralFeedback';
        }

        offerUtils.isEmptyCategory = function (category) {
            return ["", null, undefined, "Categoría", "Todas", "CATEGORÍA", "TODAS"].indexOf(category) > -1;
        }

        return offerUtils;
    }]);


