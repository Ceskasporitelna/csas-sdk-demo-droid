package cz.csas.demo.places;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by hauseja3 on 05/05/15.
 */
public class LocationTrackerImpl implements LocationTracker, LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_UPDATE_DISTANCE = 10;
    // The minimum time between updates in milliseconds
    private static final long MIN_UPDATE_TIME = 1000 * 60;

    private Context context;
    private LocationManager locationManager;
    private String provider;
    private Location lastLocation;
    private long lastTime;
    private boolean isRunning;
    private LocationUpdateListener locationUpdateListener;

    /**
     * Instantiates a new Location tracker.
     *
     * @param context      the context
     * @param providerType the provider type
     */
    public LocationTrackerImpl(Context context, String providerType) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        provider = providerType;
        this.context = context;
    }

    @Override
    public void start() {
        if (isRunning) {
            return;
        }

        isRunning = true;
        if (locationManager.getAllProviders().contains(provider) &&
                locationManager.isProviderEnabled(provider) &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            locationManager.requestLocationUpdates(provider, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, this);

        lastLocation = null;
        lastTime = 0;
        return;
    }

    @Override
    public void start(LocationUpdateListener update) {
        start();
        locationUpdateListener = update;
    }

    @Override
    public void stop() {
        if (isRunning &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
            isRunning = false;
            locationUpdateListener = null;
        }
    }

    @Override
    public boolean hasLocation() {
        if (lastLocation == null) {
            return false;
        }
        if (System.currentTimeMillis() - lastTime > 60 * MIN_UPDATE_TIME) {
            return false; // iff older than one hour => stale for our purpose
        }
        return true;
    }

    @Override
    public boolean hasPossiblyStaleLocation() {
        if (lastLocation != null) {
            return true;
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return locationManager.getLastKnownLocation(provider) != null;
        return false;
    }

    @Override
    public Location getLocation() {
        if (lastLocation == null) {
            return null;
        }
        if (System.currentTimeMillis() - lastTime > 60 * MIN_UPDATE_TIME) {
            return null; // iff older than one hour => stale for our purpose
        }
        return lastLocation;
    }

    @Override
    public LatLng getLocationLatLng() {
        Location aktLocation = getLocation();
        if (aktLocation == null) {
            return null;
        }
        return new LatLng(aktLocation.getLatitude(), aktLocation.getLongitude());
    }

    @Override
    public Location getPossiblyStaleLocation() {
        if (lastLocation != null) {
            return lastLocation;
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return locationManager.getLastKnownLocation(provider);
        return null;
    }

    @Override
    public void onLocationChanged(Location newLocation) {
        long now = System.currentTimeMillis();
        if (locationUpdateListener != null) {
            locationUpdateListener.onUpdate(lastLocation, lastTime, newLocation, now);
        }
        lastLocation = newLocation;
        lastTime = now;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
