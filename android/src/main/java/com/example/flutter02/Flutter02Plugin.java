package com.example.flutter02;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tencent.wework.api.IWWAPI;
import com.tencent.wework.api.IWWAPIEventHandler;
import com.tencent.wework.api.WWAPIFactory;
import com.tencent.wework.api.model.BaseMessage;
import com.tencent.wework.api.model.WWAuthMessage;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** Flutter02Plugin */
public class Flutter02Plugin extends FlutterActivity implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private IWWAPI iwwapi;
  private static final String APPID = "ww74b6271d7d4c7948";
  private static final String AGENTID = "1000007";
  private static final String SCHEMA = "wwauth74b6271d7d4c7948000007";

  @Override
  protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    iwwapi = WWAPIFactory.createWWAPI(this);
    iwwapi.registerApp(SCHEMA);

  }
//  发起授权
  private void wxLogin(){
    final WWAuthMessage.Req req = new WWAuthMessage.Req();
    req.sch = SCHEMA;
    req.appId = APPID;
    req.agentId = AGENTID;
    iwwapi.sendMessage(req, new IWWAPIEventHandler() {
      @Override
      public void handleResp(BaseMessage resp) {
        WWAuthMessage.Resp rsp = (WWAuthMessage.Resp) resp;
        if (rsp.errCode == WWAuthMessage.ERR_CANCEL) {
          Toast.makeText(Flutter02Plugin.this, "登录取消", Toast.LENGTH_SHORT).show();
        }else if (rsp.errCode == WWAuthMessage.ERR_FAIL) {
          Toast.makeText(Flutter02Plugin.this, "登录失败", Toast.LENGTH_SHORT).show();
        } else if (rsp.errCode == WWAuthMessage.ERR_OK) {
          Toast.makeText(Flutter02Plugin.this, "登录成功：" + rsp.code,
                  Toast.LENGTH_SHORT).show();
        }
      }
    });

  }
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter02");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }else if (call.method.equals("initSDK")){
//      result.success("hello");
      wxLogin();
    }else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
