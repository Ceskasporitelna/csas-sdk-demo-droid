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
import cz.csas.netbanking.contracts.insurances.InsuranceService;
import cz.csas.netbanking.contracts.insurances.InsuranceServiceState;
import cz.csas.netbanking.contracts.insurances.InsuranceServicesListResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JInsuranceServicesListTest extends TestCase {

    private final String X_JUDGE_CASE = "contracts.insurances.withId.services.list";

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

                mNetbankingJudgeClient.getContractsResource().getInsurancesResource().withId(id).getServicesResource().list(new CallbackWebApi<InsuranceServicesListResponse>() {
                    @Override
                    public void success(InsuranceServicesListResponse response) {
                        List<InsuranceService> services = response.getServices();
                        if (!assertEquals(1, services.size()))
                            return;
                        InsuranceService service = services.get(0);

                        if (!assertEquals("1", service.getId()))
                            return;
                        if (!assertEquals("RISK_SPORTS", service.getGroup()))
                            return;
                        if (!assertEquals("RISK_SPORTS", service.getIconGroup()))
                            return;
                        if (!assertEquals(Double.valueOf(30), service.getAvailableDays()))
                            return;
                        if (!assertEquals(InsuranceServiceState.INACTIVE, service.getState()))
                            return;
                        if (!assertEquals("Rizikové sporty", service.getNameI18N()))
                            return;
                        if (!assertEquals("Zdarma pojištění libovolných 30 dnů v roce na aktivity jako horolezectví, bungee jumping, potápění nebo rafting. Stačí si jej aktivovat 2 hodiny před plánovanou aktivitou.",
                                service.getDescriptionI18N()))
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
        return "JInsuranceServicesListTest";
    }

}
