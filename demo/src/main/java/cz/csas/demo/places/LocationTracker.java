package cz.csas.demo.places;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by hauseja3 on 05/05/15.
 */
public interface LocationTracker {
    /**
     * The interface Location update listener.
     */
    public interface LocationUpdateListener {
        /**
         * On update.
         *
         * @param oldLocation the old location
         * @param oldTime     the old time
         * @param newLocation the new location
         * @param newTime     the new time
         */
        public void onUpdate(Location oldLocation, long oldTime, Location newLocation, long newTime);
    }

    /**
     * Start.
     */
    public void start();

    /**
     * Start.
     *
     * @param update the update
     */
    public void start(LocationUpdateListener update);

    /**
     * Stop.
     */
    public void stop();

    /**
     * Has location boolean.
     *
     * @return the boolean
     */
    public boolean hasLocation();

    /**
     * Has possibly stale location boolean.
     *
     * @return the boolean
     */
    public boolean hasPossiblyStaleLocation();

    /**
     * Gets location.
     *
     * @return the location
     */
    public Location getLocation();

    /**
     * Gets location lat lng.
     *
     * @return the location lat lng
     */
    public LatLng getLocationLatLng();

    /**
     * Gets possibly stale location.
     *
     * @return the possibly stale location
     */
    public Location getPossiblyStaleLocation();

}
