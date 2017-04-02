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
import cz.csas.cscore.utils.TimeUtils;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.orders.PaymentBookingDateRequest;
import cz.csas.netbanking.orders.PaymentBookingDateResponse;
import cz.csas.netbanking.orders.PaymentOrderPriority;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JPaymentBookingDateUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "payments.bookingDate.update";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String accountId = "4B2F9EBE742BCAE1E98A78E12F6FBC62464A74EE";
                AccountNumber receiver = new AccountNumber("123-123", "0100", "CZ", null, null);

                PaymentBookingDateRequest request = new PaymentBookingDateRequest(accountId, receiver, PaymentOrderPriority.STANDARD);
                mNetbankingJudgeClient.getOrdersResource().getPaymentsResource().getBookingDateResource().update(request, new CallbackWebApi<PaymentBookingDateResponse>() {
                    @Override
                    public void success(PaymentBookingDateResponse paymentBookingDateResponse) {
                        if (!assertEquals(TimeUtils.getISO8601Date("2016-03-21T00:00:00+01:00"), paymentBookingDateResponse.getBookingDate()))
                            return;

                        mTestCallback.result(mTestResult);
                    }

                    @Override
                    public void failure(CsSDKError error) {
                        handleError(error);
                    }
                });
            }

            @Override
            public void failure(CsRestError error) {
                handleError(error);
            }
        });
    }

    @Override
    public String getName() {
        return "JPaymentBookingDateUpdateTest";
    }

}
