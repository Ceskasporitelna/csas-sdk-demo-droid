package cz.csas.demo.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.demo.R;
import cz.csas.lockerui.utils.TypefaceUtils;

/**
 * The type Field type pobocky.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 07 /01/16.
 */
public class FieldTypePobocky extends FieldTypeView {

    /**
     * The Tv pobocky.
     */
    @Bind(R.id.tv_pobocky)
    TextView tvPobocky;

    private Context mContext;
    private LayoutInflater mInflater;

    /**
     * Instantiates a new Field type pobocky.
     *
     * @param context the context
     */
    public FieldTypePobocky(Context context) {
        super(context);
        init(context,null);
    }

    /**
     * Instantiates a new Field type pobocky.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public FieldTypePobocky(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    /**
     * Instantiates a new Field type pobocky.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public FieldTypePobocky(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs){
        this.mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.field_type_pobocky, this, true);

        ButterKnife.bind(this);

        tvFieldTitle.setTypeface(TypefaceUtils.getRobotoMedium(context));
        tvPobocky.setTypeface(TypefaceUtils.getRobotoRegular(context));
        tvError.setTypeface(TypefaceUtils.getRobotoRegular(context));

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.FieldTypePobocky, 0, 0);

        try{
            if (ta.getString(R.styleable.FieldTypePobocky_setTitlePobocky) != null) {
                setTitle(ta.getString(R.styleable.FieldTypePobocky_setTitlePobocky));
            }

            if (ta.getString(R.styleable.FieldTypePobocky_setErrorPobocky) != null) {
                setError(ta.getString(R.styleable.FieldTypePobocky_setErrorPobocky));
            }
        }finally
        {
            ta.recycle();
        }
    }

    @Override
    public List<String> getText() {
        List<String> list = new ArrayList<>();
        if (!tvPobocky.getText().equals(mContext.getString(R.string.pobocky_list)))
            list.add(tvPobocky.getText().toString());
        return list;
    }

    /**
     * Get tv pobocky text view.
     *
     * @return the text view
     */
    public TextView getTvPobocky(){
        return tvPobocky;
    }

    /**
     * Set title pobocky.
     *
     * @param title the title
     */
    public void setTitlePobocky(String title){
        tvPobocky.setText(title);
    }
}
