import 'package:flutter/material.dart';
import 'package:flutter02/wechatAuth.dart';


void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final wechatAuth = WechatAuth();
  String? _msg = "暂无";

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text('code: $_msg'),
              ElevatedButton(
                onPressed: _loginQr,
                child: Text("企业微信登录"),
              ),
            ],
          ),
        ),
      ),
    );
  }

  _loginQr() async {
    // initsdk();
    var msg = await wechatAuth.initSDK({
      "SCHEMA": "wwauth74b6271d7d4c7948000014",
      "APPID": "ww74b6271d7d4c7948",
      "AGENTID": "1000014"
    });
    setState(() {
      _msg = msg;
    });
    print("------------");
    print(_msg);
  }
}
