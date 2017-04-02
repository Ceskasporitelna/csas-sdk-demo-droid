package cz.csas.demo.netbanking;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.thuongnh.zprogresshud.ZProgressHUD;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.demo.Constants;
import cz.csas.demo.R;
import cz.csas.demo.components.DetailItem;
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.Netbanking;
import cz.csas.netbanking.accounts.MainAccount;


/**
 * The type Form desc fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class AccountDescFragment extends Fragment {

    /**
     * The Pdi acc number.
     */
    @Bind(R.id.pdi_acc_number)
    DetailItem pdiAccNumber;

    /**
     * The Pdi bank code.
     */
    @Bind(R.id.pdi_acc_bank_code)
    DetailItem pdiBankCode;

    /**
     * The Pdi iban.
     */
    @Bind(R.id.pdi_iban)
    DetailItem pdiIban;

    /**
     * The Pdi balance.
     */
    @Bind(R.id.pdi_balance)
    DetailItem pdiBalance;

    /**
     * The Pdi currency.
     */
    @Bind(R.id.pdi_currency)
    DetailItem pdiCurrency;

    /**
     * The Pdi cz iban.
     */
    @Bind(R.id.pdi_cziban)
    DetailItem pdiCzIban;

    /**
     * The Pdi bic.
     */
    @Bind(R.id.pdi_bic)
    DetailItem pdiBic;

    /**
     * The Pdi cz bic.
     */
    @Bind(R.id.pdi_czbic)
    DetailItem pdiCzBic;

    /**
     * The Pdi country code.
     */
    @Bind(R.id.pdi_country_code)
    DetailItem pdiCountryCode;

    @Bind(R.id.rl_btn_domestic_payment)
    RelativeLayout rlBtnDomesticPayment;

    @Bind(R.id.btn_domestic_payment)
    Button btnDomesticPayment;

    private View mRootView;
    private ZProgressHUD mProgress;
    private FragmentCallback mFragmentCallback;
    private MainAccount mAccount;
    private boolean mProgressResult = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentCallback = (FragmentCallback) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.netbanking_account_fragment, container, false);
        ButterKnife.bind(this, mRootView);

        mProgress = ZProgressHUD.getInstance(getActivity());
        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();
        final Bundle bundle = this.getArguments();
        if (bundle != null && bundle.get(Constants.ACCOUNT_ID_EXTRA) != null) {

            final String id = bundle.getString(Constants.ACCOUNT_ID_EXTRA);
            Netbanking.getInstance().getNetbankingClient().getAccountsResource().withId(id).get(new CallbackWebApi<MainAccount>() {
                @Override
                public void success(MainAccount mainAccount) {
                    mAccount = mainAccount;
                    if (mAccount != null)
                        setAccountLayout();
                    mProgress.dismiss();
                }

                @Override
                public void failure(CsSDKError error) {
                    mProgress.dismissWithFailure();

                }
            });

            btnDomesticPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragmentCallback.changeFragmentToDomesticPayment(id);
                }
            });
        } else {
            mProgress.dismissWithFailure();
            mFragmentCallback.changeFragmentToAccountList();
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

    private void setAccountLayout() {
        AccountNumber accountNumber = mAccount.getAccountno();
        if (accountNumber != null) {
            rlBtnDomesticPayment.setVisibility(View.VISIBLE);
            String number = accountNumber.getNumber();
            String bankCode = accountNumber.getBankCode();
            String czIban = accountNumber.getCzIban();
            String czBic = accountNumber.getCzBic();
            String countryCode = accountNumber.getCountryCode();
            if (number != null)
                pdiAccNumber.setValue(number);
            if (bankCode != null)
                pdiBankCode.setValue(bankCode);
            if (czIban != null)
                pdiCzIban.setValue(czIban);
            if (czBic != null)
                pdiCzBic.setValue(czBic);
            if (countryCode != null)
                pdiCountryCode.setValue(countryCode);
        }
        Amount amount = mAccount.getBalance();
        if (amount != null) {
            pdiBalance.setValue(String.valueOf(BigDecimal.valueOf(amount.getValue(), amount.getPrecision())));
            pdiCurrency.setValue(amount.getCurrency());
        }
    }
}
