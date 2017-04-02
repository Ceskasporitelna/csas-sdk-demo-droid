package cz.csas.demo.test.cases.netbanking.orders;

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
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.orders.ApplicationId;
import cz.csas.netbanking.orders.AuthorizationType;
import cz.csas.netbanking.orders.ChannelId;
import cz.csas.netbanking.orders.PaymentLimit;
import cz.csas.netbanking.orders.PaymentLimitsListResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JPaymentLimitsListTest extends TestCase {

    private final String X_JUDGE_CASE = "payments.limits.list";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                mNetbankingJudgeClient.getOrdersResource().getPaymentsResource().getLimitsResource().list(new CallbackWebApi<PaymentLimitsListResponse>() {
                    @Override
                    public void success(PaymentLimitsListResponse paymentLimitsListResponse) {

                        List<PaymentLimit> limits = paymentLimitsListResponse.getPaymentLimits();
                        if (!assertEquals(2, limits.size()))
                            return;

                        for (int i = 0; i < limits.size(); ++i) {
                            PaymentLimit limit = limits.get(i);
                            switch (i) {
                                case 0:
                                    if (!assertEquals(AuthorizationType.TAC, limit.getAuthorizationType()))
                                        return;
                                    if (!assertEquals(ChannelId.NET_BANKING, limit.getChannelId()))
                                        return;
                                    if (!assertEquals(ApplicationId.GEORGE, limit.getApplicationId()))
                                        return;
                                    if (!assertEquals(Long.valueOf(99999999999999L), limit.getRemainingAmount().getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), limit.getRemainingAmount().getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", limit.getRemainingAmount().getCurrency()))
                                        return;
                                    break;
                                case 1:
                                    if (!assertEquals(AuthorizationType.TAC, limit.getAuthorizationType()))
                                        return;
                                    if (!assertEquals(ChannelId.NET_BANKING, limit.getChannelId()))
                                        return;
                                    if (!assertEquals(ApplicationId.UNKNOWN, limit.getApplicationId()))
                                        return;
                                    if (!assertEquals(Long.valueOf(99999999999999L), limit.getRemainingAmount().getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), limit.getRemainingAmount().getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", limit.getRemainingAmount().getCurrency()))
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
        return "JPaymentLimitsListTest";
    }

}
