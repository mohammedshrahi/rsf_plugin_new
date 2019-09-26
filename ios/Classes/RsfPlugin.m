#import "RsfPlugin.h"
#import <rsf_plugin/rsf_plugin-Swift.h>

@implementation RsfPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftRsfPlugin registerWithRegistrar:registrar];
}
@end
