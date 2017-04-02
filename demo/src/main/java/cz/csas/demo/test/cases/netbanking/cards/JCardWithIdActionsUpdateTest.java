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
import cz.csas.cscore.webapi.signing.SigningState;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.cards.CardAction;
import cz.csas.netbanking.cards.CardActionRequest;
import cz.csas.netbanking.cards.CardActionResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JCardWithIdActionsUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "cards.withId.actions.update";

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

                CardActionRequest request = new CardActionRequest(CardAction.ACTIVATE_CARD);

                mNetbankingJudgeClient.getCardsResource().withId(cardId).getActionsResource().update(request, new CallbackWebApi<CardActionResponse>() {
                    @Override
                    public void success(CardActionResponse cardActionResponse) {
                        if (!assertEquals(SigningState.OPEN, cardActionResponse.signing().getSigningState()))
                            return;
                        if (!assertEquals("1883293134", cardActionResponse.signing().getSignId()))
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
        return "JCardWithIdActionsUpdateTest";
    }

}
