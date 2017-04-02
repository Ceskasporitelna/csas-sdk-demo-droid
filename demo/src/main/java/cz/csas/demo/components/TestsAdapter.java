package cz.csas.demo.components;

import android.app.Activity;
import android.support.annotation.Nullable;
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
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class TestsAdapter extends BaseAdapter {

    /**
     * The Tv title.
     */
    @Nullable
    @Bind(R.id.tv_netbanking_test_name)
    TextView tvTitle;

    private List<String> mData;
    private Activity mActivity;

    public TestsAdapter(List<String> data, Activity activity) {
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.lv_netbanking_test_row, parent, false);
            ButterKnife.bind(this, convertView);
            holder = new Holder();
            holder.name = tvTitle;
            holder.name.setTypeface(TypefaceUtils.getRobotoMedium(mActivity));
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        String string = mData.get(position);

        holder.name.setText(string);

        return convertView;
    }


    private class Holder {
        /**
         * The Name.
         */
        public TextView name;
    }

}
