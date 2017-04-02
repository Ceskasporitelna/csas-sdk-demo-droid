package cz.csas.demo.test.cases.netbanking.cards;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.Pagination;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.cards.CardsListResponse;
import cz.csas.netbanking.cards.CardsParameters;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class CardsListPage1Test extends TestCase {

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;

        CardsParameters parameters = new CardsParameters(new Pagination(1, 1), null);

        mNetbankingClient.getCardsResource().list(parameters, new CallbackWebApi<CardsListResponse>() {
            @Override
            public void success(CardsListResponse cardsListResponse) {
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
        return "CardsListPage1Test";
    }

}
