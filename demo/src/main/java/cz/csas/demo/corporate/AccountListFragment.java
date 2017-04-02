package cz.csas.demo.corporate;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.thuongnh.zprogresshud.ZProgressHUD;
import cz.csas.corporate.Corporate;
import cz.csas.corporate.accounts.Account;
import cz.csas.corporate.accounts.AccountsListResponse;
import cz.csas.corporate.accounts.AccountsParameters;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.Pagination;
import cz.csas.demo.R;
import cz.csas.demo.components.CorporateAccountsAdapter;

/**
 * The type Form list fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class AccountListFragment extends Fragment {

    @Bind(R.id.srl_account_list)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.lv_accounts)
    ListView lvAccount;

    private View mRootView;
    private FragmentCallback mFragmentCallback;
    private ZProgressHUD mProgress;
    private List<Account> mAccounts;
    private CorporateAccountsAdapter mAccountsAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentCallback = (FragmentCallback) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.corporate_account_list_fragment, container, false);
        ButterKnife.bind(this, mRootView);

        mAccounts = new ArrayList<>();
        mAccountsAdapter = new CorporateAccountsAdapter(mAccounts, getActivity());
        lvAccount.setAdapter(mAccountsAdapter);

        mProgress = ZProgressHUD.getInstance(getActivity());
        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();
        getAccounts();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAccounts();
            }
        });

        lvAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Account mainAccount = (Account) mAccountsAdapter.getItem(position);
                if (mainAccount != null)
                    mFragmentCallback.changeFragmentToAccountDetail(mainAccount.getId());
            }
        });
        return mRootView;
    }

    private void getAccounts() {
        AccountsParameters parameters = new AccountsParameters(new Pagination(0, 70));
        Corporate.getInstance().getCorporateClient().getAccountsResource().list(parameters, new CallbackWebApi<cz.csas.corporate.accounts.AccountsListResponse>() {
            @Override
            public void success(AccountsListResponse accountsListResponse) {
                if (accountsListResponse.getAccounts() != null) {
                    mAccounts = accountsListResponse.getAccounts();
                    mAccountsAdapter.changeData(mAccounts);
                }
                if (mProgress != null && mProgress.isShowing())
                    mProgress.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(CsSDKError error) {
                if (mProgress != null && mProgress.isShowing())
                    mProgress.dismissWithFailure();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mProgress != null && mProgress.isShowing())
            mProgress.dismiss();
        mProgress = null;
    }
}
