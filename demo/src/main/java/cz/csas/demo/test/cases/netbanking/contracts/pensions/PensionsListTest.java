package cz.csas.demo.test.cases.netbanking.contracts.pensions;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.contracts.pensions.PensionsListResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class PensionsListTest extends TestCase {

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;

        mNetbankingClient.getContractsResource().getPensionsResource().list(null, new CallbackWebApi<PensionsListResponse>() {
            @Override
            public void success(PensionsListResponse pensionsListResponse) {
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
        return "PensionsListTest";
    }

}
