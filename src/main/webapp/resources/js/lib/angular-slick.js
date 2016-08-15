"use strict";
angular.module("slick", []).directive("slick", function () {
    return{restrict: "AEC", scope: {accessibility: "@", arrows: "@", autoplay: "@", autoplaySpeed: "@", centerMode: "@", centerPadding: "@", cssEase: "@", dots: "@", draggable: "@", easing: "@", fade: "@", infinite: "@", lazyLoad: "@", onBeforeChange: "@", onAfterChange: "@", onInit: "@", onReInit: "@", pauseOnHover: "@", responsive: "@", slide: "@", slidesToShow: "@", slidesToScroll: "@", speed: "@", swipe: "@", touchMove: "@", touchThreshold: "@", vertical: "@"}, link: function (a, b) {
        return $(b).slick({accessibility: null != a.accessibility ? Boolean(a.accessibility) : !0, arrows: null != a.arrows ? Boolean(a.arrows) : !0, autoplay: Boolean(a.autoplay), autoplaySpeed: null != a.autoplaySpeed ? parseInt(a.autoplaySpeed, 10) : 3e3, centerMode: Boolean(a.centerMode), centerPadding: a.centerPadding || "50px", cssEase: a.cssEase || "ease", dots: Boolean(a.dots), draggable: null != a.draggable ? Boolean(a.draggable) : !0, easing: a.easing || "linear", fade: Boolean(a.fade), infinite: a.infinite=='true', lazyLoad: a.lazyLoad || "ondemand", onBeforeChange: a.onBeforeChange || null, onAfterChange: a.onAfterChange || null, onInit: a.onInit || null, onReInit: a.onReInit || null, pauseOnHover: null != a.pauseOnHover ? a.pauseOnHover : !0, responsive: a.responsive || null, slide: a.slide || "div", slidesToShow: null != a.slidesToShow ? parseInt(a.slidesToShow, 10) : 1, slidesToScroll: null != a.slidesToScroll ? parseInt(a.slidesToScroll, 10) : 1, speed: null != a.speed ? parseInt(a.speed, 10) : 300, swipe: null != a.swipe ? a.swipe : !0, touchMove: null != a.touchMove ? a.touchMove : !0, touchThreshold: a.touchThreshold ? parseInt(a.touchThreshold, 10) : 5, vertical: null != a.vertical ? a.vertical : !1})
    }}
});