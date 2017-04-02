package cz.csas.demo.places;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

/**
 * The type Location tracker fallback.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 27/04/16.
 */
public class LocationTrackerFallback implements LocationTracker, LocationTracker.LocationUpdateListener {

    private boolean isRunning;

    private LocationTrackerImpl gpsTracker;
    private LocationTrackerImpl networkTracker;
    private final LatLng DEFAULT_LOCATION = new LatLng(50.0878f, 14.4205f);

    private LocationUpdateListener locationUpdateListener;

    /**
     * The Last location.
     */
    Location lastLocation;
    /**
     * The Last time.
     */
    long lastTime;

    /**
     * Instantiates a new Location tracker fallback.
     *
     * @param context the context
     */
    public LocationTrackerFallback(Context context) {
        gpsTracker = new LocationTrackerImpl(context, LocationManager.GPS_PROVIDER);
        networkTracker = new LocationTrackerImpl(context, LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void start() {
        if (isRunning) {
            return;
        }
        gpsTracker.start(this);
        networkTracker.start(this);
        isRunning = true;
    }

    @Override
    public void start(LocationUpdateListener update) {
        start();
        locationUpdateListener = update;
    }

    @Override
    public void stop() {
        if (isRunning) {
            gpsTracker.stop();
            networkTracker.stop();
            isRunning = false;
            locationUpdateListener = null;
        }
    }

    @Override
    public boolean hasLocation() {
        //If either has a location, use it
        return gpsTracker.hasLocation() || networkTracker.hasLocation();
    }

    @Override
    public boolean hasPossiblyStaleLocation() {
        //If either has a location, use it
        return gpsTracker.hasPossiblyStaleLocation() || networkTracker.hasPossiblyStaleLocation();
    }

    @Override
    public Location getLocation() {
        Location location = gpsTracker.getLocation();
        if (location == null) {
            location = networkTracker.getLocation();
        }
        return location;
    }

    @Override
    public LatLng getLocationLatLng() {
        Location aktLocation = getLocation();
        if (aktLocation == null) {
            return DEFAULT_LOCATION;
        }
        return new LatLng(aktLocation.getLatitude(), aktLocation.getLongitude());
    }

    @Override
    public Location getPossiblyStaleLocation() {
        Location location = gpsTracker.getPossiblyStaleLocation();
        if (location == null) {
            location = networkTracker.getPossiblyStaleLocation();
        }
        return location;
    }

    @Override
    public void onUpdate(Location oldLocation, long oldTime, Location newLocation, long newTime) {
        boolean update = false;

        //We should update only if there is no last location, the provider is the same, or the provider is more accurate, or the old location is stale
        if (lastLocation == null) {
            update = true;
        } else if (lastLocation != null && lastLocation.getProvider().equals(newLocation.getProvider())) {
            update = true;
        } else if (newLocation.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            update = true;
        } else if (newTime - lastTime > 5 * 60 * 1000) {
            update = true;
        }

        if (update) {
            if (locationUpdateListener != null) {
                locationUpdateListener.onUpdate(lastLocation, lastTime, newLocation, newTime);
            }
            lastLocation = newLocation;
            lastTime = newTime;
        }
    }
}
