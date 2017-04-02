package cz.csas.demo.test.cases.netbanking.accounts;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.Sort;
import cz.csas.cscore.webapi.SortDirection;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.StatementsListResponse;
import cz.csas.netbanking.StatementsParameters;
import cz.csas.netbanking.StatementsSortableFields;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class AccountWithIdSubAccountStatementsListTest extends TestCase {

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;

        StatementsParameters parameters = new StatementsParameters(null,
                Sort.by(StatementsSortableFields.STATEMENT_DATE, SortDirection.ASCENDING));

        String accountId = "4B2F9EBE742BCAE1E98A78E12F6FBC62464A74EE";
        String subAccountId = "932933BABDE2A94753BAFF7FF146BA69BA90C259";

        mNetbankingClient.getAccountsResource().withId(accountId).getSubAccountsResource().withId(subAccountId).getStatementsResource().list(parameters, new CallbackWebApi<StatementsListResponse>() {
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
        return "AccountWithIdSubAccountStatementsListTest";
    }

}
