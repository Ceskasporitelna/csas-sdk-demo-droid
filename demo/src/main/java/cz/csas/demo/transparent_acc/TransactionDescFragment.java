package cz.csas.demo.transparent_acc;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.thuongnh.zprogresshud.ZProgressHUD;
import cz.csas.cscore.utils.TimeUtils;
import cz.csas.demo.R;
import cz.csas.demo.components.DetailItem;
import cz.csas.transparent_acc.Transaction;


/**
 * The type Transaction desc fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class TransactionDescFragment extends Fragment {


    /**
     * The Pdi amount.
     */
    @Bind(R.id.pdi_amount)
    DetailItem pdiAmount;

    /**
     * The Pdi type.
     */
    @Bind(R.id.pdi_type)
    DetailItem pdiType;

    /**
     * The Pdi processing date.
     */
    @Bind(R.id.pdi_processing_date)
    DetailItem pdiProcessingDate;

    /**
     * The Pdi due date.
     */
    @Bind(R.id.pdi_due_date)
    DetailItem pdiDueDate;

    /**
     * The Pdi receiver iban.
     */
    @Bind(R.id.pdi_receiver_iban)
    DetailItem pdiReceiverIban;

    /**
     * The Pdi receiver acc num.
     */
    @Bind(R.id.pdi_receiver_acc_num)
    DetailItem pdiReceiverAccNum;

    /**
     * The Pdi receiver bank code.
     */
    @Bind(R.id.pdi_receiver_bank_code)
    DetailItem pdiReceiverBankCode;

    /**
     * The Pdi sender acc num.
     */
    @Bind(R.id.pdi_sender_acc_num)
    DetailItem pdiSenderAccNum;

    /**
     * The Pdi sender name.
     */
    @Bind(R.id.pdi_sender_name)
    DetailItem pdiSenderName;

    @Bind(R.id.pdi_sender_bank_code)
    DetailItem pdiSenderBankCode;

    @Bind(R.id.pdi_sender_iban)
    DetailItem pdiSenderIban;

    @Bind(R.id.pdi_sender_vs)
    DetailItem pdiSenderVs;

    @Bind(R.id.pdi_sender_cs)
    DetailItem pdiSenderCS;

    @Bind(R.id.pdi_sender_ss)
    DetailItem pdiSenderSS;

    @Bind(R.id.pdi_sender_ssp)
    DetailItem pdiSenderSSP;

    @Bind(R.id.pdi_sender_description)
    DetailItem pdiSenderDescription;


    private View mRootView;
    private ZProgressHUD mProgress;
    private cz.csas.demo.transparent_acc.FragmentCallback mFragmentCallback;
    private Transaction mTransaction;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentCallback = (cz.csas.demo.transparent_acc.FragmentCallback) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.transaction_fragment, container, false);
        ButterKnife.bind(this, mRootView);

        mProgress = ZProgressHUD.getInstance(getActivity());
        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();
        mTransaction = ((TransparentAccActivity) getActivity()).getTransaction();

        if (mTransaction != null) {
            setTransaction();
            mProgress.dismiss();
        } else {
            mProgress.dismissWithFailure();
            mFragmentCallback.changeFragmentToAccountsList();
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

    private void setTransaction() {
        if (mTransaction.getAmount() != null && mTransaction.getAmount().getValue() != null && mTransaction.getAmount().getPrecision() != null && mTransaction.getAmount().getCurrency() != null) {
            double amount = mTransaction.getAmount().getValue() / Math.pow(10, mTransaction.getAmount().getPrecision());
            pdiAmount.setValue(String.valueOf(amount) + " " + mTransaction.getAmount().getCurrency());
        }
        if (mTransaction.getType() != null)
            pdiType.setValue(mTransaction.getType());
        if (mTransaction.getProcessingDate() != null)
            pdiProcessingDate.setValue(TimeUtils.getISO8601String(mTransaction.getProcessingDate()));
        if (mTransaction.getDueDate() != null)
            pdiDueDate.setValue(TimeUtils.getISO8601String(mTransaction.getDueDate()));
        if (mTransaction.getReceiver() != null) {
            if (mTransaction.getReceiver().getAccountNumber() != null)
                pdiReceiverAccNum.setValue(mTransaction.getReceiver().getAccountNumber());
            if (mTransaction.getReceiver().getBankCode() != null)
                pdiReceiverBankCode.setValue(mTransaction.getReceiver().getBankCode());
            if (mTransaction.getReceiver().getIban() != null)
                pdiReceiverIban.setValue(mTransaction.getReceiver().getIban());
        }
        if (mTransaction.getSenderName() != null)
            pdiSenderName.setValue(mTransaction.getSenderName());
        if (mTransaction.getSender() != null) {
            if (mTransaction.getSender().getName() != null)
                pdiSenderName.setValue(mTransaction.getSender().getName());
            if (mTransaction.getSender().getAccountNumber() != null)
                pdiSenderAccNum.setValue(mTransaction.getSender().getAccountNumber());
            if (mTransaction.getSender().getBankCode() != null)
                pdiSenderBankCode.setValue(mTransaction.getSender().getBankCode());
            if (mTransaction.getSender().getIban() != null)
                pdiSenderIban.setValue(mTransaction.getSender().getIban());
            if (mTransaction.getSender().getVariableSymbol() != null)
                pdiSenderVs.setValue(mTransaction.getSender().getVariableSymbol());
            if (mTransaction.getSender().getConstantSymbol() != null)
                pdiSenderCS.setValue(mTransaction.getSender().getConstantSymbol());
            if (mTransaction.getSender().getSpecificSymbol() != null)
                pdiSenderSS.setValue(mTransaction.getSender().getSpecificSymbol());
            if (mTransaction.getSender().getSpecificSymbolParty() != null)
                pdiSenderSSP.setValue(mTransaction.getSender().getSpecificSymbolParty());
            if (mTransaction.getSender().getDescription() != null)
                pdiSenderDescription.setValue(mTransaction.getSender().getDescription());
        }
    }

}
