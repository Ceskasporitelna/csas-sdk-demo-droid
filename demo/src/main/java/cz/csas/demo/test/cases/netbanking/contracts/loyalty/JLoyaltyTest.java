package cz.csas.demo.test.cases.netbanking.contracts.loyalty;

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
import cz.csas.netbanking.contracts.loyalty.Loyalty;
import cz.csas.netbanking.contracts.loyalty.LoyaltyState;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JLoyaltyTest extends TestCase {

    private final String X_JUDGE_CASE = "contracts.loyalty.get";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                mNetbankingJudgeClient.getContractsResource().getLoyaltyResource().get(new CallbackWebApi<Loyalty>() {
                    @Override
                    public void success(Loyalty loyalty) {
                        if (!assertEquals(TimeUtils.getISO8601Date("2016-05-31T00:00:00+02:00"), loyalty.getExportDate()))
                            return;
                        if (!assertEquals(LoyaltyState.UNREGISTERED, loyalty.getState()))
                            return;
                        if (!assertEquals(Double.valueOf(0), loyalty.getPointsCount()))
                            return;
                        if (!assertEquals("15B8FE1760", loyalty.getActivationCode()))
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
        return "JLoyaltyTest";
    }

}
