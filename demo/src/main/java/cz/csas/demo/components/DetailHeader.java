package cz.csas.demo.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.demo.R;
import cz.csas.lockerui.utils.TypefaceUtils;

/**
 * The type Detail item.
 *
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 24 /08/15.
 */
public class DetailHeader extends RelativeLayout {

    /**
     * The Tv title.
     */
    @Bind(R.id.tv_title)
    public TextView tvTitle;


    private LayoutInflater mInflater;

    /**
     * Instantiates a new Detail item.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public DetailHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Instantiates a new Detail item.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public DetailHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.place_detail_header_component, this, true);

        ButterKnife.bind(this);
        tvTitle.setTypeface(TypefaceUtils.getRobotoRegular(context));

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DetailHeader, 0, 0);

        if (ta.getString(R.styleable.DetailHeader_setHeaderTitle) != null) {
            setDetailHeader(ta.getString(R.styleable.DetailHeader_setHeaderTitle));
        }

        ta.recycle();
    }

    /**
     * Set title.
     *
     * @param title the title
     */
    public void setDetailHeader(String title) {
        tvTitle.setText(title);
    }


}
