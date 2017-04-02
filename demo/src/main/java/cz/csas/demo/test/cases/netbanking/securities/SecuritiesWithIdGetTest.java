package cz.csas.demo.test.cases.netbanking.securities;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.securities.Security;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class SecuritiesWithIdGetTest extends TestCase {

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;

        String id = "420A817C20E4814C7C516A53ABA8E78F0CDBE324";

        mNetbankingClient.getSecuritiesResource().withId(id).get(new CallbackWebApi<Security>() {
            @Override
            public void success(Security security) {
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
        return "SecuritiesWithIdGetTest";
    }

}
