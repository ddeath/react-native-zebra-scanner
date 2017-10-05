package com.ddeath.ZebraScanner;

import android.support.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.*;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.List;
import java.util.ArrayList;

public class ZebraScannerViewManager extends ViewGroupManager<ZebraScannerView> {
    private static final String REACT_CLASS = "ZebraScanner";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public ZebraScannerView createViewInstance(ThemedReactContext context) {
        return new ZebraScannerView(context);
    }

    @ReactProp(name = "aspect")
    public void setAspect(ZebraScannerView view, int aspect) {
        
    }
}