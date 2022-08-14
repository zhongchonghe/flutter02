import 'dart:async';

import 'package:flutter/services.dart';

class Flutter02 {
  static const MethodChannel _channel = MethodChannel('flutter02');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  Future<String> initSDK(settings) async {
    return await _channel.invokeMethod('initSDK', settings);
  }
}
