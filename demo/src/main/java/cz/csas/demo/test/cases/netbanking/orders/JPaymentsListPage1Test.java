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
import cz.csas.cscore.webapi.Pagination;
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

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JPaymentsListPage1Test extends TestCase {

    private final String X_JUDGE_CASE = "payments.list.page1";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        final Map<String, String> headers = new HashMap<>();
                        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                        mNetbankingJudgeClient.setGlobalHeaders(headers);

                        PaymentsParameters parameters = new PaymentsParameters(new Pagination(1, 1), null);

                        mNetbankingJudgeClient.getOrdersResource().getPaymentsResource().list(parameters, new CallbackWebApi<PaymentsListResponse>() {
                                    @Override
                                    public void success(PaymentsListResponse paymentsListResponse) {

                                        if (!assertEquals(Long.valueOf(1), paymentsListResponse.getPageNumber()))
                                            return;
                                        if (!assertEquals(Long.valueOf(7), paymentsListResponse.getPageCount()))
                                            return;
                                        if (!assertEquals(Long.valueOf(1), paymentsListResponse.getPageSize()))
                                            return;
                                        if (!assertEquals(Long.valueOf(2), paymentsListResponse.getNextPage()))
                                            return;

                                        Payment payment = paymentsListResponse.getPayments().get(0);
                                        Amount amount = payment.getAmount();
                                        AccountNumber receiver = payment.getReceiver();
                                        AccountNumber sender = payment.getSender();
                                        SigningObject signingObject = payment.signing();
                                        List<String> flags = new ArrayList<String>();
                                        flags.add("redoable");
                                        flags.add("own_transfer");

                                        if (!assertEquals("T4B2F9EBE742BCAE1E98A78E12F6FBC62464A74EE_1XZ1XZO5o0VZB", payment.getId()))
                                            return;
                                        if (!assertNull(payment.getAdditionalInfo().getText4x35(), "Payment.additionalInfo.text4x35"))
                                            return;
                                        if (!assertEquals(Long.valueOf(100000), amount.getValue()))
                                            return;
                                        if (!assertEquals(Integer.valueOf(2), amount.getPrecision()))
                                            return;
                                        if (!assertEquals("CZK", amount.getCurrency()))
                                            return;
                                        if (!assertEquals(ApplicationId.GEORGE, payment.getApplicationId()))
                                            return;
                                        if (!assertEquals(ChannelId.NET_BANKING, payment.getChannelId()))
                                            return;
                                        if (!assertEquals(TimeUtils.getISO8601Date("2016-03-22T00:00:00+01:00"), payment.getCzOrderingDate()))
                                            return;
                                        if (!assertEquals(TimeUtils.getISO8601Date("2016-03-22T00:00:00+01:00"), payment.getExecutionDate()))
                                            return;
                                        if (!assertEquals(flags, payment.getFlags()))
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

                                        if (!assertEquals("R1EZG2CY", payment.getReferenceId()))
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

                                        if (!assertEquals(SigningState.NONE, signingObject.getSigningState()))
                                            return;
                                        if (!assertEquals(PaymentState.CLOSED, payment.getState()))
                                            return;
                                        if (!assertEquals(PaymentStateDetail.FIN, payment.getStateDetail()))
                                            return;
                                        if (!assertEquals(Boolean.valueOf(true), payment.getStateOk()))
                                            return;
                                        if (!assertEquals(TimeUtils.getPlainDate("2016-03-22"), payment.getTransferDate()))
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
        return "JPaymentsListPage1Test";
    }

}
