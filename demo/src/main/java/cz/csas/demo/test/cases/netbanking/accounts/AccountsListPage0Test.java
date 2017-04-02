package cz.csas.demo.test.cases.netbanking.accounts;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.Pagination;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.accounts.AccountsListResponse;
import cz.csas.netbanking.accounts.AccountsParameters;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class AccountsListPage0Test extends TestCase {

    private final String X_JUDGE_CASE = "accounts.list.page0";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;

        AccountsParameters parameters = new AccountsParameters(new Pagination(0, 1));

        mNetbankingClient.getAccountsResource().list(parameters, new CallbackWebApi<AccountsListResponse>() {
            @Override
            public void success(AccountsListResponse accountsListResponse) {
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
        return "AccountsListPage0Test";
    }

}
