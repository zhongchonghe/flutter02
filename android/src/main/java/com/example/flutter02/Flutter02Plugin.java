package com.example.flutter02;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.wework.api.IWWAPI;
import com.tencent.wework.api.IWWAPIEventHandler;
import com.tencent.wework.api.WWAPIFactory;
import com.tencent.wework.api.model.BaseMessage;
import com.tencent.wework.api.model.WWAuthMessage;
import com.tencent.wework.api.model.WWMediaText;

import org.jetbrains.annotations.NotNull;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** Flutter02Plugin */
public class Flutter02Plugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private IWWAPI iwwapi;
  private static  String APPID = "";
  private static  String AGENTID = "";
  private static  String SCHEMA = "";
  private Activity activity;
  private static String CODE = "";

  //  发起授权
  private void wxLogin(Result result){

    iwwapi = WWAPIFactory.createWWAPI(activity);
    iwwapi.registerApp(SCHEMA);
    final WWAuthMessage.Req req = new WWAuthMessage.Req();
    req.sch = SCHEMA;
    req.appId = APPID;
    req.agentId = AGENTID;
    iwwapi.sendMessage(req, new IWWAPIEventHandler() {
      @Override
      public void handleResp(BaseMessage resp) {
        WWAuthMessage.Resp rsp = (WWAuthMessage.Resp) resp;
        if (rsp.errCode == WWAuthMessage.ERR_CANCEL) {
          Toast.makeText(activity, "登录取消", Toast.LENGTH_SHORT).show();
        }else if (rsp.errCode == WWAuthMessage.ERR_FAIL) {
          Toast.makeText(activity, "登录失败", Toast.LENGTH_SHORT).show();
        } else if (rsp.errCode == WWAuthMessage.ERR_OK) {
          Toast.makeText(activity, "登录成功",
                  Toast.LENGTH_SHORT).show();
//                  CODE=rsp.code;
          result.success(rsp.code);

        }
      }
    });

  }
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "wechatAuth");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }else if (call.method.equals("initSDK")){
      APPID = (String) call.argument("APPID");
      AGENTID = (String) call.argument("AGENTID");
      SCHEMA = (String) call.argument("SCHEMA");

      wxLogin(result);
    }else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  @Override
  public void onAttachedToActivity(@NonNull @NotNull ActivityPluginBinding binding) {
    activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull @NotNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {

  }
}
