package cz.csas.demo.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.demo.R;

/**
 * The type Csas marker.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 02 /05/16.
 */
public class CsasMarker extends RelativeLayout {

    /**
     * The Rl marker layout.
     */
    @Bind(R.id.rl_marker_layout)
    RelativeLayout rlMarkerLayout;

    /**
     * The Iv marker image.
     */
    @Bind(R.id.iv_marker_image)
    ImageView ivMarkerImage;

    private LayoutInflater mInflater;

    /**
     * Instantiates a new Csas marker.
     *
     * @param context the context
     */
    public CsasMarker(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Instantiates a new Csas marker.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CsasMarker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Instantiates a new Csas marker.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public CsasMarker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.marker_csas_component, this, true);

        ButterKnife.bind(this);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CsasMarker, 0, 0);

            if (ta.getString(R.styleable.CsasMarker_setMarkerImage) != null) {
                setMarkerImage(ta.getResourceId(R.styleable.CsasMarker_setMarkerImage, 0));
            }
            ta.recycle();
        }
    }

    /**
     * Set marker image.
     *
     * @param resource the resource
     */
    public void setMarkerImage(int resource) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), resource, mInflater.getContext().getTheme());
        ivMarkerImage.setImageDrawable(drawable);
    }
}
