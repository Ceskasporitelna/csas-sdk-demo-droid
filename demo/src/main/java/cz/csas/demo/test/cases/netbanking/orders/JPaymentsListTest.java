package cz.csas.demo.test.cases.netbanking.orders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeRestService;
import cz.csas.cscore.utils.TimeUtils;
import cz.csas.cscore.webapi.Sort;
import cz.csas.cscore.webapi.SortDirection;
import cz.csas.cscore.webapi.signing.SigningObject;
import cz.csas.cscore.webapi.signing.SigningState;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.orders.ApplicationId;
import cz.csas.netbanking.orders.ChannelId;
import cz.csas.netbanking.orders.Payment;
import cz.csas.netbanking.orders.PaymentCategory;
import cz.csas.netbanking.orders.PaymentOrderType;
import cz.csas.netbanking.orders.PaymentState;
import cz.csas.netbanking.orders.PaymentStateDetail;
import cz.csas.netbanking.orders.PaymentsListResponse;
import cz.csas.netbanking.orders.PaymentsParameters;
import cz.csas.netbanking.orders.PaymentsSortableFields;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JPaymentsListTest extends TestCase {

    private final String X_JUDGE_CASE = "payments.list";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        final Map<String, String> headers = new HashMap<>();
                        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                        mNetbankingJudgeClient.setGlobalHeaders(headers);

                        PaymentsParameters parameters = new PaymentsParameters(null,
                                Sort.by(PaymentsSortableFields.TRANSFER_DATE, SortDirection.ASCENDING));

                        mNetbankingJudgeClient.getOrdersResource().getPaymentsResource().list(parameters, new CallbackWebApi<PaymentsListResponse>() {
                                    @Override
                                    public void success(PaymentsListResponse paymentsListResponse) {

                                        if (!assertEquals(Long.valueOf(0), paymentsListResponse.getPageNumber()))
                                            return;
                                        if (!assertEquals(Long.valueOf(1), paymentsListResponse.getPageCount()))
                                            return;
                                        if (!assertEquals(Long.valueOf(2), paymentsListResponse.getPageSize()))
                                            return;
                                        List<Payment> payments = paymentsListResponse.getPayments();

                                        for (int i = 0; i < payments.size(); ++i) {
                                            Payment payment = payments.get(i);
                                            Amount amount = payment.getAmount();
                                            AccountNumber receiver = payment.getReceiver();
                                            AccountNumber sender = payment.getSender();
                                            SigningObject signingObject = payment.signing();
                                            List<String> flags = new ArrayList<String>();
                                            flags.add("editable");
                                            flags.add("deletable");
                                            flags.add("signable");
                                            flags.add("own_transfer");

                                            switch (i) {
                                                case 0:
                                                    if (!assertEquals("1023464260", payment.getId()))
                                                        return;
                                                    if (!assertNull(payment.getAdditionalInfo().getText4x35(), "Payment.additionalInfo.text4x35"))
                                                        return;
                                                    if (!assertEquals(Long.valueOf(200000), amount.getValue()))
                                                        return;
                                                    if (!assertEquals(Integer.valueOf(2), amount.getPrecision()))
                                                        return;
                                                    if (!assertEquals("CZK", amount.getCurrency()))
                                                        return;
                                                    if (!assertEquals(ApplicationId.GEORGE, payment.getApplicationId()))
                                                        return;
                                                    if (!assertEquals(ChannelId.NET_BANKING, payment.getChannelId()))
                                                        return;
                                                    if (!assertEquals(TimeUtils.getISO8601Date("2016-03-20T00:00:00+01:00"), payment.getCzOrderingDate()))
                                                        return;
                                                    if (!assertEquals(TimeUtils.getISO8601Date("2016-03-20T00:00:00+01:00"), payment.getExecutionDate()))
                                                        return;
                                                    if (!assertEquals(flags, payment.getFlags()))
                                                        return;
                                                    if (!assertEquals(TimeUtils.getISO8601Date("2016-03-20T18:16:04+01:00"), payment.getModificationDate()))
                                                        return;
                                                    if (!assertEquals(PaymentCategory.OWN_TRANSFER, payment.getOrderCategory()))
                                                        return;
                                                    if (!assertEquals(PaymentOrderType.PAYMENT_OUT, payment.getOrderType()))
                                                        return;

                                                    if (!assertEquals("428602109", receiver.getNumber()))
                                                        return;
                                                    if (!assertEquals("0800", receiver.getBankCode()))
                                                        return;
                                                    if (!assertEquals("CZ", receiver.getCountryCode()))
                                                        return;
                                                    if (!assertEquals("CZ6408000000000428602109", receiver.getCzIban()))
                                                        return;
                                                    if (!assertEquals("GIBACZPX", receiver.getCzBic()))
                                                        return;

                                                    if (!assertEquals("2059930033", sender.getNumber()))
                                                        return;
                                                    if (!assertEquals("0800", sender.getBankCode()))
                                                        return;
                                                    if (!assertEquals("CZ", sender.getCountryCode()))
                                                        return;
                                                    if (!assertEquals("CZ1208000000002059930033", sender.getCzIban()))
                                                        return;
                                                    if (!assertEquals("GIBACZPX", sender.getCzBic()))
                                                        return;

                                                    if (!assertEquals("Aleš Vrba", payment.getSenderName()))
                                                        return;
                                                    if (!assertEquals(SigningState.OPEN, signingObject.getSigningState()))
                                                        return;
                                                    if (!assertEquals("1b20fd5dd9e41f0b0c08a3ebcafedcd1e2fe64ae937e0dc8a1e7f946b3d6b0f", signingObject.getSignId()))
                                                        return;
                                                    if (!assertEquals(PaymentState.OPEN, payment.getState()))
                                                        return;
                                                    if (!assertEquals(PaymentStateDetail.OPN, payment.getStateDetail()))
                                                        return;
                                                    if (!assertEquals(Boolean.valueOf(true), payment.getStateOk()))
                                                        return;
                                                    if (!assertEquals(TimeUtils.getPlainDate("2016-03-21"), payment.getTransferDate()))
                                                        return;
                                                    if (!assertEquals("Vrba Aleš", payment.getReceiverName()))
                                                        return;

                                                    break;
                                                case 1:
                                                    if (!assertEquals("1719856390", payment.getId()))
                                                        return;
                                                    List<String> text4x35 = new ArrayList<String>();
                                                    text4x35.add("Vlastní převod");
                                                    if (!assertEquals(text4x35, payment.getAdditionalInfo().getText4x35()))
                                                        return;
                                                    if (!assertEquals(Long.valueOf(12000), amount.getValue()))
                                                        return;
                                                    if (!assertEquals(Integer.valueOf(2), amount.getPrecision()))
                                                        return;
                                                    if (!assertEquals("CZK", amount.getCurrency()))
                                                        return;
                                                    if (!assertEquals(ApplicationId.GEORGE, payment.getApplicationId()))
                                                        return;
                                                    if (!assertEquals(ChannelId.NET_BANKING, payment.getChannelId()))
                                                        return;
                                                    if (!assertEquals(TimeUtils.getISO8601Date("2016-03-20T00:00:00+01:00"), payment.getCzOrderingDate()))
                                                        return;
                                                    if (!assertEquals(flags, payment.getFlags()))
                                                        return;
                                                    if (!assertEquals(TimeUtils.getISO8601Date("2016-03-20T18:13:40+01:00"), payment.getModificationDate()))
                                                        return;
                                                    if (!assertEquals(PaymentCategory.OWN_TRANSFER, payment.getOrderCategory()))
                                                        return;
                                                    if (!assertEquals(PaymentOrderType.PAYMENT_OUT, payment.getOrderType()))
                                                        return;

                                                    if (!assertEquals("2059930033", receiver.getNumber()))
                                                        return;
                                                    if (!assertEquals("0800", receiver.getBankCode()))
                                                        return;
                                                    if (!assertEquals("CZ", receiver.getCountryCode()))
                                                        return;
                                                    if (!assertEquals("CZ1208000000002059930033", receiver.getCzIban()))
                                                        return;
                                                    if (!assertEquals("GIBACZPX", receiver.getCzBic()))
                                                        return;

                                                    if (!assertEquals("3668601379", sender.getNumber()))
                                                        return;
                                                    if (!assertEquals("0800", sender.getBankCode()))
                                                        return;
                                                    if (!assertEquals("CZ", sender.getCountryCode()))
                                                        return;
                                                    if (!assertEquals("CZ7308000000003668601379", sender.getCzIban()))
                                                        return;
                                                    if (!assertEquals("GIBACZPX", sender.getCzBic()))
                                                        return;
                                                    if (!assertEquals("Spořicí účet k Osobnímu kontu", payment.getSenderName()))
                                                        return;
                                                    if (!assertEquals(SigningState.OPEN, signingObject.getSigningState()))
                                                        return;
                                                    if (!assertEquals("ff7d933db8049afd048712d445429218d485b63e5320acac31bd4289f2c8bbba", signingObject.getSignId()))
                                                        return;
                                                    if (!assertEquals(PaymentState.OPEN, payment.getState()))
                                                        return;
                                                    if (!assertEquals(PaymentStateDetail.OPN, payment.getStateDetail()))
                                                        return;
                                                    if (!assertEquals(Boolean.valueOf(true), payment.getStateOk()))
                                                        return;
                                                    if (!assertEquals(TimeUtils.getPlainDate("2016-03-21"), payment.getTransferDate()))
                                                        return;
                                                    if (!assertEquals("Aleš Vrba", payment.getReceiverName()))
                                                        return;
                                                    break;
                                            }
                                        }
                                        mTestCallback.result(mTestResult);
                                    }

                                    @Override
                                    public void failure(CsSDKError error) {
                                        handleError(error);
                                    }
                                }

                        );
                    }

                    @Override
                    public void failure(CsRestError error) {
                        handleError(error);
                    }
                }

        );
    }

    @Override
    public String getName() {
        return "JPaymentsListTest";
    }

}
