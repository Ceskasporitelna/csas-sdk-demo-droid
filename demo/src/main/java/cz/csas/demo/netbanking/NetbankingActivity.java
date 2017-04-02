package cz.csas.demo.netbanking;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import cz.csas.cscore.CoreSDK;
import cz.csas.cscore.locker.LockerAccessTokenProvider;
import cz.csas.demo.Constants;
import cz.csas.demo.R;
import cz.csas.netbanking.Netbanking;

import static cz.csas.demo.Constants.TYPE_EXTRA;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class NetbankingActivity extends AppCompatActivity implements FragmentCallback {

    private final String NETBANING_MAIN_FRAGMENT_TAG = "netbanking_main_fragment";
    private final String TESTS_LIST_FRAGMENT_TAG = "tests_list_fragment";
    private final String SPECIFIC_TEST_LIST_FRAGMENT_TAG = "specific_test_list_fragment";
    private final String ACCOUNT_LIST_FRAGMENT_TAG = "account_list_fragment";
    private final String ACCOUNT_DESC_FRAGMENT_TAG = "account_desc_fragment";
    private final String PAYMENT_LIST_FRAGMENT_TAG = "payment_list_fragment";
    private final String PAYMENT_DESC_FRAGMENT_TAG = "payment_desc_fragment";
    private final String DOMESTIC_PAYMENT_FRAGMENT_TAG = "domestic_payment_fragment";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netbanking);
        ButterKnife.bind(this);
        Netbanking.getInstance().getNetbankingClient().setAccessTokenProvider(new LockerAccessTokenProvider(CoreSDK.getInstance().getLocker(), CoreSDK.getInstance().getLogger()));
        changeFragmentToMain();
    }


    @Override
    public void changeFragmentToMain() {
        changeFragment(new NetbankingMainFragment(), NETBANING_MAIN_FRAGMENT_TAG);
    }

    @Override
    public void changeFragmentToTestList() {
        changeFragment(new TestListFragment(), TESTS_LIST_FRAGMENT_TAG);
    }

    @Override
    public void changeFragmentToSpecificTestList(String id) {
        SpecificTestListFragment fragment = new SpecificTestListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE_EXTRA, id);
        fragment.setArguments(bundle);
        changeFragment(fragment, SPECIFIC_TEST_LIST_FRAGMENT_TAG);
    }

    @Override
    public void changeFragmentToAccountList() {
        changeFragment(new AccountListFragment(), ACCOUNT_LIST_FRAGMENT_TAG);
    }

    @Override
    public void changeFragmentToAccountDetail(String id) {
        AccountDescFragment accountDescFragment = new AccountDescFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCOUNT_ID_EXTRA, id);
        accountDescFragment.setArguments(bundle);
        changeFragment(accountDescFragment, ACCOUNT_DESC_FRAGMENT_TAG);
    }

    @Override
    public void changeFragmentToPaymentList() {
        changeFragment(new PaymentsListFragment(), PAYMENT_LIST_FRAGMENT_TAG);
    }

    @Override
    public void changeFragmentToPaymentDetail(String id) {
        PaymentDescFragment paymentDescFragment = new PaymentDescFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PAYMENT_ID_EXTRA, id);
        paymentDescFragment.setArguments(bundle);
        changeFragment(paymentDescFragment, PAYMENT_DESC_FRAGMENT_TAG);
    }

    @Override
    public void changeFragmentToDomesticPayment(String id) {
        DomesticPaymentFragment domesticPaymentFragment = new DomesticPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCOUNT_ID_EXTRA, id);
        domesticPaymentFragment.setArguments(bundle);
        changeFragment(domesticPaymentFragment, DOMESTIC_PAYMENT_FRAGMENT_TAG);
    }

    private void changeFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_netbanking_activity, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        NetbankingMainFragment fragment = (NetbankingMainFragment) getFragmentManager().findFragmentByTag(NETBANING_MAIN_FRAGMENT_TAG);

        if (fragment != null && fragment.isVisible())
            finish();
        else
            getFragmentManager().popBackStackImmediate();
    }
}
