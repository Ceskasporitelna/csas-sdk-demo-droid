package cz.csas.demo.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Extends View. Just used to draw Rect when the screen is touched
 * for auto focus.
 * <p/>
 * Use setHaveTouch function to set the status and the Rect to be drawn.
 * Call invalidate to draw Rect. Call invalidate again after
 * setHaveTouch(false, Rect(0, 0, 0, 0)) to hide the rectangle.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class DrawingView extends View {
    private boolean haveTouch = false;
    private Rect touchArea;
    private Paint paint;

    /**
     * Instantiates a new Drawing view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xeed7d7d7);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        haveTouch = false;
    }

    /**
     * Sets have touch.
     *
     * @param val  the val
     * @param rect the rect
     */
    public void setHaveTouch(boolean val, Rect rect) {
        haveTouch = val;
        touchArea = rect;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(haveTouch){
            //drawingPaint.setColor(Color.BLUE);
            canvas.drawRect(
                    touchArea.left, touchArea.top, touchArea.right, touchArea.bottom,
                    paint);
        }
    }

}