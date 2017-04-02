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
import cz.csas.cscore.webapi.signing.SigningState;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.contracts.insurances.InsuranceTransferType;
import cz.csas.netbanking.contracts.insurances.InsuranceTransferUpdateRequest;
import cz.csas.netbanking.contracts.insurances.InsuranceTransferUpdateResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JInsuranceTransferUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "contracts.insurances.withId.transfer.update";

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

                InsuranceTransferUpdateRequest request = new InsuranceTransferUpdateRequest(
                        InsuranceTransferType.PAY_PREMIUM,
                        new Amount(1500l, 2, "CZK"),
                        new AccountNumber("2723000003", "0800")
                );

                mNetbankingJudgeClient.getContractsResource().getInsurancesResource().withId(id).getTransferResource()
                        .update(request, new CallbackWebApi<InsuranceTransferUpdateResponse>() {
                            @Override
                            public void success(InsuranceTransferUpdateResponse insuranceTransferUpdateResponse) {

                                if (!assertEquals(SigningState.OPEN, insuranceTransferUpdateResponse.signing().getSigningState()))
                                    return;
                                if (!assertEquals("160815138818340", insuranceTransferUpdateResponse.signing().getSignId()))
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
        return "JInsuranceTransferUpdateTest";
    }

}
