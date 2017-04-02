package cz.csas.demo.uniforms;

/**
 * The interface Fragment callback.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public interface FragmentCallback {

    /**
     * Change fragment to list.
     */
    public void changeFragmentToList();

    /**
     * Change fragment to desc.
     */
    public void changeFragmentToDesc(long id);
}
