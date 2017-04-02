package cz.csas.demo.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.demo.R;
import cz.csas.lockerui.utils.TypefaceUtils;

/**
 * The type Field type checkbox.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 07 /01/16.
 */
public class FieldTypeCheckbox extends FieldTypeView {

    /**
     * The Ll checkboxes.
     */
    @Bind(R.id.ll_checkboxes)
    LinearLayout llCheckboxes;

    private LayoutInflater mInflater;
    private List<String> mCheckTexts;

    /**
     * Instantiates a new Field type checkbox.
     *
     * @param context the context
     * @param options the options
     */
    public FieldTypeCheckbox(Context context,List<String> options) {
        super(context);
        setTextOptions(options);
        init(context,null);
    }

    /**
     * Instantiates a new Field type checkbox.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public FieldTypeCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    /**
     * Instantiates a new Field type checkbox.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public FieldTypeCheckbox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.field_type_checkbox, this, true);

        ButterKnife.bind(this);

        tvFieldTitle.setTypeface(TypefaceUtils.getRobotoMedium(context));
        tvError.setTypeface(TypefaceUtils.getRobotoRegular(context));

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.FieldTypeCheckbox, 0, 0);
        try {
            if (ta.getString(R.styleable.FieldTypeCheckbox_setTitleCheckBox) != null) {
                setTitle(ta.getString(R.styleable.FieldTypeCheckbox_setTitleCheckBox));
            }
            if (ta.getString(R.styleable.FieldTypeCheckbox_setErrorCheckBox) != null) {
                setError(ta.getString(R.styleable.FieldTypeCheckbox_setErrorCheckBox));
            }
            if (ta.getTextArray(R.styleable.FieldTypeCheckbox_setOptionsArrayCheckBox) != null){
                setTextOptions(ta.getTextArray(R.styleable.FieldTypeCheckbox_setOptionsArrayCheckBox));
            }

        }finally
        {
            for (int i = 0; i<mCheckTexts.size();i++){
                FieldTypeCheckboxRow fieldTypeCheckboxRow = new FieldTypeCheckboxRow(context);
                fieldTypeCheckboxRow.setTitleCheckbox(mCheckTexts.get(i));
                llCheckboxes.addView(fieldTypeCheckboxRow);
            }
            ta.recycle();
        }
    }

    @Override
    public List<String> getText() {
        List<String> texts = new ArrayList<>();
        for (int i = 0; i < llCheckboxes.getChildCount(); i++){
            if(((FieldTypeCheckboxRow)llCheckboxes.getChildAt(i)).isChecked())
                texts.add(mCheckTexts.get(i));
        }
        return texts;
    }

    /**
     * Set text options.
     *
     * @param texts the texts
     */
    public void setTextOptions(List<String> texts){
        this.mCheckTexts = texts;
    }

    /**
     * Set text options.
     *
     * @param texts the texts
     */
    public void setTextOptions(CharSequence[] texts){
        for (int i = 0; i< texts.length; i++){
            mCheckTexts.add(texts[i].toString());
        }
    }

}
