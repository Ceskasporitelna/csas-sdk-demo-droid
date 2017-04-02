package cz.csas.demo.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

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
public class FieldTypeRadio extends FieldTypeView {

    /**
     * The Ll radios.
     */
    @Bind(R.id.ll_radios)
    LinearLayout llRadios;

    private LayoutInflater mInflater;
    private List<String> mCheckTexts;

    /**
     * Instantiates a new Field type checkbox.
     *
     * @param context the context
     * @param options the options
     */
    public FieldTypeRadio(Context context,List<String> options) {
        super(context);
        setTextOptions(options);
        init(context, null);
    }

    /**
     * Instantiates a new Field type checkbox.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public FieldTypeRadio(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Instantiates a new Field type checkbox.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public FieldTypeRadio(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.field_type_radio, this, true);

        ButterKnife.bind(this);

        tvFieldTitle.setTypeface(TypefaceUtils.getRobotoMedium(context));
        tvError.setTypeface(TypefaceUtils.getRobotoRegular(context));


        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.FieldTypeRadio, 0, 0);
        try {
            if (ta.getString(R.styleable.FieldTypeRadio_setTitleRadio) != null) {
                setTitle(ta.getString(R.styleable.FieldTypeRadio_setTitleRadio));
            }
            if (ta.getString(R.styleable.FieldTypeRadio_setErrorRadio) != null) {
                setError(ta.getString(R.styleable.FieldTypeRadio_setErrorRadio));
            }
            if (ta.getTextArray(R.styleable.FieldTypeRadio_setOptionsArrayRadio) != null){
                setTextOptions(ta.getTextArray(R.styleable.FieldTypeRadio_setOptionsArrayRadio));
            }
        }finally
        {
            for (int i = 0; i<mCheckTexts.size();i++){
                final FieldTypeRadioRow fieldTypeRadioRow = new FieldTypeRadioRow(context);
                fieldTypeRadioRow.setTitleRadio(mCheckTexts.get(i));
                fieldTypeRadioRow.getRbRadio().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked)
                            recheck(fieldTypeRadioRow.getRbRadio());
                    }
                });
                llRadios.addView(fieldTypeRadioRow);
            }
            ta.recycle();
        }
    }

    @Override
    public List<String> getText() {
        List<String> texts = new ArrayList<>();
        for (int i = 0; i < llRadios.getChildCount(); i++){
            if(((FieldTypeRadioRow)llRadios.getChildAt(i)).isChecked())
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

    private void recheck(RadioButton radioButton){
        for (int i = 0; i < llRadios.getChildCount(); i++){
            if(radioButton != ((FieldTypeRadioRow)llRadios.getChildAt(i)).getRbRadio()){
                ((FieldTypeRadioRow)llRadios.getChildAt(i)).getRbRadio().setChecked(false);
            }
        }
    }
}
