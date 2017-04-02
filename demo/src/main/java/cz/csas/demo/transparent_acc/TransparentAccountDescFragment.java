package cz.csas.demo.transparent_acc;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.thuongnh.zprogresshud.ZProgressHUD;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.utils.TimeUtils;
import cz.csas.demo.Constants;
import cz.csas.demo.R;
import cz.csas.demo.components.DetailItem;
import cz.csas.transparent_acc.TransparentAccount;
import cz.csas.transparent_acc.TransparentAccounts;


/**
 * The type Form desc fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class TransparentAccountDescFragment extends Fragment {

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
     * The Pdi trans from.
     */
    @Bind(R.id.pdi_transparency_from)
    DetailItem pdiTransFrom;

    /**
     * The Pdi trans to.
     */
    @Bind(R.id.pdi_transparency_to)
    DetailItem pdiTransTo;

    /**
     * The Pdi pub to.
     */
    @Bind(R.id.pdi_publication_to)
    DetailItem pdiPubTo;

    /**
     * The Pdi act date.
     */
    @Bind(R.id.pdi_actualisation_date)
    DetailItem pdiActDate;

    /**
     * The Pdi name.
     */
    @Bind(R.id.pdi_name)
    DetailItem pdiName;

    /**
     * The Pdi desc.
     */
    @Bind(R.id.pdi_description)
    DetailItem pdiDesc;

    /**
     * The Pdi note.
     */
    @Bind(R.id.pdi_note)
    DetailItem pdiNote;

    /**
     * The Btn transactions.
     */
    @Bind(R.id.btn_transactions)
    Button btnTransactions;


    private View mRootView;
    private ZProgressHUD mProgress;
    private cz.csas.demo.transparent_acc.FragmentCallback mFragmentCallback;
    private TransparentAccount mTransparentAccount;
    private boolean mProgressResult = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentCallback = (cz.csas.demo.transparent_acc.FragmentCallback) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.transparent_acc_fragment, container, false);
        ButterKnife.bind(this, mRootView);

        mProgress = ZProgressHUD.getInstance(getActivity());
        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();
        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.get(Constants.ACCOUNT_ID_EXTRA) != null) {

            TransparentAccounts.getInstance().getTransparentAccountsClient().getTransparentAccountsResource().withId(bundle.get(Constants.ACCOUNT_ID_EXTRA)).get(new CallbackWebApi<TransparentAccount>() {
                @Override
                public void success(TransparentAccount transparentAccount) {
                    mTransparentAccount = transparentAccount;
                    setAccountLayout();
                    mProgress.dismiss();
                }

                @Override
                public void failure(CsSDKError error) {
                    mProgress.dismissWithFailure();

                }
            });
        } else {
            mProgress.dismissWithFailure();
            mFragmentCallback.changeFragmentToAccountsList();
        }

        btnTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentCallback.changeFragmentToTransactionsList(mTransparentAccount.getId());
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

    private void setAccountLayout() {
        pdiAccNumber.setValue(mTransparentAccount.getAccountNumber());
        pdiBankCode.setValue(mTransparentAccount.getBankCode());
        pdiBalance.setValue(String.valueOf(mTransparentAccount.getBalance()));
        pdiCurrency.setValue(mTransparentAccount.getCurrency());
        pdiIban.setValue(mTransparentAccount.getIban());
        if (mTransparentAccount.getTransparencyFrom() != null)
            pdiTransFrom.setValue(TimeUtils.getISO8601String(mTransparentAccount.getTransparencyFrom()));
        if (mTransparentAccount.getTransparencyTo() != null)
            pdiTransTo.setValue(TimeUtils.getISO8601String(mTransparentAccount.getTransparencyTo()));
        if (mTransparentAccount.getPublicationTo() != null)
            pdiPubTo.setValue(TimeUtils.getISO8601String(mTransparentAccount.getPublicationTo()));
        if (mTransparentAccount.getActualizationDate() != null)
            pdiActDate.setValue(TimeUtils.getISO8601String(mTransparentAccount.getActualizationDate()));
        pdiName.setValue(mTransparentAccount.getName());
        pdiDesc.setValue(mTransparentAccount.getDescription());
        pdiNote.setValue(mTransparentAccount.getNote());

    }
}
