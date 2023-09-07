#import <Cordova/CDV.h>
#import "WPGuidedAccess.h"

@implementation GuidedAccessMode


- (void)isGuidedAccessEnabled:(CDVInvokedUrlCommand*)command {
    BOOL guidedAccessEnabled = UIAccessibilityIsGuidedAccessEnabled();

    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:guidedAccessEnabled];

    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

@end