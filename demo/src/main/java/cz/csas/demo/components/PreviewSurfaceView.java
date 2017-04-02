package cz.csas.demo.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

/**
 * SurfaceView to show LenxCameraPreview2 feed
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class PreviewSurfaceView extends SurfaceView {

    private CameraPreview camPreview;
    private boolean listenerSet = false;
    /**
     * The Paint.
     */
    public Paint paint;
    private DrawingView drawingView;
    private boolean drawingViewSet = false;

    /**
     * Instantiates a new Preview surface view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public PreviewSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!listenerSet) {
            return false;
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            float x = event.getX();
            float y = event.getY();

            Rect touchRect = new Rect(
                    (int)(x - 100),
                    (int)(y - 100),
                    (int)(x + 100),
                    (int)(y + 100));

            final Rect targetFocusRect = new Rect(
                    touchRect.left * 2000/this.getWidth() - 1000,
                    touchRect.top * 2000/this.getHeight() - 1000,
                    touchRect.right * 2000/this.getWidth() - 1000,
                    touchRect.bottom * 2000/this.getHeight() - 1000);

            camPreview.doTouchFocus(targetFocusRect);
            if (drawingViewSet) {
                drawingView.setHaveTouch(true, touchRect);
                drawingView.invalidate();

                // Remove the square after some time
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        drawingView.setHaveTouch(false, new Rect(0, 0, 0, 0));
                        drawingView.invalidate();
                    }
                }, 1000);
            }

        }
        return false;
    }

    /**
     * set CameraPreview instance for touch focus.
     *
     * @param camPreview - CameraPreview
     */
    public void setListener(CameraPreview camPreview) {
        this.camPreview = camPreview;
        listenerSet = true;
    }

    /**
     * set DrawingView instance for touch focus indication.
     *
     * @param dView the d view
     */
    public void setDrawingView(DrawingView dView) {
        drawingView = dView;
        drawingViewSet = true;
    }

}