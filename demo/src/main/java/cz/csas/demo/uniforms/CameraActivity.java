package cz.csas.demo.uniforms;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 12/10/15.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.cscore.utils.csjson.CsJson;
import cz.csas.demo.Constants;
import cz.csas.demo.R;
import cz.csas.demo.components.CameraPreview;
import cz.csas.demo.components.DrawingView;
import cz.csas.demo.components.PreviewSurfaceView;
import cz.csas.lockerui.utils.TypefaceUtils;

/**
 * The type Camera activity.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
@SuppressWarnings("deprecation")
public class CameraActivity extends Activity{

    private final int RESULT_CANCELED = 1;
    private Activity mActivity;
    private Camera mCamera;
    private CameraPreview mCameraPreview;
    private SensorManager mSensorManager = null;
    private int mOrientation;
    private int mDeviceHeight;
    private int mDegrees = -1;
    private File mPictureFile;

    /**
     * The Dw drawing surface.
     */
    @Bind(R.id.drawing_surface)
    DrawingView dwDrawingSurface;

    /**
     * The Psv preview surface.
     */
    @Bind(R.id.preview_surface)
    PreviewSurfaceView psvPreviewSurface;

    /**
     * The Btn capture.
     */
    @Bind(R.id.btn_capture)
    ImageButton btnCapture;

    /**
     * The Btn retake.
     */
    @Bind(R.id.btn_retake)
    Button btnRetake;

    /**
     * The Btn upload.
     */
    @Bind(R.id.btn_upload)
    Button btnUpload;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout();
        mActivity = this;
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Selecting the resolution of the Android device so we can create a
        // proportional preview
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        mDeviceHeight = display.getHeight();

    }

    private void setLayout(){
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        btnRetake.setTypeface(TypefaceUtils.getRobotoRegular(this));
        btnUpload.setTypeface(TypefaceUtils.getRobotoRegular(this));
        btnCapture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCamera.takePicture(null, null, mPicture);
                btnCapture.setClickable(false);
            }
        });
        btnRetake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.startPreview();
                btnCapture.setClickable(true);
                btnRetake.setVisibility(View.GONE);
                btnUpload.setVisibility(View.GONE);
                btnCapture.setVisibility(View.VISIBLE);
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFileWithResult();
            }
        });
    }

    private void createCamera() {
        // Create an instance of Camera
        mCamera = getCameraInstance();
        if(mCamera != null) {
            // Setting the right parameters in the camera
            Camera.Parameters params = mCamera.getParameters();
            List<Camera.Size> sizes = params.getSupportedPictureSizes();
            Camera.Size size = sizes.get(0);
            params.setPictureSize(size.width, size.height);
            params.setPictureFormat(PixelFormat.JPEG);
            params.setJpegQuality(85);
            mCamera.setParameters(params);
            mCamera.setDisplayOrientation(90);

            psvPreviewSurface = (PreviewSurfaceView) findViewById(R.id.preview_surface);
            SurfaceHolder camHolder = psvPreviewSurface.getHolder();

            mCameraPreview = new CameraPreview(mCamera);
            camHolder.addCallback(mCameraPreview);
            camHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            camHolder.setFixedSize(100, 100);

            psvPreviewSurface.setListener(mCameraPreview);
            //mCameraPreview.changeExposureComp(-currentAlphaAngle);
            psvPreviewSurface.setDrawingView(dwDrawingSurface);


            float widthFloat = (float) (mDeviceHeight) * 4 / 3;
            int width = Math.round(widthFloat);
        }else{
            onBackPressed();
            finish();
        }
        // Resizing the LinearLayout so we can make a proportional preview. This
        // approach is not 100% perfect because on devices with a really small
        // screen the the image will still be distorted - there is place for
        // improvment.
        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, mDeviceHeight);
        //container.setLayoutParams(layoutParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Test if there is a camera on the device and if the SD card is
        // mounted.
        if (!checkCameraHardware(this)) {
            onBackPressed();
            finish();
        }
        setLayout();
        // Creating the camera
        createCamera();

        // Register this class as a listener for the accelerometer sensor
        ButterKnife.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            psvPreviewSurface.getHolder().removeCallback(mCameraPreview);
            mCamera.release();
            mCamera = null;
        }
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void uploadFileWithResult(){
        if(mPictureFile != null) {
            Intent returnIntent = new Intent();
            CsJson csJson = new CsJson();
            returnIntent.putExtra(Constants.FILE_EXTRA, csJson.toJson(mPictureFile));
            setResult(RESULT_OK, returnIntent);
        }else
            setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * A safe way to get an instance of the Camera object.
     *
     * @return the camera instance
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {

            // File name of the image that we just took.
            mPictureFile = new File(getCacheDir(), "priloha_"+System.currentTimeMillis()+".jpeg");
            try {
                FileOutputStream purge = new FileOutputStream(mPictureFile);
                purge.write(data);
                purge.close();
            } catch (FileNotFoundException e) {
                Log.d("DG_DEBUG", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("DG_DEBUG", "Error accessing file: " + e.getMessage());
            }
            btnCapture.setVisibility(View.GONE);
            btnRetake.setVisibility(View.VISIBLE);
            btnUpload.setVisibility(View.VISIBLE);
            mCamera.stopPreview();
        }
    };

}