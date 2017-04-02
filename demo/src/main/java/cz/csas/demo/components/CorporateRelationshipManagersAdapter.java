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
import cz.csas.corporate.companies.RelationshipManager;
import cz.csas.demo.R;
import cz.csas.lockerui.utils.TypefaceUtils;

/**
 * The type Relationship Managers adapter.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class CorporateRelationshipManagersAdapter extends BaseAdapter {

    @Nullable
    @Bind(R.id.tv_manager_name)
    TextView mTvManagerName;

    @Nullable
    @Bind(R.id.tv_manager_id)
    TextView mTvManagerId;

    /**
     * The Tv placeholder.
     */
    @Nullable
    @Bind(R.id.tv_placeholder_text)
    public TextView tvPlaceholder;

    private final int VIEW_TYPE_PLACEHOLDER = 0;
    private final int VIEW_TYPE_APPLICATION = 1;
    private List<RelationshipManager> mOriginalData;
    private List<RelationshipManager> mData;
    private Activity mActivity;

    /**
     * Instantiates a new Accounts adapter.
     *
     * @param data     the data
     * @param activity the activity
     */
    public CorporateRelationshipManagersAdapter(List<RelationshipManager> data, Activity activity) {
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
                convertView = mActivity.getLayoutInflater().inflate(R.layout.lv_corporate_relationship_manager_row, parent, false);
                ButterKnife.bind(this, convertView);
                holder = new Holder();
                holder.name = mTvManagerName;
                holder.id = mTvManagerId;
                holder.name.setTypeface(TypefaceUtils.getRobotoRegular(mActivity));
                holder.id.setTypeface(TypefaceUtils.getRobotoMedium(mActivity));
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            RelationshipManager relationshipManager = mData.get(position);

            if (relationshipManager.getName() != null)
                holder.name.setText(relationshipManager.getName());
            else
                holder.name.setText("N/A");

            if (relationshipManager.getId() != null)
                holder.id.setText(relationshipManager.getId());
            else
                holder.id.setText("N/A");
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
            holder.text.setText(R.string.corporate_relationship_managers_placeholder);
        }
        return convertView;
    }

    /**
     * Change data.
     *
     * @param changedData the changed data
     */
    public void changeData(List<RelationshipManager> changedData) {
        mOriginalData.clear();
        mData.clear();
        mOriginalData = changedData;
        mData = mOriginalData;
        notifyDataSetChanged();
    }

    private class Holder {
        public TextView name;
        public TextView id;
    }

    private class PlaceholderHolder {
        /**
         * The Text.
         */
        public TextView text;
    }
}
