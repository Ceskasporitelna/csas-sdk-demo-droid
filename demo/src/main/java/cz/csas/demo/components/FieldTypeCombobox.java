package cz.csas.demo.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.demo.R;
import cz.csas.lockerui.utils.TypefaceUtils;

/**
 * The type Field type text view.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class FieldTypeCombobox extends FieldTypeView {

    /**
     * The Et field.
     */
    @Bind(R.id.s_combobox)
    Spinner sCombobox;

    private LayoutInflater mInflater;
    private String mSpinnerText;

    /**
     * Instantiates a new Field type text view.
     *
     * @param context the context
     */
    public FieldTypeCombobox(Context context) {
        super(context);
        init(context,null);
    }

    /**
     * Instantiates a new Field type text view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public FieldTypeCombobox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    /**
     * Instantiates a new Field type text view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public FieldTypeCombobox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.field_type_combobox, this, true);

        ButterKnife.bind(this);

        tvFieldTitle.setTypeface(TypefaceUtils.getRobotoMedium(context));
        tvError.setTypeface(TypefaceUtils.getRobotoRegular(context));

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.FieldTypeCombobox, 0, 0);
        try{
            if (ta.getString(R.styleable.FieldTypeCombobox_setTitleCombobox) != null) {
                setTitle(ta.getString(R.styleable.FieldTypeCombobox_setTitleCombobox));
            }

            if (ta.getString(R.styleable.FieldTypeCombobox_setErrorCombobox) != null) {
                setError(ta.getString(R.styleable.FieldTypeCombobox_setErrorCombobox));
            }

        }finally
        {
            ta.recycle();
        }
    }

    @Override
    public List<String> getText() {
        List<String> texts = new ArrayList<>();
        texts.add(mSpinnerText);
        return texts;
    }

    /**
     * Get spinner spinner.
     *
     * @return the spinner
     */
    public Spinner getSpinner(){
        return sCombobox;
    }

    /**
     * Set text.
     *
     * @param text the text
     */
    public void setText(String text){
        mSpinnerText = text;
    }
}
