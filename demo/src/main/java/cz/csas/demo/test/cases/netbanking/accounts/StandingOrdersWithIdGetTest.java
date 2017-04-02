package cz.csas.demo.test.cases.netbanking.accounts;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.Sort;
import cz.csas.cscore.webapi.SortDirection;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.accounts.StandingOrder;
import cz.csas.netbanking.accounts.StandingOrdersParameters;
import cz.csas.netbanking.accounts.StandingOrdersSortableFields;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class StandingOrdersWithIdGetTest extends TestCase {

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;

        StandingOrdersParameters parameters = new StandingOrdersParameters(null,
                Sort.by(StandingOrdersSortableFields.NEXT_EXECUTION_DATE, SortDirection.DESCENDING));

        String accountId = "4B2F9EBE742BCAE1E98A78E12F6FBC62464A74EE";
        String orderId = "1";

        mNetbankingClient.getAccountsResource().withId(accountId).getStandingOrdersResource().withId(orderId).get(new CallbackWebApi<StandingOrder>() {
            @Override
            public void success(StandingOrder standingOrder) {
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
        return "StandingOrdersWithIdGetTest";
    }

}
