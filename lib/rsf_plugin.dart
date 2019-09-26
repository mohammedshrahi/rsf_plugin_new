import 'dart:async';

import 'package:flutter/services.dart';

class RsfPlugin {
  static const MethodChannel _channel = const MethodChannel('rsf_plugin');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static void stopRead() {
    _channel.invokeMethod('stopRead');
  }

  static Future<bool> setPower(int set) async {
    Map<String, dynamic> args = <String, dynamic>{};
    args.putIfAbsent("set", () => set);
    return await _channel.invokeMethod('setPower');
  }

  static void startRead() {
    _channel.invokeMethod("startRead");
  }

  static Future<String> getTag() async {
    return await _channel.invokeMethod("getTag");
  }

  static void close() {
    _channel.invokeMethod("close");
  }

  static void clear() {
    _channel.invokeMethod("clear");
  }

  static Future<String> testing() async {
    return await _channel.invokeMethod("testing");
  }
}
