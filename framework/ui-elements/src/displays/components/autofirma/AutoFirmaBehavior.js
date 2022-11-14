import React from "react";
import deployJava from './deployJava';
import { AutoScript } from './autoscript';
import protocolCheck from './protocolcheck';

const AutoFirmaBehavior = (element) => {
    var IS_INSTALLED_INTENTION = "afirma://service?";

    function runProtocolChecker(intentionUrl, successCallback, failureCallback) {
        if (!checkProtocol(intentionUrl)) {
            successCallback();
            return;
        }

        element.installed = true;

        window.protocolCheck(intentionUrl, function() {
            element.installed = false;
            if (element.successTimeout != null)
                clearTimeout(element.successTimeout);
            failureCallback();
        });

        element.successTimeout = window.setTimeout(function () {
            if (element.installed) successCallback();
        }, isLinux() ? 5500 : 2500);
    }

    function checkProtocol(intention) {
        if (intention != IS_INSTALLED_INTENTION)
            return true;

        if (!element.checkProtocol)
            return false;

        return !isLinux();
    }

    function isWindows() {
        return navigator.appVersion.indexOf("Win") !=-1;
    }

    function isLinux() {
        return navigator.appVersion.indexOf("Linux") != -1;
    }

    function isMacOS() {
        return navigator.appVersion.indexOf("Mac") != -1;
    }

    function getOperatingSystem() {
        if (isWindows()) return "windows";
        if (isLinux()) return "linux";
        if (isMacOS()) return "macos";
        return "UnknownOS";
    }

    return {
        algorithm: "SHA1withRSA",
        installed: null,
        intention : null,

        initSignatory: function () {
            AutoScript.setForceWSMode(true);
            AutoScript.cargarAppAfirma();
            AutoScript.setServlets(this.storageUrl, this.retrieveUrl);
        },

        setDownloadUrl : function(downloadUrl) {
            this.downloadUrl = downloadUrl;
        },

        setStorageUrl : function(storageUrl) {
            this.storageUrl = storageUrl;
            AutoScript.setServlets(this.storageUrl, this.retrieveUrl);
        },

        setRetrieveUrl : function(retrieveUrl) {
            this.retrieveUrl = retrieveUrl;
            AutoScript.setServlets(this.storageUrl, this.retrieveUrl);
        },

        checkSignatoryAppInstalled: function (successCallback, failureCallback) {
            var element = this;

            if (successCallback == null)
                successCallback = function() { element.installedSuccessCallback(); };

            if (failureCallback == null)
                failureCallback = function(code, message) { element.installedFailureCallback(code, message); };

            if (element.installed === true) {
                successCallback();
                return;
            }

            var intentionUrl = this.intention != null ? this.intention.url : IS_INSTALLED_INTENTION;
            runProtocolChecker(intentionUrl, successCallback, failureCallback);
        },

        sign : function(dataB64, format, successCallback, failureCallback) {
            var behavior = this;
            var afirmaFormat = this._afirmaFormat(format);
            var afirmaParams = this._afirmaParams(format);
            this.execute(function(element) {
                AutoScript.sign(dataB64, element.algorithm, afirmaFormat, afirmaParams, successCallback, function(code, message, intention) {
                    behavior.checkFailureCallback(code, message, intention, failureCallback);
                });
            });
        },

        signDocument: function (dataB64, successCallback, failureCallback) {
            const behavior = this;
            this.execute(function(element) {
                var format = "PAdES";
                var params = "format=PAdES" +
                        "\nmode=implicit" +
                        "\nsignatureSubFilter=ETSI.CAdES.detached" +
                        "\nreferencesDigestMethod=http://www.w3.org/2001/04/xmlenc#sha256";

                AutoScript.sign(dataB64, element.algorithm, format, params, successCallback, function(code, message, intention) {
                    behavior.checkFailureCallback(code, message, intention, failureCallback);
                });
            });
        },

        signWithCertificate: function (dataB64, certificateSerialNumber, successCallback, failureCallback) {
            const behavior = this;
            this.execute(function(element) {
                var format = "CMS/PKCS#7";
                var params = "format=CMS/PKCS#7\nmode=implicit\n";

                if (certificateSerialNumber != null)
                    params += "\nheadless=true\nfilter=qualified:" + certificateSerialNumber;

                AutoScript.sign(dataB64, element.algorithm, format, params, successCallback, function(code, message, intention) { behavior.checkFailureCallback(code, message, intention, failureCallback); });
            });
        },

        coSign : function(signB64, dataB64, format, successCallback, failureCallback) {
            var behavior = this;
            var afirmaFormat = this._afirmaFormat(format);
            var afirmaParams = this._afirmaParams(format);
            this.execute(function(element) {
                AutoScript.coSign(signB64, dataB64, element.algorithm, afirmaFormat, afirmaParams, successCallback, function(code, message, intention) {
                    behavior.checkFailureCallback(code, message, intention, failureCallback);
                });
            });
        },

        counterSign : function(signB64, format, successCallback, failureCallback) {
            var behavior = this;
            var afirmaFormat = this._afirmaFormat(format);
            var afirmaParams = this._afirmaParams(format);
            this.execute(function(element) {
                AutoScript.counterSign(signB64, element.algorithm, afirmaFormat, afirmaParams, successCallback, function(code, message, intention) {
                    behavior.checkFailureCallback(code, message, intention, failureCallback);
                });
            });
        },

        signLocalDocument: function (successCallback, failureCallback) {
            this.signDocument(null, successCallback, failureCallback);
        },

        toBase64 : function(data) {
            return AutoScript.getBase64FromText(data);
        },

        fromBase64 : function(dataB64) {
            return AutoScript.getTextFromBase64(dataB64);
        },

        execute : function(method) {
            var element = this;
            this.checkSignatoryAppInstalled(function() { method(element); });
        },

        checkFailureCallback : function(code, message, intention, failureCallback) {
            if (code != null && code == "not_installed") {
                this.installedFailureCallback(code, message, intention);
                return;
            }

            failureCallback(code, message, intention);
        },

        showDownloadDialog : function(anchor) {
            element.openDownloadDialog();
        },

        hideDownloadDialog : function() {
            element.closeDownloadDialog();
        },

        downloadClient : function(event) {
            var operatingSystem = getOperatingSystem();

            if (operatingSystem == "linux") {
                this.showDownloadDialog(event.target);
                return;
            }

            this.doDownloadClient(operatingSystem);
        },

        downloadClientDistribution : function(distribution) {
            this.doDownloadClient(distribution);
        },

        doDownloadClient : function(os) {
            window.location.href = this.downloadUrl + "?os=" + os;
        },

        _afirmaFormat : function(format) {
            format = format.toLowerCase();
            if (format.indexOf("xades") != -1) return "XAdES";
            if (format.indexOf("cades") != -1) return "CAdES";
            return null;
        },

        _afirmaParams : function(format) {
            var afirmaFormat = this._afirmaFormat(format);
            var detached = format.toLowerCase().indexOf("attached") == -1;
            return "format=" + afirmaFormat + (detached ? " Detached" : "") + "\nmode=implicit" +
                   "\nreferencesDigestMethod=http://www.w3.org/2001/04/xmlenc#sha256";
        }
    };
};

export default AutoFirmaBehavior;