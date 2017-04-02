package cz.csas.demo.components;

import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Camera preview.
 */
@SuppressWarnings("deprecation")
public class CameraPreview implements SurfaceHolder.Callback {

    private Camera mCamera = null;

    /**
     * The Supported sizes.
     */
    public List<Camera.Size> supportedSizes;


    private final static String TAG = "CameraPreview";

    /**
     * Instantiates a new Camera preview.
     *
     * @param camera the camera
     */
    public CameraPreview(Camera camera) {
        mCamera = camera;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d("DG_DEBUG", "Error setting camera preview: " + e.getMessage());
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * Called from PreviewSurfaceView to set touch focus.
     *
     * @param tfocusRect the tfocus rect
     */
    public void doTouchFocus(final Rect tfocusRect) {
        Log.d(TAG, "TouchFocus");
        try {
            final List<Camera.Area> focusList = new ArrayList<Camera.Area>();
            Camera.Area focusArea = new Camera.Area(tfocusRect, 1000);
            focusList.add(focusArea);

            Camera.Parameters para = mCamera.getParameters();
            para.setFocusAreas(focusList);
            para.setMeteringAreas(focusList);
            mCamera.setParameters(para);

            mCamera.autoFocus(myAutoFocusCallback);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Unable to autofocus");
        }

    }

    /**
     * AutoFocus callback
     */
    AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            if (arg0){
                mCamera.cancelAutoFocus();
            }
        }
    };
}