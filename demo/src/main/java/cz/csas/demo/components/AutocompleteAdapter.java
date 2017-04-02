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
import cz.csas.places.AutocompleteAddress;
import cz.csas.places.AutocompleteCity;
import cz.csas.places.AutocompletePostCode;

/**
 * The type Place adapter.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 29 /04/16.
 */
public class AutocompleteAdapter extends BaseAdapter {

    /**
     * The Tv name.
     */
    @Nullable
    @Bind(R.id.tv_name)
    public TextView tvName;

    /**
     * The Tv placeholder.
     */
    @Nullable
    @Bind(R.id.tv_placeholder_text)
    public TextView tvPlaceholder;

    private final int VIEW_TYPE_PLACEHOLDER = 0;
    private final int VIEW_TYPE_PLACE = 1;
    private List mData;
    private List mOriginalData;
    private Activity mActivity;

    /**
     * Instantiates a new Place adapter.
     *
     * @param activity the activity
     * @param data     the data
     */
    public AutocompleteAdapter(Activity activity, List data) {
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
        return (mData.size() == 0) ? VIEW_TYPE_PLACEHOLDER : VIEW_TYPE_PLACE;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (getItemViewType(i) == VIEW_TYPE_PLACE) {
            final Holder holder;
            if (convertView == null || convertView.getTag() instanceof PlaceholderHolder) {
                convertView = mActivity.getLayoutInflater().inflate(R.layout.autocomplete_item_component, viewGroup, false);
                ButterKnife.bind(this, convertView);
                holder = new Holder();
                holder.name = tvName;
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.name.setTypeface(TypefaceUtils.getRobotoRegular(mActivity));
            if (mData.get(i) instanceof AutocompleteAddress) {
                holder.name.setText(((AutocompleteAddress) mData.get(i)).getAddress() + ", " + ((AutocompleteAddress) mData.get(i)).getCity());
            } else if (mData.get(i) instanceof AutocompleteCity) {
                holder.name.setText(((AutocompleteCity) mData.get(i)).getCity());
            } else if (mData.get(i) instanceof AutocompletePostCode) {
                holder.name.setText(((AutocompletePostCode) mData.get(i)).getCity() + ", " + ((AutocompletePostCode) mData.get(i)).getPostCode());
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
            holder.text.setText(R.string.autocomplete_placeholder);
        }
        return convertView;
    }

    /**
     * Change data.
     *
     * @param changedData the changed data
     */
    public void changeData(List changedData) {
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

    }

    private class PlaceholderHolder {
        /**
         * The Text.
         */
        public TextView text;
    }
}
