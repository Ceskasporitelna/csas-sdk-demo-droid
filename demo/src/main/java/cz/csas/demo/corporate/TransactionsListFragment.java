package cz.csas.demo.corporate;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.thuongnh.zprogresshud.ZProgressHUD;
import cz.csas.corporate.Corporate;
import cz.csas.corporate.accounts.Transaction;
import cz.csas.corporate.accounts.TransactionsListResponse;
import cz.csas.corporate.accounts.TransactionsParameters;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.utils.TimeUtils;
import cz.csas.cscore.webapi.Pagination;
import cz.csas.demo.Constants;
import cz.csas.demo.R;
import cz.csas.demo.components.CorporateTransactionsAdapter;

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
    private CorporateTransactionsAdapter mTransactionsAdapter;
    private FragmentCallback mFragmentCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentCallback = (FragmentCallback) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.corporate_transactions_list_fragment, container, false);
        ButterKnife.bind(this, mRootView);

        mData = new ArrayList<>();
        mTransactionsAdapter = new CorporateTransactionsAdapter(mData, getActivity());
        lvTransactions.setAdapter(mTransactionsAdapter);

        mProgress = ZProgressHUD.getInstance(getActivity());

        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();
        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.get(Constants.ACCOUNT_ID_EXTRA) != null) {

            Calendar calendar = Calendar.getInstance();
            String dateEnd = TimeUtils.getISO8601String(calendar.getTime());
            calendar.add(Calendar.MONTH, -6);
            String dateStart = TimeUtils.getISO8601String(calendar.getTime());

            TransactionsParameters parameters = new TransactionsParameters(new Pagination(0, 70), dateStart, dateEnd);
            Corporate.getInstance().getCorporateClient().getAccountsResource().withId(bundle.get(Constants.ACCOUNT_ID_EXTRA)).getTransactionsResource().list(parameters, new CallbackWebApi<TransactionsListResponse>() {
                @Override
                public void success(TransactionsListResponse transactionsListResponse) {
                    mData = transactionsListResponse.getTransactions();
                    mTransactionsAdapter.changeData(mData);
                    mProgress.dismiss();
                }

                @Override
                public void failure(CsSDKError error) {
                    mProgress.dismissWithFailure();
                }
            });
        } else {
            mProgress.dismissWithFailure();
        }

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
