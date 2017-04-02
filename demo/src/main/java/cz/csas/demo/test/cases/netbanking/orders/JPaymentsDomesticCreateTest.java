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
import cz.csas.cscore.webapi.signing.SigningState;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.orders.ApplicationId;
import cz.csas.netbanking.orders.ChannelId;
import cz.csas.netbanking.orders.DomesticPaymentCreateRequest;
import cz.csas.netbanking.orders.DomesticPaymentCreateResponse;
import cz.csas.netbanking.orders.Payment;
import cz.csas.netbanking.orders.PaymentCategory;
import cz.csas.netbanking.orders.PaymentOrderType;
import cz.csas.netbanking.orders.PaymentState;
import cz.csas.netbanking.orders.PaymentStateDetail;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JPaymentsDomesticCreateTest extends TestCase {

    private final String X_JUDGE_CASE = "payments.domestic.create";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        final Map<String, String> headers = new HashMap<>();
                        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                        mNetbankingJudgeClient.setGlobalHeaders(headers);

                        DomesticPaymentCreateRequest request = new DomesticPaymentCreateRequest.Builder()
                                .setSenderName("Vrba")
                                .setSender(new AccountNumber("2059930033", "0800"))
                                .setReceiverName("Vojtíšková")
                                .setReceiver(new AccountNumber("2328489013", "0800"))
                                .setAmount(new Amount(110L, 2, "CZK"))
                                .build();

                        mNetbankingJudgeClient.getOrdersResource().getPaymentsResource().getDomesticResource().create(request, new CallbackWebApi<DomesticPaymentCreateResponse>() {
                                    @Override
                                    public void success(DomesticPaymentCreateResponse domesticOrderResponse) {

                                        Payment payment = domesticOrderResponse;

                                        if (!assertEquals("1154226597", payment.getId()))
                                            return;
                                        if (!assertEquals(Long.valueOf(110), payment.getAmount().getValue()))
                                            return;
                                        if (!assertEquals(Integer.valueOf(2), payment.getAmount().getPrecision()))
                                            return;
                                        if (!assertEquals("CZK", payment.getAmount().getCurrency()))
                                            return;
                                        if (!assertEquals(ApplicationId.GEORGE, payment.getApplicationId()))
                                            return;
                                        if (!assertEquals(ChannelId.NET_BANKING, payment.getChannelId()))
                                            return;
                                        if (!assertEquals(TimeUtils.getISO8601Date("2016-03-21T10:30:54+01:00"), payment.getCzOrderingDate()))
                                            return;
                                        if (!assertEquals(TimeUtils.getISO8601Date("2016-03-21T00:00:00+01:00"), payment.getExecutionDate()))
                                            return;

                                        List<String> flags = new ArrayList<String>();
                                        flags.add("editable");
                                        flags.add("deletable");
                                        if (!assertEquals(flags, payment.getFlags()))
                                            return;
                                        if (!assertEquals(TimeUtils.getISO8601Date("2016-03-21T10:30:54+01:00"), payment.getModificationDate()))
                                            return;
                                        if (!assertEquals(PaymentCategory.DOMESTIC, payment.getOrderCategory()))
                                            return;
                                        if (!assertEquals(PaymentOrderType.PAYMENT_OUT, payment.getOrderType()))
                                            return;

                                        if (!assertEquals("2328489013", payment.getReceiver().getNumber()))
                                            return;
                                        if (!assertEquals("0800", payment.getReceiver().getBankCode()))
                                            return;
                                        if (!assertEquals("CZ", payment.getReceiver().getCountryCode()))
                                            return;
                                        if (!assertEquals("CZ5908000000002328489013", payment.getReceiver().getCzIban()))
                                            return;
                                        if (!assertEquals("GIBACZPX", payment.getReceiver().getCzBic()))
                                            return;
                                        if (!assertEquals("Vojtíšková", payment.getReceiverName()))
                                            return;

                                        if (!assertEquals("2059930033", payment.getSender().getNumber()))
                                            return;
                                        if (!assertEquals("0800", payment.getSender().getBankCode()))
                                            return;
                                        if (!assertEquals("CZ", payment.getSender().getCountryCode()))
                                            return;
                                        if (!assertEquals("CZ1208000000002059930033", payment.getSender().getCzIban()))
                                            return;
                                        if (!assertEquals("GIBACZPX", payment.getSender().getCzBic()))
                                            return;
                                        if (!assertEquals("Vrba", payment.getSenderName()))
                                            return;

                                        if (!assertEquals("8d0e89d424dd2176f94e7ba15bb97ff3362bd74ecc1f58b1119ab75f4bf96f61", payment.signing().getSignId()))
                                            return;
                                        if (!assertEquals(SigningState.OPEN, payment.signing().getSigningState()))
                                            return;

                                        if (!assertEquals(PaymentState.OPEN, payment.getState()))
                                            return;
                                        if (!assertEquals(PaymentStateDetail.OPN, payment.getStateDetail()))
                                            return;
                                        if (!assertEquals(Boolean.valueOf(true), payment.getStateOk()))
                                            return;
                                        if (!assertEquals(TimeUtils.getPlainDate("2016-03-21"), payment.getTransferDate()))
                                            return;
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
        return "JPaymentsDomesticCreateTest";
    }

}
