package cz.csas.demo.places;

/**
 * The interface Fragment callback.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 27 /04/16.
 */
public interface FragmentCallback {

    /**
     * Change fragment to detail.
     *
     * @param id the id
     */
    public void changeFragmentToDetail(String id, String type);

    /**
     * Change fragment to list.
     */
    public void changeFragmentToList();

    /**
     * Change fragment to autocomplete.
     */
    public void changeFragmentToAutocomplete();

    /**
     * Change fragment to map.
     */
    public void changeFragmentToMap();

    /**
     * Change fragment to settings.
     */
    public void changeFragmentToSettings();
}
