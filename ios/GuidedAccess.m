#import <Cordova/CDV.h>
#import "GuidedAccess.h"

@implementation KioskPlugin


- (void)isGuidedAccessEnabled:(CDVInvokedUrlCommand*)command {
    BOOL guidedAccessEnabled = UIAccessibilityIsGuidedAccessEnabled();

    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:guidedAccessEnabled];

    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

@end