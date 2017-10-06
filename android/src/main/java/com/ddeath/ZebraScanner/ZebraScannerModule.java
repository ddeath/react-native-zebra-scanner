package com.ddeath.ZebraScanner;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class ZebraScannerModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public ZebraScannerModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  public static ReactApplicationContext getReactContextSingleton() {
    return reactContext;
  }

  @Override
  public String getName() {
    return "ZebraScannerModule";
  }
}