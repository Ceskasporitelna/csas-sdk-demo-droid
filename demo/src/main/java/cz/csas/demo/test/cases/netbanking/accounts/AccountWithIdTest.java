package cz.csas.demo.test.cases.netbanking.accounts;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.accounts.MainAccount;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class AccountWithIdTest extends TestCase {

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        String accountId = "4B2F9EBE742BCAE1E98A78E12F6FBC62464A74EE";

        mNetbankingClient.getAccountsResource().withId(accountId).get(new CallbackWebApi<MainAccount>() {
            @Override
            public void success(MainAccount mainAccount) {
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
        return "AccountWithIdTest";
    }

}
