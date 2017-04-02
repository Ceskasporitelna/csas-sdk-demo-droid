package cz.csas.demo.components;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import cz.csas.demo.R;

/**
 * The type Field type view.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 07 /01/16.
 */
public abstract class FieldTypeView extends LinearLayout {

    /**
     * The Tv field title.
     */
    @Bind(R.id.tv_field_title)
    TextView tvFieldTitle;

    /**
     * The Tv error.
     */
    @Bind(R.id.tv_error)
    TextView tvError;

    private int fieldId;

    /**
     * Instantiates a new Field type view.
     *
     * @param context the context
     */
    public FieldTypeView(Context context) {
        super(context);
    }

    /**
     * Instantiates a new Field type view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public FieldTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Instantiates a new Field type view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public FieldTypeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public abstract List<String> getText();

    /**
     * Set error.
     *
     * @param errors the errors
     */
    public void setError(List<String> errors){
        if(errors != null)
            tvError.setText(TextUtils.join(",",errors));
        else
            tvError.setText("");
    }

    /**
     * Get field id int.
     *
     * @return the int
     */
    public int getFieldId(){
        return fieldId;
    }

    /**
     * Set field id.
     *
     * @param fieldId the field id
     */
    public void setFieldId(int fieldId){
        this.fieldId = fieldId;
    }

    /**
     * Set error.
     *
     * @param error the error
     */
    public void setError(String error){
        tvError.setText(error);
    }

    /**
     * Set title.
     *
     * @param title the title
     */
    public void setTitle(String title){
        tvFieldTitle.setText(title);
    }

}
