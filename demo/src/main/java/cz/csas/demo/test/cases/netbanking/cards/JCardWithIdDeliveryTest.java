package cz.csas.demo.test.cases.netbanking.cards;

import java.util.HashMap;
import java.util.Map;

import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeRestService;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.Address;
import cz.csas.netbanking.cards.CardDelivery;
import cz.csas.netbanking.cards.CardDeliveryMode;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JCardWithIdDeliveryTest extends TestCase {

    private final String X_JUDGE_CASE = "cards.withId.delivery.get";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String cardId = "33A813886442D946122C78305EC4E482DE9F574D";
                mNetbankingJudgeClient.getCardsResource().withId(cardId).getDeliveryResource().get(new CallbackWebApi<CardDelivery>() {
                    @Override
                    public void success(CardDelivery cardDelivery) {
                        if (!assertEquals(CardDeliveryMode.BRANCH, cardDelivery.getCardDeliveryMode()))
                            return;
                        if (!assertEquals("1075", cardDelivery.getBranchId()))
                            return;

                        Address address = cardDelivery.getAddress();
                        if (!assertEquals("Antala Staška", address.getStreet()))
                            return;
                        if (!assertEquals("1292", address.getBuildingApartment()))
                            return;
                        if (!assertEquals("32", address.getStreetNumber()))
                            return;
                        if (!assertEquals("Praha", address.getCity()))
                            return;
                        if (!assertEquals("14000", address.getZipCode()))
                            return;
                        if (!assertEquals("CZ", address.getCountry()))
                            return;
                        mTestCallback.result(mTestResult);
                    }

                    @Override
                    public void failure(CsSDKError error) {
                        handleError(error);
                    }
                });
            }

            @Override
            public void failure(CsRestError error) {
                handleError(error);
            }
        });
    }

    @Override
    public String getName() {
        return "JCardWithIdDeliveryTest";
    }

}
