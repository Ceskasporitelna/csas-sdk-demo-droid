package cz.csas.demo.components;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.demo.R;
import cz.csas.lockerui.utils.TypefaceUtils;
import cz.csas.uniforms.FormListing;

/**
 * The type Uniforms adapter.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class UniformsAdapter extends BaseAdapter {

    /**
     * The Tv title.
     */
    @Bind(R.id.tv_uniform_title)
    TextView tvTitle;

    /**
     * The Tv id.
     */
    @Bind(R.id.tv_uniform_id)
    TextView tvId;

    private List<FormListing> mOriginalData;
    private List<FormListing> mData;
    private Activity mActivity;

    /**
     * Instantiates a new Uniforms adapter.
     *
     * @param data     the data
     * @param activity the activity
     */
    public UniformsAdapter(List<FormListing> data, Activity activity) {
        this.mOriginalData = data;
        this.mData = data;
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.lv_uniforms_row, parent, false);
            ButterKnife.bind(this, convertView);
            holder = new Holder();
            holder.name = tvTitle;
            holder.id = tvId;
            holder.name.setTypeface(TypefaceUtils.getRobotoMedium(mActivity));
            holder.id.setTypeface(TypefaceUtils.getRobotoRegular(mActivity));
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        FormListing formListing = mData.get(position);

        if (formListing.getNameI18N() != null)
            holder.name.setText(formListing.getNameI18N());
        if (formListing.getId() != null)
            holder.id.setText(mActivity.getString(R.string.form_list_id)+String.valueOf(formListing.getId()));

        return convertView;
    }

    /**
     * Change data.
     *
     * @param changedData the changed data
     */
    public void changeData(List<FormListing> changedData) {
        mOriginalData.clear();
        mData.clear();
        mOriginalData = changedData;
        mData = mOriginalData;
        notifyDataSetChanged();
    }

    private class Holder {
        /**
         * The Name.
         */
        public TextView name;
        /**
         * The Id.
         */
        public TextView id;
    }
}
