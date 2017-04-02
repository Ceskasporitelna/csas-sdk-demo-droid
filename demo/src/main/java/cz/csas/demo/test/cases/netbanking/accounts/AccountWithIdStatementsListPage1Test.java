package cz.csas.demo.test.cases.netbanking.accounts;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.Pagination;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.StatementsListResponse;
import cz.csas.netbanking.StatementsParameters;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class AccountWithIdStatementsListPage1Test extends TestCase {

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;

        StatementsParameters parameters = new StatementsParameters(new Pagination(1, 1),
                null);
        String accountId = "4B2F9EBE742BCAE1E98A78E12F6FBC62464A74EE";

        mNetbankingClient.getAccountsResource().withId(accountId).getStatementsResource().list(parameters, new CallbackWebApi<StatementsListResponse>() {
            @Override
            public void success(StatementsListResponse statementsListResponse) {
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
        return "AccountWithIdStatementsListPage1Test";
    }

}
