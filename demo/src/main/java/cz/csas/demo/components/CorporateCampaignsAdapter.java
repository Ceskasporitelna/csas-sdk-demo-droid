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
import cz.csas.corporate.companies.Campaign;
import cz.csas.cscore.utils.TimeUtils;
import cz.csas.demo.R;
import cz.csas.lockerui.utils.TypefaceUtils;

/**
 * The type Campaigns adapter.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class CorporateCampaignsAdapter extends BaseAdapter {

    /**
     * The M tv campaign name.
     */
    @Nullable
    @Bind(R.id.tv_campaign_name)
    TextView mTvCampaignName;

    /**
     * The M tv campaign end date.
     */
    @Nullable
    @Bind(R.id.tv_campaing_end_date)
    TextView mTvCampaignEndDate;

    /**
     * The Tv placeholder.
     */
    @Nullable
    @Bind(R.id.tv_placeholder_text)
    public TextView tvPlaceholder;

    private final int VIEW_TYPE_PLACEHOLDER = 0;
    private final int VIEW_TYPE_APPLICATION = 1;
    private List<Campaign> mOriginalData;
    private List<Campaign> mData;
    private Activity mActivity;

    /**
     * Instantiates a new Accounts adapter.
     *
     * @param data     the data
     * @param activity the activity
     */
    public CorporateCampaignsAdapter(List<Campaign> data, Activity activity) {
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
                convertView = mActivity.getLayoutInflater().inflate(R.layout.lv_corporate_campaign_row, parent, false);
                ButterKnife.bind(this, convertView);
                holder = new Holder();
                holder.name = mTvCampaignName;
                holder.endDate = mTvCampaignEndDate;
                holder.name.setTypeface(TypefaceUtils.getRobotoRegular(mActivity));
                holder.endDate.setTypeface(TypefaceUtils.getRobotoMedium(mActivity));
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            Campaign campaign = mData.get(position);

            if (campaign.getName() != null)
                holder.name.setText(campaign.getName());
            else
                holder.name.setText("N/A");

            if (campaign.getEndDate() != null)
                holder.endDate.setText(TimeUtils.getISO8601String(campaign.getEndDate()));
            else
                holder.endDate.setText("N/A");
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
            holder.text.setText(R.string.corporate_campaigns_placeholder);
        }
        return convertView;
    }

    /**
     * Change data.
     *
     * @param changedData the changed data
     */
    public void changeData(List<Campaign> changedData) {
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
         * The End date.
         */
        public TextView endDate;
    }

    private class PlaceholderHolder {
        /**
         * The Text.
         */
        public TextView text;
    }
}
