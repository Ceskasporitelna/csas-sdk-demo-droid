package cz.csas.demo.corporate;

/**
 * The interface Fragment callback.
 */
public interface FragmentCallback {

    /**
     * Change fragment to main.
     */
    public void changeFragmentToMain();

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
     * Change fragment to companies list.
     */
    public void changeFragmentToCompaniesList();

    /**
     * Change fragment to company detail.
     */
    public void changeFragmentToCompanyDetail(String id);

    /**
     * Change fragment to transactions list.
     */
    public void changeFragmentToTransactionsList(String id);

    /**
     * Change fragment to campaigns list.
     */
    public void changeFragmentToCompanyCampaigns(String id);

    /**
     * Change fragment to relationship managers list.
     */
    public void changeFragmentToCompanyRelationshipManagers(String id);

}
