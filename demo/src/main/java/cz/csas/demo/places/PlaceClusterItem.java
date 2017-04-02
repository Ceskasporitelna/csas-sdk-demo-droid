package cz.csas.demo.places;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import cz.csas.places.Place;

/**
 * The type Place cluster item.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 29 /04/16.
 */
public class PlaceClusterItem implements ClusterItem {

    private Place place;

    /**
     * Instantiates a new Place cluster item.
     *
     * @param place the place
     */
    public PlaceClusterItem(Place place) {
        this.place = place;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(place.getLocation().getLat(), place.getLocation().getLng());
    }

    /**
     * Gets place.
     *
     * @return the place
     */
    public Place getPlace() {
        return place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaceClusterItem)) return false;

        PlaceClusterItem that = (PlaceClusterItem) o;

        return !(place != null ? !place.equals(that.place) : that.place != null);

    }

    @Override
    public int hashCode() {
        return place != null ? place.hashCode() : 0;
    }
}
