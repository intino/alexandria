"use strict";

var launchApplication = function (configuration) {

    window.Application = (function (configuration) {
        var services = {};

        var self = {};
        self.configuration = configuration;

        return self;

    })(configuration);

    window.addEventListener('WebComponentsReady', function () {
        PushService.openConnection(configuration.pushServiceUrl);
    });

};

function isImage(file) {
    if (file.name == null) return false;
    return file.name.match(/\.(jpg|jpeg|png|gif)$/);
};

NodeList.prototype.forEach = Array.prototype.forEach;
HTMLCollection.prototype.forEach = Array.prototype.forEach;

function addBrowserClasses(element) {
    if (navigator.userAgent.match(/msie/i) || navigator.userAgent.match(/trident/i) ){
        $(element).addClass("ie");
    }
}
