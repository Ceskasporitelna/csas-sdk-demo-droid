package cz.csas.demo.test.cases.netbanking.contracts.insurances;

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
import cz.csas.cscore.webapi.signing.SigningState;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.contracts.insurances.RiskSportsDeactivationUpdateResponse;
import cz.csas.netbanking.contracts.insurances.RiskSportsUpdateRequest;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JInsuranceDeactivateRiskSportsTest extends TestCase {

    private final String X_JUDGE_CASE = "contracts.insurances.withId.services.deactivateRiskSports";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String id = "3961D3F9E922EEE93E2581E896B34566645FE7E3";

                RiskSportsUpdateRequest request = new RiskSportsUpdateRequest(
                        TimeUtils.getPlainDate("2016-08-16"),
                        TimeUtils.getPlainDate("2016-08-20"),
                        "602123456"
                );
                mNetbankingJudgeClient.getContractsResource().getInsurancesResource().withId(id).getServicesResource()
                        .deactivateRiskSports(request, new CallbackWebApi<RiskSportsDeactivationUpdateResponse>() {
                            @Override
                            public void success(RiskSportsDeactivationUpdateResponse riskSportsActivationUpdateResponse) {
                                if (!assertEquals(SigningState.NONE, riskSportsActivationUpdateResponse.signing().getSigningState()))
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
        return "JInsuranceDeactivateRiskSportsTest";
    }

}
