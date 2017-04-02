package cz.csas.demo.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.demo.R;
import cz.csas.lockerui.utils.TypefaceUtils;

/**
 * The type Field type checkbox row.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 07 /01/16.
 */
public class FieldTypeRadioRow extends RelativeLayout {

    /**
     * The Tv title.
     */
    @Bind(R.id.tv_radio_title)
    TextView tvTitleRadio;

    /**
     * The Chb checkbox.
     */
    @Bind(R.id.rb_radio)
    RadioButton rbRadio;

    private LayoutInflater mInflater;

    /**
     * Instantiates a new Field type checkbox row.
     *
     * @param context the context
     */
    public FieldTypeRadioRow(Context context) {
        super(context);
        init(context,null);
    }

    /**
     * Instantiates a new Field type checkbox row.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public FieldTypeRadioRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Instantiates a new Field type checkbox row.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public FieldTypeRadioRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs){
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.field_type_radio_row, this, true);
        ButterKnife.bind(this);

        tvTitleRadio.setTypeface(TypefaceUtils.getRobotoRegular(context));

    }

    /**
     * Is checked boolean.
     *
     * @return the boolean
     */
    public boolean isChecked(){
        return rbRadio.isChecked();
    }


    /**
     * Get rb radio radio button.
     *
     * @return the radio button
     */
    public RadioButton getRbRadio(){
        return rbRadio;
    }

    /**
     * Set title checkbox.
     *
     * @param title the title
     */
    public void setTitleRadio(String title){
        tvTitleRadio.setText(title);
    }
}
