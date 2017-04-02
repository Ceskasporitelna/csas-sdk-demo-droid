package cz.csas.demo.test.cases.netbanking.accounts;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.Sort;
import cz.csas.cscore.webapi.SortDirection;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.accounts.AccountsListResponse;
import cz.csas.netbanking.accounts.AccountsParameters;
import cz.csas.netbanking.accounts.AccountsSortableFields;
import cz.csas.netbanking.accounts.ProductType;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class AccountsListTest extends TestCase {

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;

        AccountsParameters parameters = new AccountsParameters(null,
                Sort.by(AccountsSortableFields.IBAN, SortDirection.ASCENDING)
                        .thenBy(AccountsSortableFields.BALANCE, SortDirection.DESCENDING), ProductType.CURRENT);

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
        return "AccountsListTest";
    }

}
