package cz.csas.demo.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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
public class DetailItem extends RelativeLayout {

    /**
     * The Tv detail title.
     */
    @Bind(R.id.tv_detail_title)
    public TextView tvDetailTitle;

    /**
     * The Tv detail value.
     */
    @Bind(R.id.tv_detail_value)
    public TextView tvDetailValue;

    private LayoutInflater mInflater;

    public DetailItem(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Instantiates a new Detail item.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public DetailItem(Context context, AttributeSet attrs) {
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
    public DetailItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.place_detail_item_component, this, true);

        ButterKnife.bind(this);
        tvDetailTitle.setTypeface(TypefaceUtils.getRobotoRegular(context));
        tvDetailValue.setTypeface(TypefaceUtils.getRobotoRegular(context));
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DetailItem, 0, 0);

            if (ta.getString(R.styleable.DetailItem_setTitle) != null) {
                setTitle(ta.getString(R.styleable.DetailItem_setTitle));
            }

            if (ta.getString(R.styleable.DetailItem_setValue) != null) {
                setValue(ta.getString(R.styleable.DetailItem_setValue));
            }

            if (ta.getString(R.styleable.DetailItem_setValueTextColor) != null) {
                setValueTextColor(ta.getString(R.styleable.DetailItem_setValueTextColor));
            }

            ta.recycle();
        }
    }

    /**
     * Set title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        tvDetailTitle.setText(title);
    }

    /**
     * Set value.
     *
     * @param value the value
     */
    public void setValue(String value) {
        tvDetailValue.setText(value);
    }

    /**
     * Set value text color.
     *
     * @param color the color
     */
    public void setValueTextColor(String color) {
        tvDetailValue.setTextColor(Color.parseColor(color));
    }

    /**
     * Set value text color int.
     *
     * @param color the color
     */
    public void setValueTextColor(int color) {
        tvDetailValue.setTextColor(color);
    }

}
