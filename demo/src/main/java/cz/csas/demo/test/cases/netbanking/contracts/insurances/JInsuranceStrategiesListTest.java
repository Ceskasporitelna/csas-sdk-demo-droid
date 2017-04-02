package cz.csas.demo.test.cases.netbanking.contracts.insurances;

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
import cz.csas.netbanking.contracts.insurances.ContractStrategiesListResponse;
import cz.csas.netbanking.contracts.insurances.ContractStrategy;
import cz.csas.netbanking.contracts.insurances.ContractStrategyGroup;
import cz.csas.netbanking.contracts.insurances.ContractStrategyType;
import cz.csas.netbanking.contracts.insurances.StrategyFund;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JInsuranceStrategiesListTest extends TestCase {

    private final String X_JUDGE_CASE = "contracts.insurances.withId.strategies.list";

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

                mNetbankingJudgeClient.getContractsResource().getInsurancesResource().withId(id).getStrategiesResource()
                        .list(new CallbackWebApi<ContractStrategiesListResponse>() {
                            @Override
                            public void success(ContractStrategiesListResponse strategiesListResponse) {
                                List<ContractStrategy> strategies = strategiesListResponse.getStrategies();
                                if (!assertEquals(1, strategies.size()))
                                    return;

                                ContractStrategy strategy = strategies.get(0);
                                if (!assertEquals(ContractStrategyType.ACTUAL_SETTING, strategy.getType()))
                                    return;
                                if (!assertEquals(ContractStrategyGroup.STRATEGY, strategy.getGroup()))
                                    return;
                                List<StrategyFund> funds = strategy.getFunds();
                                StrategyFund fund = funds.get(0);

                                if (!assertEquals("5", fund.getCode()))
                                    return;
                                if (!assertEquals("PČS fond akciový", fund.getName()))
                                    return;
                                if (!assertEquals(Double.valueOf(0), fund.getShare()))
                                    return;
                                if (!assertEquals("EDITABLE", fund.getChangeType()))
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
        return "JInsuranceStrategiesListTest";
    }

}
