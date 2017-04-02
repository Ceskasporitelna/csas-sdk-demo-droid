package cz.csas.demo.transparent_acc;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.PaginatedParameters;
import cz.csas.cscore.webapi.Pagination;
import cz.csas.demo.Constants;
import cz.csas.demo.R;
import cz.csas.demo.components.TransactionsAdapter;
import cz.csas.transparent_acc.Transaction;
import cz.csas.transparent_acc.TransactionsListResponse;
import cz.csas.transparent_acc.TransparentAccounts;

/**
 * The type Form list fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class TransactionsListFragment extends Fragment {

    @Bind(R.id.lv_transactions)
    ListView lvTransactions;

    private View mRootView;
    private ZProgressHUD mProgress;
    private List<Transaction> mData;
    private TransactionsAdapter mTransactionsAdapter;
    private cz.csas.demo.transparent_acc.FragmentCallback mFragmentCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentCallback = (cz.csas.demo.transparent_acc.FragmentCallback) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.transactions_list_fragment, container, false);
        ButterKnife.bind(this, mRootView);

        mData = new ArrayList<>();
        mTransactionsAdapter = new TransactionsAdapter(mData, getActivity());
        lvTransactions.setAdapter(mTransactionsAdapter);

        mProgress = ZProgressHUD.getInstance(getActivity());

        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();
        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.get(Constants.ACCOUNT_ID_EXTRA) != null) {

            TransparentAccounts.getInstance().getTransparentAccountsClient().getTransparentAccountsResource().withId(bundle.get(Constants.ACCOUNT_ID_EXTRA)).getTransactionsResource().list(new PaginatedParameters(new Pagination(0, 70)), new CallbackWebApi<TransactionsListResponse>() {
                @Override
                public void success(TransactionsListResponse transactionsListResponse) {
                    mData = transactionsListResponse.getTransactions();
                    mTransactionsAdapter.changeData(mData);
                    mProgress.dismiss();

                }

                @Override
                public void failure(CsSDKError error) {
                    mProgress.dismissWithFailure();
                    mFragmentCallback.changeFragmentToAccountsList();
                }
            });
        } else {
            mProgress.dismissWithFailure();
            mFragmentCallback.changeFragmentToAccountsList();
        }

        lvTransactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((TransparentAccActivity) getActivity()).setTransaction((Transaction) mTransactionsAdapter.getItem(position));
                mFragmentCallback.changeFragmentToTransaction();
            }
        });

        return mRootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mProgress != null && mProgress.isShowing())
            mProgress.dismiss();
        mProgress = null;
    }
}
