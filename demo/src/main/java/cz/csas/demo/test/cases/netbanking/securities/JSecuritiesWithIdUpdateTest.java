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
import cz.csas.cscore.webapi.signing.SigningState;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.securities.SecurityUpdateRequest;
import cz.csas.netbanking.securities.SecurityUpdateResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JSecuritiesWithIdUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "securities.withId.update";

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
                        final String alias = "lorem";
                        mNetbankingJudgeClient.getSecuritiesResource().withId(id).update(new SecurityUpdateRequest(alias), new CallbackWebApi<SecurityUpdateResponse>() {
                            @Override
                            public void success(SecurityUpdateResponse signedSecuritiesAccount) {
                                SecurityUpdateResponse account = signedSecuritiesAccount;
                                if (!assertEquals("420A817C20E4814C7C516A53ABA8E78F0CDBE324", account.getId()))
                                    return;
                                if (!assertEquals(Long.valueOf(1345437), account.getBalance().getValue()))
                                    return;
                                if (!assertEquals(Integer.valueOf(2), account.getBalance().getPrecision()))
                                    return;
                                if (!assertEquals("CZK", account.getBalance().getCurrency()))
                                    return;
                                if (!assertEquals("Aleš Vrba", account.getDescription()))
                                    return;
                                if (!assertEquals("1034176627", account.getAccountno()))
                                    return;
                                if (!assertEquals(alias, account.getAlias()))
                                    return;
                                if (!assertEquals(SigningState.NONE, account.signing().getSigningState()))
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
                }

        );
    }

    @Override
    public String getName() {
        return "JSecuritiesWithIdUpdateTest";
    }

}
