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
import cz.csas.transparent_acc.TransparentAccount;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class TransparentAccountsAdapter extends BaseAdapter {

    /**
     * The Tv title.
     */
    @Nullable
    @Bind(R.id.tv_uniform_title)
    TextView tvTitle;

    /**
     * The Tv id.
     */
    @Nullable
    @Bind(R.id.tv_uniform_id)
    TextView tvId;

    /**
     * The Tv placeholder.
     */
    @Nullable
    @Bind(R.id.tv_placeholder_text)
    public TextView tvPlaceholder;

    private final int VIEW_TYPE_PLACEHOLDER = 0;
    private final int VIEW_TYPE_APPLICATION = 1;
    private List<TransparentAccount> mOriginalData;
    private List<TransparentAccount> mData;
    private Activity mActivity;

    public TransparentAccountsAdapter(List<TransparentAccount> data, Activity activity) {
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

            TransparentAccount transparentAccount = mData.get(position);

            if (transparentAccount.getAccountNumber() != null)
                holder.name.setText(transparentAccount.getAccountNumber());
            else
                holder.name.setText("N/A");
            if (transparentAccount.getBalance() != null && transparentAccount.getCurrency() != null)
                holder.id.setText(transparentAccount.getBalance() + " " + transparentAccount.getCurrency());
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
            holder.text.setText(R.string.trans_acc_placeholder);
        }
        return convertView;
    }

    /**
     * Change data.
     *
     * @param changedData the changed data
     */
    public void changeData(List<TransparentAccount> changedData) {
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

    private class PlaceholderHolder {
        /**
         * The Text.
         */
        public TextView text;
    }
}
