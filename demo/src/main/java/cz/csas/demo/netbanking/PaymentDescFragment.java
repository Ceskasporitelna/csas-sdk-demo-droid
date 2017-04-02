package cz.csas.demo.netbanking;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.math.BigDecimal;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.thuongnh.zprogresshud.ZProgressHUD;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.utils.TimeUtils;
import cz.csas.demo.Constants;
import cz.csas.demo.R;
import cz.csas.demo.components.DetailItem;
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.Netbanking;
import cz.csas.netbanking.orders.Payment;
import cz.csas.netbanking.orders.PaymentCategory;
import cz.csas.netbanking.orders.PaymentOrderType;
import cz.csas.netbanking.orders.PaymentState;


/**
 * The type Payment desc fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class PaymentDescFragment extends Fragment {

    /**
     * The Pdi amount.
     */
    @Bind(R.id.pdi_amount)
    DetailItem pdiAmount;

    /**
     * The Pdi state.
     */
    @Bind(R.id.pdi_state)
    DetailItem pdiState;

    /**
     * The Pdi category.
     */
    @Bind(R.id.pdi_category)
    DetailItem pdiCategory;

    /**
     * The Pdi type.
     */
    @Bind(R.id.pdi_type)
    DetailItem pdiType;

    /**
     * The Pdi execution date.
     */
    @Bind(R.id.pdi_execution_date)
    DetailItem pdiExecutionDate;

    /**
     * The Pdi sender name.
     */
    @Bind(R.id.pdi_receiver_name)
    DetailItem pdiReceiverName;
    /**
     * The Pdi receiver iban.
     */
    @Bind(R.id.pdi_receiver_iban)
    DetailItem pdiReceiverIban;


    /**
     * The Pdi receiver cz iban.
     */
    @Bind(R.id.pdi_receiver_cziban)
    DetailItem pdiReceiverCzIban;

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
     * The Pdi sender name.
     */
    @Bind(R.id.pdi_sender_name)
    DetailItem pdiSenderName;


    /**
     * The Pdi sender iban.
     */
    @Bind(R.id.pdi_sender_iban)
    DetailItem pdiSenderIban;


    /**
     * The Pdi sender cz iban.
     */
    @Bind(R.id.pdi_sender_cziban)
    DetailItem pdiSenderCzIban;

    /**
     * The Pdi sender acc num.
     */
    @Bind(R.id.pdi_sender_acc_num)
    DetailItem pdiSenderAccNum;

    /**
     * The Pdi sender bank code.
     */
    @Bind(R.id.pdi_sender_bank_code)
    DetailItem pdiSenderBankCode;


    private View mRootView;
    private ZProgressHUD mProgress;
    private FragmentCallback mFragmentCallback;
    private Payment mPayment;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentCallback = (FragmentCallback) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.netbanking_payment_fragment, container, false);
        ButterKnife.bind(this, mRootView);

        mProgress = ZProgressHUD.getInstance(getActivity());
        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();
        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.get(Constants.PAYMENT_ID_EXTRA) != null) {

            Netbanking.getInstance().getNetbankingClient().getOrdersResource().getPaymentsResource().withId(bundle.getString(Constants.PAYMENT_ID_EXTRA)).get(new CallbackWebApi<Payment>() {
                @Override
                public void success(Payment payment) {
                    mPayment = payment;
                    if (mPayment != null)
                        setPaymentLayout();
                    mProgress.dismiss();
                }

                @Override
                public void failure(CsSDKError error) {
                    mProgress.dismissWithFailure();

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

    private void setPaymentLayout() {
        Amount amount = mPayment.getAmount();
        if (amount != null && amount.getValue() != null && amount.getPrecision() != null && amount.getCurrency() != null) {
            BigDecimal value = BigDecimal.valueOf(amount.getValue(), amount.getPrecision());
            pdiAmount.setValue(String.valueOf(value) + " " + amount.getCurrency());
        }
        PaymentState state = mPayment.getState();
        if (state != null)
            pdiState.setValue(state.getValue());

        PaymentCategory category = mPayment.getOrderCategory();
        if (category != null)
            pdiCategory.setValue(category.getValue());

        PaymentOrderType type = mPayment.getOrderType();
        if (type != null)
            pdiType.setValue(type.getValue());

        Date executionDate = mPayment.getExecutionDate();
        if (executionDate != null)
            pdiExecutionDate.setValue(TimeUtils.getPlainDateString(executionDate));

        String receiver = mPayment.getReceiverName();
        if (receiver != null)
            pdiReceiverName.setValue(receiver);

        AccountNumber recieverNumber = mPayment.getReceiver();
        if (recieverNumber != null) {
            String cziban = recieverNumber.getCzIban();
            if (cziban != null)
                pdiReceiverCzIban.setValue(cziban);
            String number = recieverNumber.getNumber();
            if (number != null)
                pdiReceiverAccNum.setValue(number);
            String bankCode = recieverNumber.getBankCode();
            if (bankCode != null)
                pdiReceiverBankCode.setValue(bankCode);
        }

        String sender = mPayment.getSenderName();
        if (sender != null)
            pdiSenderName.setValue(sender);

        AccountNumber senderNumber = mPayment.getSender();
        if (senderNumber != null) {
            String cziban = senderNumber.getCzIban();
            if (cziban != null)
                pdiSenderCzIban.setValue(cziban);
            String number = senderNumber.getNumber();
            if (number != null)
                pdiSenderAccNum.setValue(number);
            String bankCode = senderNumber.getBankCode();
            if (bankCode != null)
                pdiSenderBankCode.setValue(bankCode);
        }
    }

}
