
var exec = require('cordova/exec');

var KioskPlugin = {
    
    exitKiosk: function (callback, hasError) {
        exec(function () {
            if (callback){
                callback();
            }
        }, function (error) {
            alert("KioskPlugin.exitKiosk failed: " + error);
            if (hasError){
                hasError(error)
            }
        }, "KioskPlugin", "exitKiosk", []);
    },

    enterKiosk: function (callback, hasError) {
        exec(function () {
            if (callback){
                callback();
            }
        }, function (error) {
            alert("KioskPlugin.enterKiosk failed: " + error);
            if (hasError){
                hasError(error)
            }
        }, "KioskPlugin", "enterKiosk", []);
    },

    leaveKiosk: function (callback, hasError) {
        exec(function () {
            if (callback){
                callback();
            }

        }, function (error) {
            alert("KioskPlugin.leaveKiosk failed: " + error);
            if (hasError){
                hasError(error)
            }
        }, "KioskPlugin", "leaveKiosk", []);
    },

    isInKiosk: function (callback, hasError) {
        if(/ios|iphone|ipod|ipad/i.test(navigator.userAgent)) {
            callback(false); // ios not supported - cannot be in kiosk
            return;
        }
        exec(function (out) {
            callback(out == "true");
        }, function (error) {
            alert("KioskPlugin.isInKiosk failed: " + error);
            if (hasError){
                hasError(error)
            }
        }, "KioskPlugin", "isInKiosk", []);
    },

    isInKioskMode: function (callback, hasError) {
        if(/ios|iphone|ipod|ipad/i.test(navigator.userAgent)) {
            callback(false); // ios not supported - cannot be in kiosk
            return;
        }
        exec(function (out) {
            callback(out == "true");
        }, function (error) {
            alert("KioskPlugin.isInKioskMode failed: " + error);
            if (hasError){
                hasError(error)
            }
        }, "KioskPlugin", "isInKioskMode", []);
    },

    isSetAsLauncher: function (callback, hasError) {
        if(/ios|iphone|ipod|ipad/i.test(navigator.userAgent)) {
            callback(false); // ios not supported - cannot be in kiosk
            return;
        }
        exec(function (out) {
            callback(out == "true");
        }, function (error) {
            alert("KioskPlugin.isSetAsLauncher failed: " + error);
            if (hasError){
                hasError(error)
            }

        }, "KioskPlugin", "isSetAsLauncher", []);
    },
    
    setAllowedKeys: function (keyCodes, callback, hasError) {
        exec(function () {
            if (callback){
                callback();
            }
        }, function (error) {
            alert("KioskPlugin.setAllowedKeys failed: " + error);
            if (hasError){
                hasError(error)
            }
        }, "KioskPlugin", "setAllowedKeys", keyCodes);
    }
    
}

module.exports = KioskPlugin;

