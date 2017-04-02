package cz.csas.demo.components;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.appmenu.AppItem;
import cz.csas.demo.R;
import cz.csas.lockerui.utils.TypefaceUtils;

/**
 * The type App menu adapter.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 24 /05/16.
 */
public class AppMenuAdapter extends BaseAdapter {

    /**
     * The Tv name.
     */
    @Nullable
    @Bind(R.id.tv_appmenu_name)
    public TextView tvName;

    /**
     * The Btn app menu state.
     */
    @Nullable
    @Bind(R.id.btn_appmenu_state)
    public Button btnAppMenuState;

    /**
     * The Iv app icon.
     */
    @Nullable
    @Bind(R.id.iv_app_icon)
    public ImageView ivAppIcon;

    /**
     * The Tv placeholder.
     */
    @Nullable
    @Bind(R.id.tv_placeholder_text)
    public TextView tvPlaceholder;

    private final int VIEW_TYPE_PLACEHOLDER = 0;
    private final int VIEW_TYPE_APPLICATION = 1;
    private List<AppItem> mData;
    private List<AppItem> mOriginalData;
    private Activity mActivity;

    /**
     * Instantiates a new App menu adapter.
     *
     * @param activity the activity
     * @param data     the data
     */
    public AppMenuAdapter(Activity activity, List<AppItem> data) {
        this.mData = data;
        this.mOriginalData = data;
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
    public int getItemViewType(int position) {
        return (mData.size() == 0) ? VIEW_TYPE_PLACEHOLDER : VIEW_TYPE_APPLICATION;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (getItemViewType(i) == VIEW_TYPE_APPLICATION) {
            final Holder holder;
            if (convertView == null || convertView.getTag() instanceof PlaceholderHolder) {
                convertView = mActivity.getLayoutInflater().inflate(R.layout.lv_appmenu_row, viewGroup, false);
                ButterKnife.bind(this, convertView);
                holder = new Holder();
                holder.name = tvName;
                holder.state = btnAppMenuState;
                holder.icon = ivAppIcon;
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            AppItem application = mData.get(i);
            if (application != null) {
                holder.name.setText(application.getAppName());
                if (application.isInstalled(mActivity))
                    holder.state.setText(R.string.open_appmenu);
                else
                    holder.state.setText(R.string.install_appmenu);
                if (application.getAppIconUrl() != null)
                    Picasso.with(mActivity).load(application.getAppIconUrl()).into(holder.icon);
            }

        } else {
            PlaceholderHolder holder;
            if (convertView == null || convertView.getTag() instanceof Holder) {
                convertView = mActivity.getLayoutInflater().inflate(R.layout.place_item_placeholder_component, viewGroup, false);
                ButterKnife.bind(this, convertView);
                holder = new PlaceholderHolder();
                holder.text = tvPlaceholder;
                convertView.setTag(holder);
            } else {
                holder = (PlaceholderHolder) convertView.getTag();
            }

            holder.text.setTypeface(TypefaceUtils.getRobotoRegular(mActivity));
            holder.text.setText(R.string.appmenu_placeholder);
        }
        return convertView;
    }

    /**
     * Change data.
     *
     * @param changedData the changed data
     */
    public void changeData(List<AppItem> changedData) {
        mOriginalData.clear();
        mData.clear();
        mOriginalData = changedData;
        mData = mOriginalData;
        notifyDataSetChanged();
        notifyDataSetInvalidated();
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

        /**
         * The Icon.
         */
        public ImageView icon;

    }

    private class PlaceholderHolder {
        /**
         * The Text.
         */
        public TextView text;
    }
}
