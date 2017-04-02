package cz.csas.demo.test.cases.netbanking.settings;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.settings.Settings;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class SettingsGetTest extends TestCase {

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;

        mNetbankingClient.getSettingsResource().get(new CallbackWebApi<Settings>() {
            @Override
            public void success(Settings settings) {
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
        return "SettingsGetTest";
    }

}
