package cz.csas.demo.transparent_acc;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import cz.csas.demo.Constants;
import cz.csas.demo.R;
import cz.csas.transparent_acc.Transaction;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 25/05/16.
 */
public class TransparentAccActivity extends AppCompatActivity implements FragmentCallback {

    private final String ACCOUNTS_LIST_FRAGMENT_TAG = "accounts_list_fragment";
    private final String ACCOUNT_DESC_FRAGMENT_TAG = "account_desc_fragment";
    private final String TRANSACTIONS_LIST_FRAGMENT_TAG = "transactions_list_fragment";
    private final String TRANSACTION_DESC_FRAGMENT_TAG = "transaction_desc_fragment";
    private Transaction mTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent_acc);
        ButterKnife.bind(this);
        changeFragmentToAccountsList();
    }

    @Override
    public void changeFragmentToAccountsList() {
        changeFragment(new TransparentAccountsListFragment(), ACCOUNTS_LIST_FRAGMENT_TAG);
    }

    @Override
    public void changeFragmentToTransactionsList(String id) {
        TransactionsListFragment transactionsListFragment = new TransactionsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCOUNT_ID_EXTRA, id);
        transactionsListFragment.setArguments(bundle);
        changeFragment(transactionsListFragment, TRANSACTIONS_LIST_FRAGMENT_TAG);
    }

    @Override
    public void changeFragmentToAccount(String id) {
        TransparentAccountDescFragment transparentAccountDescFragment = new TransparentAccountDescFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCOUNT_ID_EXTRA, id);
        transparentAccountDescFragment.setArguments(bundle);
        changeFragment(transparentAccountDescFragment, ACCOUNT_DESC_FRAGMENT_TAG);
    }

    @Override
    public void changeFragmentToTransaction() {
        changeFragment(new TransactionDescFragment(), TRANSACTION_DESC_FRAGMENT_TAG);
    }

    private void changeFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_transparent_acc_activity, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        TransparentAccountsListFragment transparentAccountsListFragment = (TransparentAccountsListFragment) getFragmentManager().findFragmentByTag(ACCOUNTS_LIST_FRAGMENT_TAG);
        if (transparentAccountsListFragment != null && transparentAccountsListFragment.isVisible())
            finish();
        else
            getFragmentManager().popBackStackImmediate();
    }

    protected void setTransaction(Transaction transaction) {
        this.mTransaction = transaction;
    }

    protected Transaction getTransaction() {
        return mTransaction;
    }
}
