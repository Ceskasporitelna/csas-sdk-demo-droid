package cz.csas.demo.test.cases.netbanking.contracts.insurances;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.PaginatedParameters;
import cz.csas.cscore.webapi.Pagination;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.contracts.insurances.InsuranceListResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class InsurancesListPage1Test extends TestCase {

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;

        mNetbankingClient.getContractsResource().getInsurancesResource().list(new PaginatedParameters(new Pagination(1, 1)), new CallbackWebApi<InsuranceListResponse>() {
            @Override
            public void success(InsuranceListResponse insuranceListResponse) {
                mTestResult.setStatus(TestStatus.OK);
                mTestCallback.result(mTestResult);
            }

            @Override
            public void failure(CsSDKError error) {
                handleError(error);
            }
        });
    }

    @Override
    public String getName() {
        return "InsurancesListPage1Test";
    }

}
