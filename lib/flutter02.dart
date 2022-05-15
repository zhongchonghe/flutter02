
import 'dart:async';

import 'package:flutter/services.dart';

class Flutter02 {
  static const MethodChannel _channel = MethodChannel('flutter02');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
  static Future<String> get initSDK async {
   return await _channel.invokeMethod('initSDK');
  }
}
