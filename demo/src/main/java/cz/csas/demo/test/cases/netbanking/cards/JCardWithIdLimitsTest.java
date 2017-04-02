package cz.csas.demo.test.cases.netbanking.cards;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeRestService;
import cz.csas.cscore.webapi.signing.SigningObject;
import cz.csas.cscore.webapi.signing.SigningState;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.Address;
import cz.csas.netbanking.Language;
import cz.csas.netbanking.cards.CardDeliveryMode;
import cz.csas.netbanking.cards.ChangeCardDeliverySettingsRequest;
import cz.csas.netbanking.cards.ChangeCardDeliverySettingsResponse;
import cz.csas.netbanking.cards.Confirmation;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JCardWithIdLimitsTest extends TestCase {

    private final String X_JUDGE_CASE = "cards.withId.delivery.update";

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

                ChangeCardDeliverySettingsRequest request = new ChangeCardDeliverySettingsRequest(CardDeliveryMode.BRANCH,
                        Arrays.asList(new Confirmation("john.doe@test.com", Language.CS)));

                mNetbankingJudgeClient.getCardsResource().withId(cardId).getDeliveryResource().update(request,
                        new CallbackWebApi<ChangeCardDeliverySettingsResponse>() {
                            @Override
                            public void success(ChangeCardDeliverySettingsResponse response) {
                                
                                if (!assertEquals(CardDeliveryMode.BRANCH, response.getCardDeliveryMode()))
                                    return;
                                if (!assertEquals("1075", response.getBranchId()))
                                    return;

                                Address address = response.getAddress();
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

                                List<Confirmation> confirmations = response.getConfirmations();
                                Confirmation confirmation = confirmations.get(0);
                                if (!assertEquals("john.doe@test.com", confirmation.getEmail()))
                                    return;
                                if (!assertEquals(Language.CS, confirmation.getLanguage()))
                                    return;

                                SigningObject signingObject = response.signing();
                                if (!assertEquals(SigningState.OPEN, signingObject.getSigningState()))
                                    return;
                                if (!assertEquals("151112531008554", signingObject.getSignId()))
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
        return "JCardWithIdLimitsTest";
    }

}
