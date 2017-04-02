package cz.csas.demo.test.cases.netbanking.orders;

import java.util.HashMap;
import java.util.Map;

import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeRestService;
import cz.csas.cscore.webapi.signing.SigningState;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.orders.MobilePaymentRequest;
import cz.csas.netbanking.orders.MobilePaymentResponse;
import cz.csas.netbanking.orders.MobilePaymentSender;
import cz.csas.netbanking.orders.MobilePaymentType;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JPaymentsMobileTest extends TestCase {

    private final String X_JUDGE_CASE = "payments.mobile.create";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        final Map<String, String> headers = new HashMap<>();
                        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                        mNetbankingJudgeClient.setGlobalHeaders(headers);
                        MobilePaymentRequest request = new MobilePaymentRequest.Builder()
                                .setPaymentType(MobilePaymentType.VODAFONE_PAYMENT)
                                .setPhoneNumber("777952341")
                                .setSender(new MobilePaymentSender("2059930033", "0800", "CZ", "CZ1208000000002059930033", "GIBACZPX"))
                                .setAmount(new Amount(3000L, 0, "CZK"))
                                .setConfirmationPhoneNumber("777952341")
                                .build();

                        mNetbankingJudgeClient.getOrdersResource().getPaymentsResource().getMobileResource().create(request, new CallbackWebApi<MobilePaymentResponse>() {
                                    @Override
                                    public void success(MobilePaymentResponse mobilePaymentResponse) {

                                        if (!assertEquals(MobilePaymentType.VODAFONE_PAYMENT, mobilePaymentResponse.getPaymentType()))
                                            return;
                                        if (!assertEquals("777952341", mobilePaymentResponse.getPhoneNumber()))
                                            return;
                                        if (!assertEquals("2059930033", mobilePaymentResponse.getSender().getNumber()))
                                            return;
                                        if (!assertEquals("0800", mobilePaymentResponse.getSender().getBankCode()))
                                            return;
                                        if (!assertEquals("CZ", mobilePaymentResponse.getSender().getCountryCode()))
                                            return;
                                        if (!assertEquals("CZ1208000000002059930033", mobilePaymentResponse.getSender().getIban()))
                                            return;
                                        if (!assertEquals("GIBACZPX", mobilePaymentResponse.getSender().getBic()))
                                            return;
                                        if (!assertEquals(Long.valueOf(300000), mobilePaymentResponse.getAmount().getValue()))
                                            return;
                                        if (!assertEquals(Integer.valueOf(2), mobilePaymentResponse.getAmount().getPrecision()))
                                            return;
                                        if (!assertEquals("CZK", mobilePaymentResponse.getAmount().getCurrency()))
                                            return;
                                        if (!assertEquals("777952341", mobilePaymentResponse.getConfirmationPhoneNumber()))
                                            return;
                                        if (!assertEquals(SigningState.OPEN, mobilePaymentResponse.signing().getSigningState()))
                                            return;
                                        if (!assertEquals("1671744209", mobilePaymentResponse.signing().getSignId()))
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
        return "JPaymentsMobileTest";
    }

}
