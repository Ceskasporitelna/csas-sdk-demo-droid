package cz.csas.demo.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;

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
public class FieldTypeTextView extends FieldTypeView {

    /**
     * The Et field.
     */
    @Bind(R.id.et_field)
    EditText etField;

    private LayoutInflater mInflater;

    /**
     * Instantiates a new Field type text view.
     *
     * @param context the context
     */
    public FieldTypeTextView(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Instantiates a new Field type text view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public FieldTypeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Instantiates a new Field type text view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public FieldTypeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.field_type_text_view, this, true);

        ButterKnife.bind(this);

        tvFieldTitle.setTypeface(TypefaceUtils.getRobotoMedium(context));
        etField.setTypeface(TypefaceUtils.getRobotoRegular(context));
        tvError.setTypeface(TypefaceUtils.getRobotoRegular(context));

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FieldTypeTextView, 0, 0);

        try {
            if (ta.getString(R.styleable.FieldTypeTextView_setTitleTextView) != null) {
                setTitle(ta.getString(R.styleable.FieldTypeTextView_setTitleTextView));
            }

            if (ta.getString(R.styleable.FieldTypeTextView_setHintTextView) != null) {
                setHint(ta.getString(R.styleable.FieldTypeTextView_setHintTextView));
            }

            if (ta.getString(R.styleable.FieldTypeTextView_setErrorTextView) != null) {
                setError(ta.getString(R.styleable.FieldTypeTextView_setErrorTextView));
            }

            if (ta.getString(R.styleable.FieldTypeTextView_setTextEditText) != null) {
                setText(ta.getString(R.styleable.FieldTypeTextView_setTextEditText));
            }
            setEnabled(ta.getBoolean(R.styleable.FieldTypeTextView_setEnabled, true));
        } finally {
            ta.recycle();
        }
    }

    @Override
    public List<String> getText() {
        List<String> texts = new ArrayList<>();
        texts.add(etField.getText().toString());
        return texts;
    }

    /**
     * Set hint.
     *
     * @param hint the hint
     */
    public void setHint(String hint) {
        etField.setHint(hint);
    }

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void setText(String text) {
        etField.setText(text);
    }

    public void setEnabled(boolean enabled) {
        etField.setEnabled(enabled);
    }

}
