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

    private Callback onCodeRead = null;

    private boolean barcodeScanned = false;
    private boolean previewing = true;
    private boolean invalid_code = false;

    private final Context zebraContext;

    public ZebraScannerView(Context context) {
        super(context);

        this.zebraContext = context;

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        // Instance barcode scanner
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);
        

        mPreview = new CameraPreview(context, mCamera, previewCb,
                autoFocusCB);
        addView(mPreview, 200, 200);
        mCamera.startPreview();
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
    protected void onDetachedFromWindow() {
        releaseCamera();
    }

    public void setOnCodeRead(final Callback callback) {
        this.onCodeRead = callback;
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
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();

            SymbolSet syms = scanner.getResults();
            for (Symbol sym : syms) {
                Log.i("<<<<<<Asset Code>>>>> ",
                        "<<<<Bar Code>>> " + sym.getData());
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


    private void processScanResult(String message)
    {
        if (this.onCodeRead !== null) {
            this.onCodeRead.invoke(message);
        }
    }

    private void restartScanner()
    {
        if (barcodeScanned) {
            barcodeScanned = false;
            mCamera.setPreviewCallback(previewCb);
            mCamera.startPreview();
            previewing = true;
            mCamera.autoFocus(autoFocusCB);
        }
    }
}