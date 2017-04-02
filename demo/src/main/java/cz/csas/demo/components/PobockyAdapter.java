package cz.csas.demo.components;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.demo.R;
import cz.csas.lockerui.utils.TypefaceUtils;
import cz.csas.uniforms.Branch;

/**
 * The type Combobox spinner adapter.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class PobockyAdapter extends BaseAdapter {

    /**
     * The Tv spinner row name.
     */
    @Bind(R.id.tv_pobocka_address)
    TextView tvAddress;

    /**
     * The Tv city.
     */
    @Bind(R.id.tv_pobocka_city)
    TextView tvCity;

    private List<Branch> mOriginalData;
    private List<Branch> mData;
    private Activity mActivity;

    /**
     * Instantiates a new Combobox spinner adapter.
     *
     * @param data     the data
     * @param activity the activity
     */
    public PobockyAdapter(HashMap<String, Branch> data, Activity activity) {
        this.mActivity = activity;
        mData = new ArrayList<>();
        for (String string : data.keySet()) {
            mData.add(data.get(string));
        }
        mOriginalData = mData;
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
        return mData.get(position).getBranchId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = mActivity.getLayoutInflater().inflate(R.layout.field_type_pobocky_list_row, parent, false);
            ButterKnife.bind(this, convertView);
            holder.address = tvAddress;
            holder.city = tvCity;
            holder.address.setTypeface(TypefaceUtils.getRobotoMedium(mActivity));
            holder.city.setTypeface(TypefaceUtils.getRobotoRegular(mActivity));
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Branch branch = mData.get(position);

        if(branch.getAddress() != null)
            holder.address.setText(branch.getAddress());
        if(branch.getCityName() != null)
            holder.city.setText(branch.getCityName());

        return convertView;
    }

    /**
     * Change data.
     *
     * @param changedData the changed data
     */
    public void changeData(HashMap<String,Branch> changedData) {
        mOriginalData.clear();
        mData.clear();
        for (String string : changedData.keySet()) {
            mOriginalData.add(changedData.get(string));
        }
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
        public TextView address;
        /**
         * The City.
         */
        public TextView city;
    }
}
