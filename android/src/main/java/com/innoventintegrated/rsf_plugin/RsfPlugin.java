package com.innoventintegrated.rsf_plugin;

import cn.pda.serialport.SerialPort;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** RsfPlugin */
public class RsfPlugin implements MethodCallHandler {
  /** Plugin registration. */
  static
  {
    //System.loadLibrary("devapi");
  }
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "rsf_plugin");
    channel.setMethodCallHandler(new RsfPlugin());
  }
  Manager245 manager245 = new Manager245();

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    switch (call.method)
    {
      case  "testing":
        result.success("returned right");
        break;
      case "getPlatformVersion":
        result.success("Android " + android.os.Build.VERSION.RELEASE);
        break;
      case "stopRead":
        manager245.stopRead();
        break;
      case "setPower":
        int set = call.argument("set");
        manager245.setPower(set);
        break;
      case "getTag":
        result.success(manager245.getTag());
        break;
      case "close":
        manager245.close();
        break;
      case "clear":
        manager245.clear();
        default:
          result.notImplemented();
    }

  }

}
