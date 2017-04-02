package cz.csas.demo.test.cases.netbanking.signing;

import java.util.ArrayList;
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
import cz.csas.cscore.webapi.signing.AuthorizationType;
import cz.csas.cscore.webapi.signing.FilledSigningObject;
import cz.csas.cscore.webapi.signing.SigningObject;
import cz.csas.cscore.webapi.signing.SigningState;
import cz.csas.cscore.webapi.signing.TacSigningProcess;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.cards.CardLimit;
import cz.csas.netbanking.cards.CardLimitPeriod;
import cz.csas.netbanking.cards.CardLimitType;
import cz.csas.netbanking.cards.ChangeCardLimitsRequest;
import cz.csas.netbanking.cards.ChangeCardLimitsResponse;


/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JTacCardsLimitsUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "signing.tac.cards.limits.update";
    private ChangeCardLimitsResponse mResponse;

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
                ChangeCardLimitsRequest request = new ChangeCardLimitsRequest(Arrays.asList(
                        new CardLimit(CardLimitType.ATM, CardLimitPeriod.FIVE_D, new Amount(1100000L, 2, "CZK"), null, null, null)), null);
                mNetbankingJudgeClient.getCardsResource().withId(cardId).getLimitsResource().update(request, new CallbackWebApi<ChangeCardLimitsResponse>() {
                    @Override
                    public void success(ChangeCardLimitsResponse changeCardLimitsResponse) {
                        mResponse = changeCardLimitsResponse;

                        SigningObject signingObject = mResponse.signing();
                        if (!assertEquals(SigningState.OPEN, signingObject.getSigningState()))
                            return;
                        if (!assertEquals("160429968745872", signingObject.getSignId()))
                            return;

                        List<CardLimit> limits = mResponse.getLimits();
                        if (!assertEquals(3, limits.size()))

                            for (CardLimit limit : limits) {
                                switch (limit.getLimitType()) {
                                    case ATM:
                                        if (!assertEquals(CardLimitPeriod.ONE_D, limit.getLimitPeriod()))
                                            return;
                                        if (!assertEquals(Long.valueOf(1100000), limit.getLimit().getValue()))
                                            return;
                                        if (!assertEquals(Integer.valueOf(2), limit.getLimit().getPrecision()))
                                            return;
                                        if (!assertEquals("CZK", limit.getLimit().getCurrency()))
                                            return;
                                        if (!assertEquals(Long.valueOf(7000000), limit.getBankLimit().getValue()))
                                            return;
                                        if (!assertEquals(Integer.valueOf(2), limit.getBankLimit().getPrecision()))
                                            return;
                                        if (!assertEquals("CZK", limit.getBankLimit().getCurrency()))
                                            return;
                                        break;
                                    case POS:
                                        if (!assertEquals(CardLimitPeriod.ONE_D, limit.getLimitPeriod()))
                                            return;
                                        if (!assertEquals(Long.valueOf(5000000), limit.getLimit().getValue()))
                                            return;
                                        if (!assertEquals(Integer.valueOf(2), limit.getLimit().getPrecision()))
                                            return;
                                        if (!assertEquals("CZK", limit.getLimit().getCurrency()))
                                            return;
                                        if (!assertEquals(Long.valueOf(50000000), limit.getBankLimit().getValue()))
                                            return;
                                        if (!assertEquals(Integer.valueOf(2), limit.getBankLimit().getPrecision()))
                                            return;
                                        if (!assertEquals("CZK", limit.getBankLimit().getCurrency()))
                                            return;
                                        break;
                                    case INTERNET:
                                        if (!assertEquals(CardLimitPeriod.ONE_D, limit.getLimitPeriod()))
                                            return;
                                        if (!assertEquals(Long.valueOf(500000), limit.getLimit().getValue()))
                                            return;
                                        if (!assertEquals(Integer.valueOf(2), limit.getLimit().getPrecision()))
                                            return;
                                        if (!assertEquals("CZK", limit.getLimit().getCurrency()))
                                            return;
                                        if (!assertEquals(Long.valueOf(50000000), limit.getBankLimit().getValue()))
                                            return;
                                        if (!assertEquals(Integer.valueOf(2), limit.getBankLimit().getPrecision()))
                                            return;
                                        if (!assertEquals("CZK", limit.getBankLimit().getCurrency()))
                                            return;
                                        break;
                                }
                            }
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
        return "JTacCardsLimitsUpdateTest";
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
                if (!assertEquals("160429968745872", filledSigningObject.getSignId()))
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
                if (!assertEquals("160429968745872", signingObject.getSignId()))
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
