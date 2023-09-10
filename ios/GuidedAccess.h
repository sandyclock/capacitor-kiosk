#import <Cordova/CDV.h>

@interface KioskPlugin: CDVPlugin

- (void)isGuidedAccessEnabled:(CDVInvokedUrlCommand*)command;

- (void)isDevicePasscodeSet:(CDVInvokedUrlCommand*)command;


@end