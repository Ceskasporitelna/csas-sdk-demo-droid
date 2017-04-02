package cz.csas.demo.components;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.demo.R;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;

/**
 * The type App menu adapter.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 24 /05/16.
 */
public class TestCaseAdapter extends BaseAdapter {

    /**
     * The Tv name.
     */
    @Nullable
    @Bind(R.id.tv_netbanking_test_name)
    public TextView tvName;

    /**
     * The Btn app menu state.
     */
    @Nullable
    @Bind(R.id.btn_netbanking_test_status)
    public Button btnAppMenuState;

    private List<TestCase> mData;
    private Activity mActivity;

    /**
     * Instantiates a new App menu adapter.
     *
     * @param activity the activity
     * @param data     the data
     */
    public TestCaseAdapter(Activity activity, List<TestCase> data) {
        this.mData = data;
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        if (mData.size() == 0)
            return 1;
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        if (mData.size() == 0)
            return null;
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (mData.size() == 0)
            return Integer.MAX_VALUE;
        return mData.get(i).hashCode();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final Holder holder;
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.lv_netbanking_row, viewGroup, false);
            ButterKnife.bind(this, convertView);
            holder = new Holder();
            holder.name = tvName;
            holder.state = btnAppMenuState;
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        TestCase testCase = mData.get(i);
        if (testCase != null) {
            holder.name.setText(testCase.getName());
            if (testCase.getTestResult().getStatus() == TestStatus.OK) {
                holder.state.setText("OK");
                holder.state.setTextColor(ContextCompat.getColor(mActivity, R.color.csasColorPrimary));
            } else if (testCase.getTestResult().getStatus() == TestStatus.FAILURE) {
                holder.state.setText("FAIL");
                holder.state.setTextColor(ContextCompat.getColor(mActivity, R.color.csasColorRed));
            } else
                holder.state.setText("");
        }
        return convertView;
    }

    /**
     * Change data.
     *
     * @param changedData the changed data
     */
    public void changeData(List<TestCase> changedData) {
        mData = changedData;
        notifyDataSetChanged();
    }

    private class Holder {
        /**
         * The Name.
         */
        public TextView name;
        /**
         * The State.
         */
        public Button state;
    }

}
