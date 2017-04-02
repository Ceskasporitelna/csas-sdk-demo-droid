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
import cz.csas.corporate.companies.Company;
import cz.csas.demo.R;
import cz.csas.lockerui.utils.TypefaceUtils;

/**
 * The type Companies adapter.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class CorporateCompaniesAdapter extends BaseAdapter {

    /**
     * The M tv company name.
     */
    @Nullable
    @Bind(R.id.tv_company_name)
    TextView mTvCompanyName;

    /**
     * The M tv company id.
     */
    @Nullable
    @Bind(R.id.tv_company_ico)
    TextView mTvCompanyIco;

    /**
     * The Tv placeholder.
     */
    @Nullable
    @Bind(R.id.tv_placeholder_text)
    public TextView tvPlaceholder;

    private final int VIEW_TYPE_PLACEHOLDER = 0;
    private final int VIEW_TYPE_APPLICATION = 1;
    private List<Company> mOriginalData;
    private List<Company> mData;
    private Activity mActivity;

    /**
     * Instantiates a new Accounts adapter.
     *
     * @param data     the data
     * @param activity the activity
     */
    public CorporateCompaniesAdapter(List<Company> data, Activity activity) {
        this.mOriginalData = data;
        this.mData = data;
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        return mData.size() != 0 ? mData.size() : 1;
    }

    @Override
    public Object getItem(int position) {
        if (mData.size() != 0)
            return mData.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public int getItemViewType(int position) {
        return (mData.size() == 0) ? VIEW_TYPE_PLACEHOLDER : VIEW_TYPE_APPLICATION;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == VIEW_TYPE_APPLICATION) {
            Holder holder;
            if (convertView == null || convertView.getTag() instanceof PlaceholderHolder) {
                convertView = mActivity.getLayoutInflater().inflate(R.layout.lv_corporate_company_row, parent, false);
                ButterKnife.bind(this, convertView);
                holder = new Holder();
                holder.name = mTvCompanyName;
                holder.ico = mTvCompanyIco;
                holder.name.setTypeface(TypefaceUtils.getRobotoRegular(mActivity));
                holder.ico.setTypeface(TypefaceUtils.getRobotoMedium(mActivity));
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            Company company = mData.get(position);

            if (company.getName() != null)
                holder.name.setText(company.getName());
            else
                holder.name.setText("N/A");

            if (company.getRegNum() != null)
                holder.ico.setText(company.getRegNum());
            else
                holder.ico.setText("N/A");
        } else {
            PlaceholderHolder holder;
            if (convertView == null || convertView.getTag() instanceof Holder) {
                convertView = mActivity.getLayoutInflater().inflate(R.layout.place_item_placeholder_component, parent, false);
                ButterKnife.bind(this, convertView);
                holder = new PlaceholderHolder();
                holder.text = tvPlaceholder;
                convertView.setTag(holder);
            } else {
                holder = (PlaceholderHolder) convertView.getTag();
            }

            holder.text.setTypeface(TypefaceUtils.getRobotoRegular(mActivity));
            holder.text.setText(R.string.corporate_company_placeholder);
        }
        return convertView;
    }

    /**
     * Change data.
     *
     * @param changedData the changed data
     */
    public void changeData(List<Company> changedData) {
        mOriginalData.clear();
        mData.clear();
        mOriginalData = changedData;
        mData = mOriginalData;
        notifyDataSetChanged();
    }

    private class Holder {
        public TextView name;
        public TextView ico;
    }

    private class PlaceholderHolder {
        /**
         * The Text.
         */
        public TextView text;
    }
}
