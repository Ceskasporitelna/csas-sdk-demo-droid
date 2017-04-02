package cz.csas.demo.components;

/**
 * The interface Fragment callback.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public interface FragmentUniformsCallback {

    /**
     * Change fragment to list.
     */
    public void changeFragmentToList();

    /**
     * Change fragment to desc.
     */
    public void changeFragmentToDesc(long id);
}
