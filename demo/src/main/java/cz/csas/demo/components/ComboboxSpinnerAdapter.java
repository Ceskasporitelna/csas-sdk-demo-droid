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

/**
 * The type Combobox spinner adapter.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class ComboboxSpinnerAdapter extends BaseAdapter {

    /**
     * The Tv spinner row name.
     */
    @Bind(R.id.tv_spinner_row_name)
    TextView tvSpinnerRowName;

    private List<String> mOriginalData;
    private List<String> mData;
    private Activity mActivity;

    /**
     * Instantiates a new Combobox spinner adapter.
     *
     * @param data     the data
     * @param activity the activity
     */
    public ComboboxSpinnerAdapter(List<String> data, Activity activity) {
        this.mActivity = activity;
        mData = data;
        mOriginalData = data;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = mActivity.getLayoutInflater().inflate(R.layout.field_type_combobox_spinner_row, parent, false);
            ButterKnife.bind(this, convertView);
            holder.name = tvSpinnerRowName;
            holder.name.setTypeface(TypefaceUtils.getRobotoRegular(mActivity));
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        String name = mData.get(position);

        if(name != null)
            holder.name.setText(name);
        return convertView;
    }

    /**
     * Change data.
     *
     * @param changedData the changed data
     */
    public void changeData(List<String> changedData) {
        mOriginalData.clear();
        mData.clear();
        mOriginalData = changedData;
        mData = mOriginalData;
        notifyDataSetChanged();
    }

    /**
     * The type Holder.
     */
    public class Holder {
        /**
         * The Name.
         */
        public TextView name;
    }
}
