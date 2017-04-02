package cz.csas.demo.components;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.demo.App;
import cz.csas.demo.R;
import cz.csas.demo.utils.LocationUtils;
import cz.csas.lockerui.utils.TypefaceUtils;
import cz.csas.places.Place;
import cz.csas.places.Type;

/**
 * The type Place adapter.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 29 /04/16.
 */
public class PlaceAdapter extends BaseAdapter {

    /**
     * The Tv name.
     */
    @Nullable
    @Bind(R.id.tv_name)
    public TextView tvName;

    /**
     * The Tv distance.
     */
    @Nullable
    @Bind(R.id.tv_distance)
    public TextView tvDistance;

    /**
     * The Rl place type.
     */
    @Nullable
    @Bind(R.id.rl_place_type)
    public RelativeLayout rlPlaceType;

    /**
     * The Iv type image.
     */
    @Nullable
    @Bind(R.id.iv_type_image)
    public ImageView ivTypeImage;

    /**
     * The Tv placeholder.
     */
    @Nullable
    @Bind(R.id.tv_placeholder_text)
    public TextView tvPlaceholder;

    private final int VIEW_TYPE_PLACEHOLDER = 0;
    private final int VIEW_TYPE_PLACE = 1;
    private List<Place> mData;
    private List<Place> mOriginalData;
    private Activity mActivity;
    private Filter mParkingFilter;

    /**
     * Instantiates a new Place adapter.
     *
     * @param activity the activity
     * @param data     the data
     */
    public PlaceAdapter(Activity activity, List data) {
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
        return mData.get(i).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return (mData.size() == 0) ? VIEW_TYPE_PLACEHOLDER : VIEW_TYPE_PLACE;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (getItemViewType(i) == VIEW_TYPE_PLACE) {
            final Holder holder;
            if (convertView == null) {
                convertView = mActivity.getLayoutInflater().inflate(R.layout.place_item_component, viewGroup, false);
                ButterKnife.bind(this, convertView);
                holder = new Holder();
                holder.name = tvName;
                holder.distance = tvDistance;
                holder.typeImage = ivTypeImage;
                holder.typeLayout = rlPlaceType;
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.name.setTypeface(TypefaceUtils.getRobotoRegular(mActivity));
            holder.distance.setTypeface(TypefaceUtils.getRobotoRegular(mActivity));
            final Place place = mData.get(i);
            if (place != null) {
                holder.name.setText(place.getName());
                float distance = LocationUtils.getDistance(place.getLocation(), App.getLocationManager().getLocation());
                holder.distance.setText(String.format("%s", distance) + "m");
                if (place.getType() == Type.ATM)
                    holder.typeImage.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.marker_atm));
                else if (place.getType() == Type.BRANCH)
                    holder.typeImage.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.marker_branch));
            }
        } else {
            PlaceholderHolder holder;
            if (convertView == null) {
                convertView = mActivity.getLayoutInflater().inflate(R.layout.place_item_placeholder_component, viewGroup, false);
                ButterKnife.bind(this, convertView);
                holder = new PlaceholderHolder();
                holder.text = tvPlaceholder;
                convertView.setTag(holder);
            } else {
                holder = (PlaceholderHolder) convertView.getTag();
            }

            holder.text.setTypeface(TypefaceUtils.getRobotoRegular(mActivity));
            holder.text.setText(R.string.place_placeholder_no_items);
        }
        return convertView;
    }

    /**
     * Change data.
     *
     * @param changedData the changed data
     */
    public void changeData(List<Place> changedData) {
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
         * The Distance.
         */
        public TextView distance;

        /**
         * The Type image.
         */
        public ImageView typeImage;

        /**
         * The Type layout.
         */
        public RelativeLayout typeLayout;
    }

    private class PlaceholderHolder {
        /**
         * The Text.
         */
        public TextView text;
    }
}
