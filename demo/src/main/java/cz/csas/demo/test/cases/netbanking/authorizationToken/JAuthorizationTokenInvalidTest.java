package cz.csas.demo.test.cases.netbanking.authorizationToken;

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
import cz.csas.netbanking.accounts.AccountsListResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JAuthorizationTokenInvalidTest extends TestCase {

    private final String X_JUDGE_CASE = "authorizationToken.invalid";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                headers.put("authorization", "bearer cn389ncoiwuencr");
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                mNetbankingJudgeClient.getAccountsResource().list(null, new CallbackWebApi<AccountsListResponse>() {
                    @Override
                    public void success(AccountsListResponse accountsListResponse) {
                        assertNull(accountsListResponse, "accountsListResponse");
                        mTestCallback.result(mTestResult);
                    }

                    @Override
                    public void failure(CsSDKError error) {
                        if (!assertEquals(CsRestError.class, error.getClass()))
                            return;
                        if (!assertEquals(403, ((CsRestError) error).getResponse().getStatus()))
                            return;
                        mTestCallback.result(mTestResult);
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
        return "JAuthorizationTokenInvalidTest";
    }

}
