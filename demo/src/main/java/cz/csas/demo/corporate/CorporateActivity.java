package cz.csas.demo.corporate;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import cz.csas.corporate.Corporate;
import cz.csas.cscore.CoreSDK;
import cz.csas.cscore.locker.LockerAccessTokenProvider;
import cz.csas.demo.R;

import static cz.csas.demo.Constants.ACCOUNT_ID_EXTRA;
import static cz.csas.demo.Constants.COMPANY_ID_EXTRA;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class CorporateActivity extends AppCompatActivity implements FragmentCallback {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporates);
        ButterKnife.bind(this);
        Corporate.getInstance().getCorporateClient().setAccessTokenProvider(new LockerAccessTokenProvider(CoreSDK.getInstance().getLocker(), CoreSDK.getInstance().getLogger()));
        changeFragmentToMain();
    }


    @Override
    public void changeFragmentToMain() {
        changeFragment(new CorporateMainFragment(), CorporateMainFragment.class.getSimpleName());
    }

    @Override
    public void changeFragmentToAccountList() {
        changeFragment(new AccountListFragment(), AccountListFragment.class.getSimpleName());
    }

    @Override
    public void changeFragmentToAccountDetail(String id) {
        AccountDescFragment fragment = new AccountDescFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ACCOUNT_ID_EXTRA, id);
        fragment.setArguments(bundle);
        changeFragment(fragment, AccountDescFragment.class.getSimpleName());
    }

    @Override
    public void changeFragmentToCompaniesList() {
        changeFragment(new CompaniesListFragment(), CompaniesListFragment.class.getSimpleName());
    }

    @Override
    public void changeFragmentToCompanyDetail(String id) {
        CompanyDescFragment fragment = new CompanyDescFragment();
        Bundle bundle = new Bundle();
        bundle.putString(COMPANY_ID_EXTRA, id);
        fragment.setArguments(bundle);
        changeFragment(fragment, CompanyDescFragment.class.getSimpleName());
    }

    @Override
    public void changeFragmentToTransactionsList(String id) {
        TransactionsListFragment fragment = new TransactionsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ACCOUNT_ID_EXTRA, id);
        fragment.setArguments(bundle);
        changeFragment(fragment, TransactionsListFragment.class.getSimpleName());
    }

    @Override
    public void changeFragmentToCompanyCampaigns(String id) {
        CampaignsListFragment fragment = new CampaignsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(COMPANY_ID_EXTRA, id);
        fragment.setArguments(bundle);
        changeFragment(fragment, CampaignsListFragment.class.getSimpleName());
    }

    @Override
    public void changeFragmentToCompanyRelationshipManagers(String id) {
        RelationshipManagersListFragment fragment = new RelationshipManagersListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(COMPANY_ID_EXTRA, id);
        fragment.setArguments(bundle);
        changeFragment(fragment, RelationshipManagersListFragment.class.getSimpleName());
    }

    private void changeFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_netbanking_activity, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        CorporateMainFragment fragment = (CorporateMainFragment) getFragmentManager().findFragmentByTag(CorporateMainFragment.class.getSimpleName());

        if (fragment != null && fragment.isVisible())
            finish();
        else
            getFragmentManager().popBackStackImmediate();
    }
}
