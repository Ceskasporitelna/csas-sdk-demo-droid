package cz.csas.demo.test.cases.netbanking.accounts;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.Pagination;
import cz.csas.cscore.webapi.Sort;
import cz.csas.cscore.webapi.SortDirection;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.accounts.DirectDebitsListResponse;
import cz.csas.netbanking.accounts.DirectDebitsParameters;
import cz.csas.netbanking.accounts.DirectDebitsSortableFields;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class DirectDebitsListPage1Test extends TestCase {


    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;

        DirectDebitsParameters parameters = new DirectDebitsParameters(new Pagination(1, 2),
                Sort.by(DirectDebitsSortableFields.PERIOD_CYCLE, SortDirection.DESCENDING));

        String accountId = "4B2F9EBE742BCAE1E98A78E12F6FBC62464A74EE";

        mNetbankingClient.getAccountsResource().withId(accountId).getDirectDebitsResource().list(parameters, new CallbackWebApi<DirectDebitsListResponse>() {
            @Override
            public void success(DirectDebitsListResponse directDebitsListResponse) {
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
        return "DirectDebitsListPage1Test";
    }

}
