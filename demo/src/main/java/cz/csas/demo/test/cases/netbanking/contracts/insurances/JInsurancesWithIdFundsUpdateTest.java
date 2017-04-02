package cz.csas.demo.test.cases.netbanking.contracts.insurances;

import java.util.Arrays;
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
import cz.csas.netbanking.contracts.insurances.FundInfo;
import cz.csas.netbanking.contracts.insurances.FundsUpdateRequest;
import cz.csas.netbanking.contracts.insurances.FundsUpdateResponse;
import cz.csas.netbanking.contracts.insurances.InvestmentProgram;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JInsurancesWithIdFundsUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "contracts.insurances.withId.funds.update";

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
                FundsUpdateRequest request = new FundsUpdateRequest(
                        Arrays.asList(new FundInfo("31", 35d), new FundInfo("32", 65d)),
                        InvestmentProgram.INVESTMENT_MANAGEMENT);

                mNetbankingJudgeClient.getContractsResource().getInsurancesResource().withId(id).getFundsResource()
                        .update(request, new CallbackWebApi<FundsUpdateResponse>() {
                            @Override
                            public void success(FundsUpdateResponse insuranceFundsUpdateResponse) {
                                if (!assertEquals("31", insuranceFundsUpdateResponse.getFunds().get(0).getCode()))
                                    return;
                                if (!assertEquals(Double.valueOf(35), insuranceFundsUpdateResponse.getFunds().get(0).getAllocation()))
                                    return;
                                if (!assertEquals("32", insuranceFundsUpdateResponse.getFunds().get(1).getCode()))
                                    return;
                                if (!assertEquals(Double.valueOf(65), insuranceFundsUpdateResponse.getFunds().get(1).getAllocation()))
                                    return;
                                if (!assertEquals(InvestmentProgram.INVESTMENT_MANAGEMENT, insuranceFundsUpdateResponse.getInvestmentProgram()))
                                    return;
                                if (!assertEquals(SigningState.OPEN, insuranceFundsUpdateResponse.signing().getSigningState()))
                                    return;
                                if (!assertEquals("151112531008724", insuranceFundsUpdateResponse.signing().getSignId()))
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
        return "JInsurancesWithIdFundsUpdateTest";
    }

}
