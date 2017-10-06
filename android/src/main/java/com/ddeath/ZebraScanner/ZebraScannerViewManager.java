package com.ddeath.ZebraScanner;

import android.support.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.*;
import com.facebook.react.bridge.Callback;
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

    @ReactProp(name = "pauseOnCodeScan")
    public void setPauseOnCodeScan(ZebraScannerView view, boolean pauseOnCodeScan) {
        view.setPauseOnCodeScan(pauseOnCodeScan);
    }

    @ReactProp(name = "resumeScanOnTouch")
    public void setResumeScanOnTouch(ZebraScannerView view, boolean resumeScanOnTouch) {
        view.setResumeScanOnTouch(resumeScanOnTouch);
    }
}