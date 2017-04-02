package cz.csas.demo.test.cases.netbanking.contracts.insurances;

import java.util.ArrayList;
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
import cz.csas.netbanking.contracts.insurances.Fund;
import cz.csas.netbanking.contracts.insurances.FundsListResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JInsurancesWithIdFundsGetTest extends TestCase {

    private final String X_JUDGE_CASE = "contracts.insurances.withId.funds.get";

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
                mNetbankingJudgeClient.getContractsResource().getInsurancesResource().withId(id).getFundsResource().list(new CallbackWebApi<FundsListResponse>() {
                    @Override
                    public void success(FundsListResponse insuranceFundsListResponse) {
                        List<String> flags = new ArrayList<String>();
                        flags.add("fundsChangeAllowed");
                        if (!assertEquals(Long.valueOf(0), insuranceFundsListResponse.getTotalInvestedAmount().getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), insuranceFundsListResponse.getTotalInvestedAmount().getPrecision()))
                            return;
                        if (!assertEquals("CZK", insuranceFundsListResponse.getTotalInvestedAmount().getCurrency()))
                            return;
                        if (!assertEquals(flags, insuranceFundsListResponse.getFlags()))
                            return;

                        List<Fund> funds = insuranceFundsListResponse.getFunds();
                        if (!assertEquals(1, funds.size()))
                            return;
                        Fund fund = funds.get(0);

                        if (!assertEquals("24", fund.getCode()))
                            return;
                        if (!assertEquals("Garantovaný fond pro běžné pojistné", fund.getName()))
                            return;
                        if (!assertEquals(Long.valueOf(0), fund.getInvestedAmount().getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), fund.getInvestedAmount().getPrecision()))
                            return;
                        if (!assertEquals("CZK", fund.getInvestedAmount().getCurrency()))
                            return;
                        if (!assertEquals(Double.valueOf(0), fund.getInvestedShare()))
                            return;
                        if (!assertEquals(Double.valueOf(100), fund.getAllocation()))
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
        return "JInsurancesWithIdFundsGetTest";
    }

}
