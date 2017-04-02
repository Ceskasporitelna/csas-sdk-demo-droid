package cz.csas.demo.test.cases.netbanking.cards;

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
public class CardAccountsStatementsListPage0Test extends TestCase {

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;

        StatementsParameters parameters = new StatementsParameters(new Pagination(0, 1), null);

        String cardId = "FAFBFBDCAE6465F6DB8058746A828E195922CB15";
        String accountId = "4B2F9EBE742BCAE1E98A78E12F6FBC62464A74EE";

        mNetbankingClient.getCardsResource().withId(cardId).getAccountsResource().withId(accountId).getStatements().list(parameters, new CallbackWebApi<StatementsListResponse>() {
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
        return "CardAccountsStatementsListPage0Test";
    }

}
