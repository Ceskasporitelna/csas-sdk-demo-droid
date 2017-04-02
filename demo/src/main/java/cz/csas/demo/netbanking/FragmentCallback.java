package cz.csas.demo.netbanking;

/**
 * The interface Fragment callback.
 */
public interface FragmentCallback {

    /**
     * Change fragment to main.
     */
    public void changeFragmentToMain();

    /**
     * Change fragment to test list.
     */
    public void changeFragmentToTestList();

    /**
     * Change fragment to specific test list.
     *
     * @param id the id
     */
    public void changeFragmentToSpecificTestList(String id);

    /**
     * Change fragment to account list.
     */
    public void changeFragmentToAccountList();

    /**
     * Change fragment to account detail.
     *
     * @param id the id
     */
    public void changeFragmentToAccountDetail(String id);

    /**
     * Change fragment to payment list.
     */
    public void changeFragmentToPaymentList();

    /**
     * Change fragment to payment detail.
     *
     * @param id the id
     */
    public void changeFragmentToPaymentDetail(String id);

    /**
     * Change fragment to domestic payment.
     *
     * @param id the id
     */
    public void changeFragmentToDomesticPayment(String id);

}
