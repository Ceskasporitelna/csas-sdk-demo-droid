package cz.csas.demo.test.cases.netbanking.authorizationLimits;

import java.util.HashMap;
import java.util.Map;

import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeRestService;
import cz.csas.cscore.webapi.signing.AuthorizationType;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.authorizationLimits.ApplicationId;
import cz.csas.netbanking.authorizationLimits.AuthorizationLimit;
import cz.csas.netbanking.authorizationLimits.ChannelId;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JAuthorizationLimitsGetTest extends TestCase {

    private final String X_JUDGE_CASE = "authorizationLimits.withId.get";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String id = "934872973982";
                mNetbankingJudgeClient.getAuthorizationLimitsResource().withId(id).get(new CallbackWebApi<AuthorizationLimit>() {
                    @Override
                    public void success(AuthorizationLimit authorizationLimit) {
                        AuthorizationLimit limit = authorizationLimit;
                        Amount dailyLimit = limit.getDailyLimit();
                        Amount transactionLimit = limit.getTransactionLimit();
                        Amount maxBankLimit = limit.getMaxBankLimit();

                        if (!assertEquals("934872973982", limit.getId()))
                            return;
                        if (!assertEquals(AuthorizationType.TAC, limit.getAuthorizationType()))
                            return;
                        if (!assertEquals(ChannelId.NET_BANKING, limit.getChannelId()))
                            return;
                        if (!assertEquals(ApplicationId.GEORGE, limit.getApplicationId()))
                            return;
                        if (!assertEquals(Long.valueOf(400000), dailyLimit.getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), dailyLimit.getPrecision()))
                            return;
                        if (!assertEquals("CZK", transactionLimit.getCurrency()))
                            return;
                        if (!assertEquals(Long.valueOf(100000), transactionLimit.getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), transactionLimit.getPrecision()))
                            return;
                        if (!assertEquals("CZK", dailyLimit.getCurrency()))
                            return;
                        if (!assertEquals(Long.valueOf(1700000), maxBankLimit.getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), maxBankLimit.getPrecision()))
                            return;
                        if (!assertEquals("CZK", maxBankLimit.getCurrency()))
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
        return "JAuthorizationLimitsGetTest";
    }

}
