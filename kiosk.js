
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

    selectLauncher: function (callback, hasError) {
        exec(function () {
            if (callback){
                callback();
            }
        }, function (error) {
            alert("KioskPlugin.selectLauncher failed: " + error);
            if (hasError){
                hasError(error)
            }
        }, "KioskPlugin", "selectLauncher", []);
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

    openLockScreenSetting: function (callback, hasError) {
        exec(function () {
            if (callback){
                callback();
            }

        }, function (error) {
            alert("KioskPlugin.openLockScreenSetting failed: " + error);
            if (hasError){
                hasError(error)
            }
        }, "KioskPlugin", "openLockScreenSetting", []);
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

    isDeviceSecure: function (callback, hasError) {
        if(/ios|iphone|ipod|ipad/i.test(navigator.userAgent)) {
            callback(false); // ios not supported - cannot be in kiosk
            return;
        }
        exec(function (out) {
            callback(out == "true");
        }, function (error) {
            alert("KioskPlugin.isDeviceSecure failed: " + error);
            if (hasError){
                hasError(error)
            }

        }, "KioskPlugin", "isDeviceSecure", []);
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
    },

    isGuidedAccessEnabled: function (callback, hasError) {
        console.log("isGuidedAccessEnabled:check:2:");

        exec(function (out) {
            // console.log("result:success:");
            // console.log(out);
            callback(out);
        }, function (error) {
            // console.log("result:failed:");
            // console.log(error);
            alert("KioskPlugin.isGuidedAccessEnabled failed: " + error);
            if (hasError){
                hasError(error)
            }
        }, "KioskPlugin", "isGuidedAccessEnabled", []);
    },

    isDevicePasscodeSet: function (callback, hasError) {
        console.log("isDevicePasscodeSet:check:1:");

        exec(function (out) {
            // console.log("result:success:");
            // console.log(out);
            callback(out);
        }, function (error) {
            // console.log("result:failed:");
            // console.log(error);
            alert("KioskPlugin.isDevicePasscodeSet failed: " + error);
            if (hasError){
                hasError(error)
            }
        }, "KioskPlugin", "isDevicePasscodeSet", []);
    }

    
}

module.exports = KioskPlugin;

