package cz.csas.demo.places;

import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

import cz.csas.places.Place;

/**
 * The interface Places manager.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 29 /04/16.
 */
public interface PlacesManager {

    /**
     * Sets radius.
     *
     * @param radius the radius
     */
    public void setRadius(int radius);

    /**
     * Gets radius.
     *
     * @return the radius
     */
    public int getRadius();

    /**
     * Sets places.
     *
     * @param places the places
     * @param bounds the bounds
     */
    public void setPlaces(List<Place> places, LatLngBounds bounds);

    /**
     * Gets places.
     *
     * @return the places
     */
    public List<Place> getPlaces();

    /**
     * Clear places.
     */
    public void clearPlaces();

    /**
     * Gets places to add.
     *
     * @return the places to add
     */
    public List<Place> getPlacesToAdd();

    /**
     * Gets places to remove.
     *
     * @return the places to remove
     */
    public List<Place> getPlacesToRemove();

    /**
     * Is branch enabled boolean.
     *
     * @return the boolean
     */
    public boolean isBranchEnabled();

    /**
     * Is atm enabled boolean.
     *
     * @return the boolean
     */
    public boolean isAtmEnabled();

    /**
     * Sets branch enabled.
     *
     * @param enabled the enabled
     */
    public void setBranchEnabled(boolean enabled);

    /**
     * Sets atm enabled.
     *
     * @param enabled the enabled
     */
    public void setAtmEnabled(boolean enabled);

    /**
     * Sets autocomplete place.
     *
     * @param place the place
     */
    public void setAutocompletePlace(Place place);

    /**
     * Gets autocomplete place.
     *
     * @return the autocomplete place
     */
    public Place getAutocompletePlace();

    /**
     * Has autocomplete place boolean.
     *
     * @return the boolean
     */
    public boolean hasAutocompletePlace();
}
