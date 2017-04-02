package cz.csas.demo.test.cases.netbanking.securities;

import java.util.HashMap;
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
import cz.csas.netbanking.securities.Security;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JSecuritiesWithIdGetTest extends TestCase {

    private final String X_JUDGE_CASE = "securities.withId.get";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String id = "420A817C20E4814C7C516A53ABA8E78F0CDBE324";
                mNetbankingJudgeClient.getSecuritiesResource().withId(id).get(new CallbackWebApi<Security>() {
                    @Override
                    public void success(Security security) {
                        if (!assertEquals("420A817C20E4814C7C516A53ABA8E78F0CDBE324", security.getId()))
                            return;
                        if (!assertEquals(Long.valueOf(1345437), security.getBalance().getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), security.getBalance().getPrecision()))
                            return;
                        if (!assertEquals("CZK", security.getBalance().getCurrency()))
                            return;
                        if (!assertEquals("Aleš Vrba", security.getDescription()))
                            return;
                        if (!assertEquals("1034176627", security.getAccountno()))
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
        return "JSecuritiesWithIdGetTest";
    }

}
