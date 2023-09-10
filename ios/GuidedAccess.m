#import <Cordova/CDV.h>
#import "GuidedAccess.h"

@implementation KioskPlugin


- (void)isGuidedAccessEnabled:(CDVInvokedUrlCommand*)command {
    BOOL guidedAccessEnabled = UIAccessibilityIsGuidedAccessEnabled();

    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:guidedAccessEnabled];

    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

- (void) isDevicePasscodeSet:(CDVInvokedUrlCommand*)command {
    //checks to see if devices (not apps) passcode has been set
    LAContext *context = [LAContext new];
    NSError *error;
    BOOL passcodeEnabled = [context canEvaluatePolicy:LAPolicyDeviceOwnerAuthentication error:&error];
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:passcodeEnabled];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

@end