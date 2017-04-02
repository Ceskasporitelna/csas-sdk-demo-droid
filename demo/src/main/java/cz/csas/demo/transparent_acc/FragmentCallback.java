package cz.csas.demo.transparent_acc;

/**
 * The interface Fragment callback.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public interface FragmentCallback {

    /**
     * Change fragment to accounts list.
     */
    public void changeFragmentToAccountsList();

    /**
     * Change fragment to transactions list.
     *
     * @param id the id
     */
    public void changeFragmentToTransactionsList(String id);

    /**
     * Change fragment to account.
     *
     * @param id the id
     */
    public void changeFragmentToAccount(String id);

    /**
     * Change fragment to transaction.
     */
    public void changeFragmentToTransaction();
}
