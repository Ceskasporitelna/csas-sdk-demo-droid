package cz.csas.demo.places;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.demo.App;
import cz.csas.demo.R;
import cz.csas.demo.components.PlaceAdapter;
import cz.csas.demo.utils.LocationUtils;
import cz.csas.places.Place;

/**
 * The type Place list fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 29 /04/16.
 */
public class PlaceListFragment extends Fragment {

    /**
     * The M lv places.
     */
    @Bind(R.id.lv_place_list)
    ListView mLvPlaces;

    private View mRootView;
    private PlaceAdapter mPlaceAdapter;
    private FragmentCallback mFragmentCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentCallback = (FragmentCallback) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.place_list_fragment, container, false);
        ButterKnife.bind(this, mRootView);
        List<Place> placeList = App.getPlaceManager().getPlaces();
        sortList(placeList);
        mPlaceAdapter = new PlaceAdapter(getActivity(), App.getPlaceManager().getPlaces());
        mLvPlaces.setAdapter(mPlaceAdapter);
        mLvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Place place = (Place) mPlaceAdapter.getItem(i);
                if (place != null) {
                    mFragmentCallback.changeFragmentToDetail(String.valueOf(place.getId()), place.getType().getValue());
                }
            }
        });
        return mRootView;
    }

    private void sortList(List<Place> places) {
        Collections.sort(places, new Comparator<Place>() {
            @Override
            public int compare(Place lhs, Place rhs) {
                android.location.Location actualLocation = App.getLocationManager().getLocation();
                return (int) (LocationUtils.getDistance(lhs.getLocation(), actualLocation) - LocationUtils.getDistance(rhs.getLocation(), actualLocation));
            }
        });
    }
}
