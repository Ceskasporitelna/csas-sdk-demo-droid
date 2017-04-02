package cz.csas.demo.places;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.GridBasedAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.Pagination;
import cz.csas.demo.App;
import cz.csas.demo.Constants;
import cz.csas.demo.R;
import cz.csas.places.Place;
import cz.csas.places.PlaceAroundListParameters;
import cz.csas.places.Places;
import cz.csas.places.Type;
import cz.csas.places.places.PlaceAroundListResponse;


/**
 * The type Places activity.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 27 /04/16.
 */
public class PlacesActivity extends AppCompatActivity implements FragmentCallback,
        OnMapReadyCallback,
        GoogleMap.OnCameraChangeListener,
        ClusterManager.OnClusterItemClickListener<PlaceClusterItem>,
        ClusterManager.OnClusterClickListener {


    private final String PLACE_LIST_FRAGMENT = "place_list_fragment";
    private final String PLACE_AUTOCOMPLETE_FRAGMENT = "place_autocomplete_fragment";
    private final String PLACE_MAP_FRAGMENT = "place_map_fragment";
    private final String PLACE_SETTINGS_FRAGMENT = "place_settings_fragment";
    private final String PLACE_DETAIL_FRAGMENT = "place_detail_fragment";
    private final float GOOGLE_MAP_ZOOM_THRESHOLD_SHOW = 13;

    private MapFragment mMapFragment;
    private Fragment mPreviousFragment;
    private PlacesManager mPlacesManager;
    private GoogleMap mMap;
    private ClusterManager<PlaceClusterItem> mClusterManager;
    private PlaceClusterRenderer mPlaceClusterRenderer;


    /**
     * The Fam places.
     */
    @Bind(R.id.fam_places_activity)
    FloatingActionMenu famPlaces;

    /**
     * The Fab places list.
     */
    @Bind(R.id.fab_places_list)
    FloatingActionButton fabPlacesList;

    /**
     * The Fab places settings.
     */
    @Bind(R.id.fab_places_settings)
    FloatingActionButton fabPlacesSettings;

    /**
     * The Fab places autocomplete.
     */
    @Bind(R.id.fab_places_autocomplete)
    FloatingActionButton fabPlacesAutocomplete;

    /**
     * The Rl autocomplete.
     */
    @Bind(R.id.rl_autocomplete)
    RelativeLayout rlAutocomplete;

    /**
     * The Tv autocomplete.
     */
    @Bind(R.id.tv_autocomplete_suggestion)
    TextView tvAutocomplete;

    /**
     * The Iv autocomplete.
     */
    @Bind(R.id.iv_autocomplete_cancel)
    ImageView ivAutocomplete;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        ButterKnife.bind(this);
        mPlacesManager = App.getPlaceManager();
        changeFragmentToMap();
        fabPlacesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragmentToList();
            }
        });
        fabPlacesAutocomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragmentToAutocomplete();
            }
        });
        fabPlacesSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragmentToSettings();
            }
        });
        ivAutocomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlAutocomplete.setVisibility(View.INVISIBLE);
                App.getPlaceManager().setAutocompletePlace(null);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void changeFragmentToDetail(String id, String type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ID_EXTRA, id);
        bundle.putString(Constants.TYPE_EXTRA, type);
        PlaceDetailFragment fragment = new PlaceDetailFragment();
        fragment.setArguments(bundle);
        changeFragment(fragment, PLACE_DETAIL_FRAGMENT);
    }

    @Override
    public void changeFragmentToList() {
        PlaceListFragment listFragment = new PlaceListFragment();
        changeFragment(listFragment, PLACE_LIST_FRAGMENT);
    }

    @Override
    public void changeFragmentToAutocomplete() {
        PlaceAutocompleteFragment searchFragment = new PlaceAutocompleteFragment();
        changeFragment(searchFragment, PLACE_AUTOCOMPLETE_FRAGMENT);
    }

    @Override
    public void changeFragmentToMap() {
        if (mMapFragment == null) {
            mMapFragment = new MapFragment();
        }
        changeFragment(mMapFragment, PLACE_MAP_FRAGMENT);
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void changeFragmentToSettings() {
        PlaceSettingsFragment settingsFragment = new PlaceSettingsFragment();
        changeFragment(settingsFragment, PLACE_SETTINGS_FRAGMENT);
    }

    private void changeFragment(Fragment fragment, String tag) {
        if (tag.equals(PLACE_MAP_FRAGMENT)) {
            famPlaces.close(false);
            famPlaces.setVisibility(View.VISIBLE);
        } else {
            rlAutocomplete.setVisibility(View.INVISIBLE);
            famPlaces.setVisibility(View.INVISIBLE);
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        mPreviousFragment = getFragmentManager().findFragmentById(R.id.fragment_container_places_activity);
        transaction.replace(R.id.fragment_container_places_activity, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        mPlacesManager.clearPlaces();
        mClusterManager = new ClusterManager<>(this, mMap);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnCameraChangeListener(this);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mPlaceClusterRenderer = new PlaceClusterRenderer(this, mMap, mClusterManager);
        mPlaceClusterRenderer.setMaxZoom(mMap.getMaxZoomLevel());
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setAlgorithm(new GridBasedAlgorithm<PlaceClusterItem>());
        mClusterManager.setRenderer(mPlaceClusterRenderer);
        if (App.getPlaceManager().hasAutocompletePlace()) {
            Place place = App.getPlaceManager().getAutocompletePlace();
            if (place.getAddress() == null) {
                tvAutocomplete.setText(R.string.place_not_found);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(App.getLocationManager().getLocationLatLng(), GOOGLE_MAP_ZOOM_THRESHOLD_SHOW + 0.5f));
            } else {
                tvAutocomplete.setText(place.getAddress());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLocation().getLat(), place.getLocation().getLng()), GOOGLE_MAP_ZOOM_THRESHOLD_SHOW + 0.5f));
            }
            rlAutocomplete.setVisibility(View.VISIBLE);
        } else
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(App.getLocationManager().getLocationLatLng(), GOOGLE_MAP_ZOOM_THRESHOLD_SHOW + 0.5f));

    }

    @Override
    public void onCameraChange(final CameraPosition cameraPosition) {
        mClusterManager.onCameraChange(cameraPosition);
        if (cameraPosition.zoom > GOOGLE_MAP_ZOOM_THRESHOLD_SHOW) {
            mPlaceClusterRenderer.setZoom(cameraPosition.zoom);
            LatLng center = cameraPosition.target;

            PlaceAroundListParameters parameters = new PlaceAroundListParameters.Builder()
                    .setPagination(new Pagination(0, 70))
                    .setTypes(!App.getPlaceManager().isAtmEnabled() ? Arrays.asList(Type.BRANCH) : !App.getPlaceManager().isBranchEnabled() ? Arrays.asList(Type.ATM) : null)
                    .create();
            Places.getInstance().getPlacesClient().getPlacesResource().around(center.latitude, center.longitude, mPlacesManager.getRadius()).list(parameters, new CallbackWebApi<PlaceAroundListResponse>() {
                @Override
                public void success(PlaceAroundListResponse placeAroundListResponse) {
                    mPlacesManager.setPlaces(placeAroundListResponse.getPlaces(), mMap.getProjection().getVisibleRegion().latLngBounds);
                    addItems();
                }

                @Override
                public void failure(CsSDKError error) {

                }
            });
        } else
            mClusterManager.clearItems();

    }

    private void addItems() {
        List<Place> itemsToAdd = mPlacesManager.getPlacesToAdd();
        List<PlaceClusterItem> itemsToRemove = castToClusterItem(mPlacesManager.getPlacesToRemove());
        for (PlaceClusterItem item : itemsToRemove) {
            mClusterManager.removeItem(item);
        }
        mClusterManager.addItems(castToClusterItem(itemsToAdd));
        mClusterManager.cluster();
    }

    private List<PlaceClusterItem> castToClusterItem(List<Place> list) {
        List<PlaceClusterItem> items = new ArrayList<>();
        for (Place place : list) {
            items.add(new PlaceClusterItem(place));
        }
        return items;
    }

    @Override
    public void onBackPressed() {
        if (mPreviousFragment != null && mPreviousFragment.getTag().equals(PLACE_LIST_FRAGMENT)
                && getFragmentManager().findFragmentById(R.id.fragment_container_places_activity).getTag().equals(PLACE_DETAIL_FRAGMENT))
            changeFragmentToList();
        else if (mMapFragment == null || !mMapFragment.isVisible())
            changeFragmentToMap();
        else
            finish();
    }

    @Override
    public boolean onClusterClick(Cluster cluster) {
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(cluster.getPosition()).zoom(mMap.getCameraPosition().zoom + .5f).build()), 400, null);
        return true;
    }

    @Override
    public boolean onClusterItemClick(PlaceClusterItem placeClusterItem) {
        changeFragmentToDetail(String.valueOf(placeClusterItem.getPlace().getId()), placeClusterItem.getPlace().getType().getValue());
        return true;
    }

}
