package cz.csas.demo.utils;


import cz.csas.places.Location;

/**
 * The type Location utils.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 02 /05/16.
 */
public class LocationUtils {

    /**
     * Gets distance.
     *
     * @param from the from
     * @param to   the to
     * @return the distance
     */
    public static float getDistance(Location from, Location to) {
        android.location.Location locationA = new android.location.Location("Point A");
        locationA.setLatitude(from.getLat());
        locationA.setLongitude(from.getLng());
        android.location.Location locationB = new android.location.Location("Point B");
        locationA.setLatitude(to.getLat());
        locationA.setLongitude(to.getLng());
        return getDistance(locationA, locationB);
    }

    /**
     * Gets distance.
     *
     * @param from the from
     * @param to   the to
     * @return the distance
     */
    public static float getDistance(Location from, android.location.Location to) {
        android.location.Location locationA = new android.location.Location("Point A");
        locationA.setLatitude(from.getLat());
        locationA.setLongitude(from.getLng());
        return getDistance(locationA, to);
    }

    /**
     * Gets distance.
     *
     * @param from the from
     * @param to   the to
     * @return the distance
     */
    public static float getDistance(android.location.Location from, android.location.Location to) {
        return from.distanceTo(to);
    }
}
