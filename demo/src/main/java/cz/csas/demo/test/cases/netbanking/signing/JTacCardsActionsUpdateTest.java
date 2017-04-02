package cz.csas.demo.test.cases.netbanking.signing;

import java.util.ArrayList;
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
import cz.csas.cscore.webapi.signing.AuthorizationType;
import cz.csas.cscore.webapi.signing.FilledSigningObject;
import cz.csas.cscore.webapi.signing.SigningObject;
import cz.csas.cscore.webapi.signing.SigningState;
import cz.csas.cscore.webapi.signing.TacSigningProcess;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.cards.CardAction;
import cz.csas.netbanking.cards.CardActionRequest;
import cz.csas.netbanking.cards.CardActionResponse;


/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JTacCardsActionsUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "signing.tac.cards.actions.update";
    private CardActionResponse mResponse;

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);
                String cardId = "3FB37388FC58076DEAD3DE282E075592A299B596";
                CardActionRequest request = new CardActionRequest(CardAction.ACTIVATE_CARD);
                mNetbankingJudgeClient.getCardsResource().withId(cardId).getActionsResource().update(request, new CallbackWebApi<CardActionResponse>() {
                    @Override
                    public void success(CardActionResponse cardActionResponse) {
                        mResponse = cardActionResponse;

                        SigningObject signingObject = mResponse.signing();
                        if (!assertEquals(SigningState.OPEN, signingObject.getSigningState()))
                            return;
                        if (!assertEquals("1607878324", signingObject.getSignId()))
                            return;
                        callSigningGET(signingObject);
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
        return "JTacCardsActionsUpdateTest";
    }


    private void callSigningGET(SigningObject signingObject) {
        signingObject.getInfo(new CallbackWebApi<FilledSigningObject>() {
            @Override
            public void success(FilledSigningObject filledSigningObject) {
                if (!assertEquals(AuthorizationType.TAC, filledSigningObject.getAuthorizationType()))
                    return;
                List<List<AuthorizationType>> scenarios = new ArrayList<List<AuthorizationType>>();
                List<AuthorizationType> scenario = new ArrayList<AuthorizationType>();
                scenario.add(AuthorizationType.TAC);
                scenarios.add(scenario);
                if (!assertEquals(scenarios, filledSigningObject.getScenarios()))
                    return;
                if (!assertEquals(SigningState.OPEN, filledSigningObject.getSigningState()))
                    return;
                if (!assertEquals("1607878324", filledSigningObject.getSignId()))
                    return;
                callSigningPOST(filledSigningObject);
            }

            @Override
            public void failure(CsSDKError error) {
                handleError(error);
            }
        });
    }

    private void callSigningPOST(FilledSigningObject filledSigningObject) {
        filledSigningObject.startSigningWithTac(new CallbackWebApi<TacSigningProcess>() {
            @Override
            public void success(TacSigningProcess tacSigningProcess) {
                if (!assertNotNull(tacSigningProcess, TacSigningProcess.class.getName()))
                    return;
                callSigningPUT(tacSigningProcess);

            }

            @Override
            public void failure(CsSDKError error) {
                handleError(error);
            }
        });
    }

    private void callSigningPUT(TacSigningProcess tacSigningProcess) {
        tacSigningProcess.finishSigning("00000000", new CallbackWebApi<SigningObject>() {
            @Override
            public void success(SigningObject signingObject) {
                if (!assertEquals("1607878324", signingObject.getSignId()))
                    return;
                if (!assertEquals(SigningState.DONE, signingObject.getSigningState()))
                    return;
                if (!assertEquals(SigningState.DONE, mResponse.signing().getSigningState()))
                    return;
                mTestCallback.result(mTestResult);

            }

            @Override
            public void failure(CsSDKError error) {
                handleError(error);
            }
        });
    }
}
