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
                specificOfferData: '@',
                overheadVisible: '=',
                noAccounts: '=',
                fullscreen: '='
            },
            controller: ['$scope', '$window', 'offerManager', '$timeout', '$cookies', 'configService',
                function ($scope, $window, $offerManager, $timeout, $cookies, configService) {
                    $scope.customCloseCallback = {};
                    $scope.theResponse = {};
                    $scope.fullscreen = false;
                    $scope.tutorialIsDisplayed = false;
                    $scope.loadingOfferSpecifications = false;

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
                    $scope.displayOfferLoading = function () {
                        $scope.setDefaultVisibility();
                        $scope.loadingOfferSpecifications = true;
                        $scope.overheadVisible = true;
                    };
                    $scope.displayServerResponse = function () {
                        $scope.setDefaultVisibility();
                        $scope.displayResponseFromServer = true;
                        $scope.overheadVisible = true;
                    }
                    $scope.displayTutorialDiagram = function () {
                        $scope.customCloseCallback = function () {
                            $scope.setTutorialCookie();
                        }
                        $scope.setDefaultVisibility();
                        $scope.displayTutorial = true;
                        $scope.overheadVisible = true;
                        $scope.tutorialIsDisplayed = true;
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
                        $scope.tutorialIsDisplayed = false;
                        $scope.loadingOfferSpecifications = false;
                    };
                    $scope.closeOverheadDisplay = function (customCloseCallback) {
                        $scope.setDefaultVisibility();
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
                            var offerSpecifications = $scope.specificOfferData
                                .replace(new RegExp("xx#!!#xx", 'g'), "\\\"")
                                .replace(new RegExp("xx#!#xx", 'g'), "'");
                            $scope.openOfferSpecifications(JSON.parse(offerSpecifications));
                        }
                    });
                    $scope.$on('offerSpecifications', function (event, args) {
                        $scope.openOfferSpecifications(args);
                    });

                    $scope.openOfferSpecifications = function (offerSpecifications) {
                        $scope.offerSpecifications = offerSpecifications;
                        $scope.loadingOfferSpecifications = false;
                        $scope.displayOfferDetails();
                        $scope.customCloseCallback = function () {
                            $scope.offerSpecifications = [];
                            $scope.fullscreen = false;
                        }
                    }

                    $scope.redirectHome = function () {
                        $window.location.href = '/';
                    };
                    $scope.$on('loadingOfferSpecifications', function () {
                        $scope.displayOfferLoading();
                    });

                    $scope.$on('updateTheOffer', function (event, args) {
                        $scope.offerSpecifications = [args];
                        $scope.customCloseCallback = function () {
                            $scope.displayOfferDetails();
                        }
                        $scope.displayOfferUpdate();
                    });

                    $scope.$on('serverResponse', function (event, args) {
                        $scope.theResponse = args;
                        $scope.fullscreen = false;
                        $scope.displayServerResponse();
                    });

                    $scope.$on('displayTutorial', function () {
                        $scope.displayTutorialDiagram();
                    });

                    $scope.$on('userLoginRequest', function () {
                        $scope.displayLogin();
                    });

                    $scope.$on('userRegisterRequest', function () {
                        $scope.displaySignup();
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

                    $scope.setTutorialCookie = function () {
                        var expireDate = new Date();
                        expireDate.setDate(expireDate.getDate() + 365);
                        $cookies.put(configService.getEndpoint('tutorial.cookie'), 'true', {'expires': expireDate});
                    };

                    $scope.setDefaultVisibility();
                    $scope.checkForErrors();

                    $timeout(function () {
                        angular.element($('#overheadSubContainer')).removeClass('hiddencontainer');
                    }, 100);

                    if (($cookies.get(configService.getEndpoint('tutorial.cookie')) != 'true') && !$scope.specificOffer) {
                        $timeout(function () {
                            $scope.displayTutorialDiagram();
                        }, 2000);
                    }
                }]
        }
    });