/**
 * Created by hasiermetal on 16/03/14.
 */
aporlaofertaApp
    .directive('ngOverheadDisplay', function () {
        return {
            restrict: 'A',
            templateUrl: 'resources/js/header-display/headDisplay.jsp',
            scope: {
                specificOffer: '@',
                overheadVisible: '=',
                noAccounts: '='
            },
            controller: ['$scope', 'offerManager', '$timeout', '$cookies',
                function ($scope, $offerManager, $timeout, $cookies) {
                    $scope.customCloseCallback = {};
                    $scope.theResponse = {};
                    $scope.fullscreen = false;
                    $scope.tutorialIsDisplayed=false;
                    $scope.displayLogin = function () {
                        $scope.setDefaultVisibility();
                        $scope.displayAccountLogin = true;
                        $scope.overheadVisible = true;
                    };
                    $scope.displaySignup = function () {
                        $scope.setDefaultVisibility();
                        $scope.displayAccountCreation = true;
                        $scope.overheadVisible = true;
                    };
                    $scope.displayAccountUpdateForm = function () {
                        $scope.setDefaultVisibility();
                        $scope.displayAccountUpdate = true;
                        $scope.overheadVisible = true;
                    };
                    $scope.displayOfferCreate = function () {
                        $scope.setDefaultVisibility();
                        $scope.displayOfferCreation = true;
                        $scope.overheadVisible = true;
                    };
                    $scope.displayOfferUpdate = function () {
                        $scope.setDefaultVisibility();
                        $scope.displayOfferToBeUpdate = true;
                        $scope.overheadVisible = true;
                    };
                    $scope.displayOfferDetails = function () {
                        $scope.setDefaultVisibility();
                        $scope.displayOfferSpecifications = true;
                        $scope.overheadVisible = true;
                    };
                    $scope.displayServerResponse = function () {
                        $scope.setDefaultVisibility();
                        $scope.displayResponseFromServer = true;
                        $scope.overheadVisible = true;
                    }
                    $scope.displayTutorialDiagram = function () {
                        $scope.setDefaultVisibility();
                        $scope.displayTutorial = true;
                        $scope.tutorialIsDisplayed=true;
                        $scope.overheadVisible = true;
                    }
                    $scope.setDefaultVisibility = function () {
                        $scope.overheadVisible = false;
                        $scope.displayOfferCreation = false;
                        $scope.displayOfferToBeUpdate = false;
                        $scope.displayAccountLogin = false;
                        $scope.displayAccountCreation = false;
                        $scope.displayAccountUpdate = false;
                        $scope.displayOfferSpecifications = false;
                        $scope.displayResponseFromServer = false;
                        $scope.displayTutorial = false;
                        $scope.tutorialIsDisplayed=false;
                    };
                    $scope.closeOverheadDisplay = function (customCloseCallback) {
                        $scope.overheadVisible = false;
                        if (typeof customCloseCallback == "function") {
                            customCloseCallback();
                            $scope.customCloseCallback = {};
                        }
                    };
                    $scope.$watch('specificOffer', function () {
                        if (/^\d+$/.exec($scope.specificOffer) != null) {
                            $scope.fullscreen = true;
                            $scope.customCloseCallback = function () {
                                $scope.fullscreen = false;
                            };
                            $offerManager.showSpecifications($scope.specificOffer);
                        }
                    });
                    $scope.$on('offerSpecifications', function (event, args) {
                        $scope.offerSpecifications = args;
                        $scope.displayOfferDetails();
                        $scope.customCloseCallback = function () {
                            $timeout(function () {
                                $scope.offerSpecifications = [];
                            }, 100);
                            $scope.fullscreen = false;
                        }

                    });

                    $scope.$on('updateTheOffer', function (event, args) {
                        $scope.offerSpecifications = [args];
                        $scope.customCloseCallback = function () {
                            $scope.displayOfferDetails();
                        }
                        $scope.displayOfferUpdate();
                    });

                    $scope.$on('closeResponse', function () {
                        $scope.overheadVisible = false;
                    });

                    $scope.$on('serverResponse', function (event, args) {
                        $scope.theResponse = args;
                        $scope.displayServerResponse();

                    });

                    $scope.$on('displayTutorial', function () {
                        $scope.displayTutorialDiagram();
                    });

                    $scope.$on('keydownControl', function (event, args) {
                        var keyDownCode = args;
                        if (keyDownCode == 27) {
                            $scope.justCloseOverheadDisplay();
                        }
                    });

                    $scope.justCloseOverheadDisplay = function () {
                        $scope.customCloseCallback = {};
                        $scope.closeOverheadDisplay();
                        $scope.fullscreen = false;
                    }

                    $scope.checkForErrors = function () {
                        if (document.getElementById("errorMessage")) {
                            var errorMessage = document.getElementById("errorMessage").value;
                            if (typeof errorMessage != undefined && errorMessage != '') {
                                var theResponse = {};
                                theResponse.descriptionEsp = errorMessage;
                                theResponse.responseResult = "error";
                                $scope.theResponse = theResponse;
                                $scope.displayServerResponse();
                            }
                        }
                    };

                    $scope.setDefaultVisibility();
                    $scope.checkForErrors();

                    $timeout(function () {
                        angular.element($('#overheadSubContainer')).removeClass('hiddencontainer');
                    }, 100);
                }]
        }
    });