package cz.csas.demo.corporate;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.thuongnh.zprogresshud.ZProgressHUD;
import cz.csas.corporate.Amount;
import cz.csas.corporate.Corporate;
import cz.csas.corporate.accounts.AccountBalance;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.demo.Constants;
import cz.csas.demo.R;
import cz.csas.demo.components.DetailItem;


/**
 * The type Account desc fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /03/17.
 */
public class AccountDescFragment extends Fragment {

    @Bind(R.id.di_balance)
    DetailItem mDiBalance;

    @Bind(R.id.di_disposable)
    DetailItem mDiDisposable;

    @Bind(R.id.di_min_balance)
    DetailItem mDiMinBalance;

    @Bind(R.id.di_overdraft)
    DetailItem mDiOverdraft;

    @Bind(R.id.di_overdraft_due_date)
    DetailItem mDiOverdraftDueDate;

    @Bind(R.id.btn_transactions)
    Button mBtnTransactions;

    private View mRootView;
    private ZProgressHUD mProgress;
    private FragmentCallback mFragmentCallback;
    private AccountBalance mAccountBalance;
    private boolean mProgressResult = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentCallback = (FragmentCallback) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.corporate_account_fragment, container, false);
        ButterKnife.bind(this, mRootView);

        mProgress = ZProgressHUD.getInstance(getActivity());
        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();
        final Bundle bundle = this.getArguments();
        if (bundle != null && bundle.get(Constants.ACCOUNT_ID_EXTRA) != null) {

            final String id = bundle.getString(Constants.ACCOUNT_ID_EXTRA);

            Corporate.getInstance().getCorporateClient().getAccountsResource().withId(id).getBalanceResource().get(new CallbackWebApi<AccountBalance>() {
                @Override
                public void success(AccountBalance accountBalance) {
                    mAccountBalance = accountBalance;
                    if (mAccountBalance != null)
                        setAccountBalanceLayout();
                    mProgress.dismiss();
                }

                @Override
                public void failure(CsSDKError error) {
                    mProgress.dismissWithFailure();
                }
            });

            mBtnTransactions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragmentCallback.changeFragmentToTransactionsList(id);
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

    private void setAccountBalanceLayout() {
        Amount balance = mAccountBalance.getBalance();
        Amount disposable = mAccountBalance.getDisposable();
        Amount overdraft = mAccountBalance.getOverdraft();
        if (balance != null) {
            mDiBalance.setValue(String.valueOf(BigDecimal.valueOf(balance.getValue(), balance.getPrecision())) + " " + balance.getCurrencyRaw());
            mDiMinBalance.setValue(mAccountBalance.getMinBalance() + " " + balance.getCurrencyRaw());
        }
        if (disposable != null)
            mDiDisposable.setValue(String.valueOf(BigDecimal.valueOf(disposable.getValue(), disposable.getPrecision())) + " " + disposable.getCurrencyRaw());
        if (overdraft != null)
            mDiOverdraft.setValue(String.valueOf(BigDecimal.valueOf(overdraft.getValue(), overdraft.getPrecision())) + " " + overdraft.getCurrencyRaw());
        if (mAccountBalance.getOverdraftDueDate() != null)
            mDiOverdraftDueDate.setValue(mAccountBalance.getOverdraftDueDate().toString());
    }
}
