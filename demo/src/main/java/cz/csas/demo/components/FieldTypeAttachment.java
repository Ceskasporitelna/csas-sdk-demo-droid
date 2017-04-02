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
 * The type Field type attachment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 07 /01/16.
 */
public class FieldTypeAttachment extends FieldTypeView {

    /**
     * The Tv attachment.
     */
    @Bind(R.id.tv_attachment)
    TextView tvAttachment;

    private LayoutInflater mInflater;
    private Context mContext;

    /**
     * Instantiates a new Field type attachment.
     *
     * @param context the context
     */
    public FieldTypeAttachment(Context context) {
        super(context);
        init(context,null);
    }

    /**
     * Instantiates a new Field type attachment.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public FieldTypeAttachment(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    /**
     * Instantiates a new Field type attachment.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public FieldTypeAttachment(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.field_type_attachment, this, true);
        mContext = context;

        ButterKnife.bind(this);

        tvFieldTitle.setTypeface(TypefaceUtils.getRobotoMedium(context));
        tvError.setTypeface(TypefaceUtils.getRobotoRegular(context));
        tvAttachment.setTypeface(TypefaceUtils.getRobotoRegular(context));

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.FieldTypeAttachment, 0, 0);

        try{
            if (ta.getString(R.styleable.FieldTypeAttachment_setTitleAttachment) != null) {
                setTitle(ta.getString(R.styleable.FieldTypeAttachment_setTitleAttachment));
            }

            if (ta.getString(R.styleable.FieldTypeAttachment_setErrorAttachment) != null) {
                setError(ta.getString(R.styleable.FieldTypeAttachment_setErrorAttachment));
            }
        }finally
        {
            ta.recycle();
        }
    }

    @Override
    public List<String> getText() {
        List<String> list = new ArrayList<>();
        if (!tvAttachment.getText().equals(mContext.getString(R.string.attachment)))
            list.add(tvAttachment.getText().toString());
        return list;
    }

    /**
     * Gets tv attachment.
     *
     * @return the tv attachment
     */
    public TextView getTvAttachment() {
        return tvAttachment;
    }


    /**
     * Set title pobocky.
     *
     * @param title the title
     */
    public void setTitleAttachment(String title){
        tvAttachment.setText(title);
    }
}
