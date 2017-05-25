var URL = "https://www.aporlaoferta.com/";
var APP_START = "start?cookies-accepted=true";
var HEALTHCHECK = "healthcheck";
var TIMEOUT = 20000;

$.ajaxSetup({
    timeout: TIMEOUT
});

$(document).ready(function () {
    loadPage();
});

var loadPage = function () {
    $(".message-container").hide();
    $(".loading-container").show();
    $("#aporlaoferta-container").hide();

    $.get(URL + HEALTHCHECK, function () {
        })
        .done(function () {
            $(".message-container").hide();
            $("#aporlaoferta-container").attr("src", URL + APP_START);
            $("#aporlaoferta-container").show();
        })
        .fail(function () {
            $(".message-container").show();
        });
}

document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
    navigator.splashscreen.hide();
}
