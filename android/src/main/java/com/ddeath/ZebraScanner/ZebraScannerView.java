package com.ddeath.ZebraScanner;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.OrientationEventListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;

import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.ReactContext;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;


import java.util.List;

public class ZebraScannerView extends ViewGroup {
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    private ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;
    private boolean invalid_code = false;
    private boolean pauseOnCodeScan = true;
    private boolean resumeScanOnTouch = true;
    private boolean allowDuplicateScan = true;

    private String lastScan = "";

    private final Context zebraContext;

    public ZebraScannerView(Context context) {
        super(context);

        this.zebraContext = context;

        autoFocusHandler = new Handler();

        // Instance barcode scanner
        scanner = new ImageScanner();
        scanner.setConfig(64, Config.ENABLE, 1);
        scanner.setConfig(64, Config.X_DENSITY, 3);
        scanner.setConfig(64, Config.Y_DENSITY, 3);

        recreateCamera();
    }

    @Override
    public void onViewAdded(View child) {
        if (this.mPreview == child) return;
        // remove and readd view to make sure it is in the back.
        // @TODO figure out why there was a z order issue in the first place and fix accordingly.
        this.removeView(this.mPreview);
        this.addView(this.mPreview, 0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = right - left;
        int height = bottom - top;

        this.mPreview.layout(0, 0, width, height);
        this.postInvalidate(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        recreateCamera();
    }

    @Override
    protected void onDetachedFromWindow() {
        releaseCamera();
    }

    public void setPauseOnCodeScan(boolean pauseOnCodeScan) {
        this.pauseOnCodeScan = pauseOnCodeScan;
    }

    public void setResumeScanOnTouch(boolean resumeScanOnTouch) {
        this.resumeScanOnTouch = resumeScanOnTouch;
        this.mPreview.setResumeScanOnTouch(resumeScanOnTouch);
    }

    public void setAllowDuplicateScan(boolean allowDuplicateScan)
    {
        this.allowDuplicateScan = allowDuplicateScan;
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance()
    {
        Camera c = null;
        try
        {
            c = Camera.open();
        } catch (Exception e)
        {
        }

        return c;
    }

    private void releaseCamera()
    {
        if (mCamera != null)
        {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing) {
                mCamera.autoFocus(autoFocusCB);
            }
        }
    };

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                if (pauseOnCodeScan) {
                    previewing = false;
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                }

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    String scanResult = sym.getData().trim();

                    processScanResult(scanResult);
                    barcodeScanned = true;

                    break;
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    private int getDeviceOrientation(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
    }

    private void recreateCamera() {
        if (mCamera == null) {
            mCamera = getCameraInstance();
            try {
                mCamera.reconnect();   
            } catch (Exception e) {
                //TODO: handle exception
            }
            mPreview = new CameraPreview(zebraContext, mCamera, previewCb,
                    autoFocusCB);
            addView(mPreview, 200, 200);
            mCamera.startPreview();
        }
    }


    private void processScanResult(String message)
    {
        if (!message.equals(this.lastScan) || this.allowDuplicateScan) {
            ReactContext reactContext = ZebraScannerModule.getReactContextSingleton();
            reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("onCodeReadAndroid", message);
            this.lastScan = message;
        }
    }
}