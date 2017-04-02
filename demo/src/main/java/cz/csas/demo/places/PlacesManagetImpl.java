package cz.csas.demo.places;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

import cz.csas.places.Place;

/**
 * The type Places managet.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 29 /04/16.
 */
public class PlacesManagetImpl implements PlacesManager {

    private final String PLACES_PREFERENCES = "places_preferences";
    private final String RADIUS_KEY = "radius";
    private final String ATM_KEY = "atm";
    private final String BRANCH_KEY = "branch";
    private Boolean mBranchEnabled;
    private Boolean mAtmEnabled;
    private Integer mRadius;
    private List<Place> mPlaces = new ArrayList<>();
    private List<Place> mPlacesToAdd = new ArrayList<>();
    private List<Place> mPlacesToRemove = new ArrayList<>();
    private SharedPreferences mPlacesSharedPreferences;
    private SharedPreferences.Editor mPlacesEditor;
    private Place mAutocompletePlace;

    /**
     * Instantiates a new Places managet.
     *
     * @param context the context
     */
    public PlacesManagetImpl(Context context) {
        mPlacesSharedPreferences = context.getSharedPreferences(PLACES_PREFERENCES, Context.MODE_PRIVATE);
        mPlacesEditor = mPlacesSharedPreferences.edit();
    }

    @Override
    public void setRadius(int radius) {
        this.mRadius = radius;
        mPlacesEditor.putInt(RADIUS_KEY, radius);
        mPlacesEditor.commit();
    }

    @Override
    public int getRadius() {
        if (mRadius == null)
            mRadius = mPlacesSharedPreferences.getInt(RADIUS_KEY, 2000);
        return mRadius;
    }

    @Override
    public void setPlaces(List<Place> places, LatLngBounds bounds) {
        mPlacesToAdd.clear();
        mPlacesToRemove.clear();
        for (int i = 0; i < places.size(); i++) {
            Place place = places.get(i);
            LatLng location = new LatLng(place.getLocation().getLat(), place.getLocation().getLng());
            if (!mPlaces.contains(place) && bounds.contains(location)) {
                mPlaces.add(place);
                mPlacesToAdd.add(place);
            }
        }
        for (int i = 0; i < mPlaces.size(); i++) {
            Place place = mPlaces.get(i);
            LatLng location = new LatLng(place.getLocation().getLat(), place.getLocation().getLng());
            if (!places.contains(place) || !bounds.contains(location)) {
                mPlacesToRemove.add(place);
                mPlaces.remove(place);
            }
        }
    }

    @Override
    public List<Place> getPlaces() {
        return mPlaces;
    }

    @Override
    public void clearPlaces() {
        mPlaces.clear();
    }

    @Override
    public List<Place> getPlacesToAdd() {
        return mPlacesToAdd;
    }

    @Override
    public List<Place> getPlacesToRemove() {
        return mPlacesToRemove;
    }

    @Override
    public boolean isBranchEnabled() {
        if (mBranchEnabled == null)
            mBranchEnabled = mPlacesSharedPreferences.getBoolean(BRANCH_KEY, true);
        return mBranchEnabled;
    }

    @Override
    public boolean isAtmEnabled() {
        if (mAtmEnabled == null)
            mAtmEnabled = mPlacesSharedPreferences.getBoolean(ATM_KEY, true);
        return mAtmEnabled;
    }

    @Override
    public void setBranchEnabled(boolean enabled) {
        mBranchEnabled = enabled;
        mPlacesEditor.putBoolean(BRANCH_KEY, enabled);
        mPlacesEditor.commit();
    }

    @Override
    public void setAtmEnabled(boolean enabled) {
        mAtmEnabled = enabled;
        mPlacesEditor.putBoolean(ATM_KEY, enabled);
        mPlacesEditor.commit();
    }

    @Override
    public void setAutocompletePlace(Place place) {
        mAutocompletePlace = place;
    }

    @Override
    public Place getAutocompletePlace() {
        return mAutocompletePlace;
    }

    @Override
    public boolean hasAutocompletePlace() {
        return mAutocompletePlace != null;
    }


}
