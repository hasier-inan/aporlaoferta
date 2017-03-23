var URL = "http://www.aporlaoferta.com/";
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
            $(".header").hide();
            $(".message-container").hide();
            $("#aporlaoferta-container").attr("src", URL + APP_START);
            $("#aporlaoferta-container").show();
        })
        .fail(function () {
            $(".message-container").show();
        })
        .always(function () {
            $(".loading-container").hide();
        });
}
