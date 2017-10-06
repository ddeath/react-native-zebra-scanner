package com.ddeath.ZebraScanner;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class ZebraScannerModule extends ReactContextBaseJavaModule {

  private static ReactApplicationContext _reactContext;

  public ZebraScannerModule(ReactApplicationContext reactContext) {
    super(reactContext);
    _reactContext = reactContext;
  }

  public static ReactApplicationContext getReactContextSingleton() {
    return _reactContext;
  }

  @Override
  public String getName() {
    return "ZebraScannerModule";
  }
}