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
import cz.csas.cscore.webapi.signing.MobileCaseSigningProcess;
import cz.csas.cscore.webapi.signing.SigningObject;
import cz.csas.cscore.webapi.signing.SigningState;
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
public class JCaseMobileCardLimitsUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "signing.caseMobile.card.limits.update";
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
                String cardId = "33A813886442D946122C78305EC4E482DE9F574D";
                ChangeCardLimitsRequest request = new ChangeCardLimitsRequest(Arrays.asList(
                        new CardLimit(CardLimitType.ATM, CardLimitPeriod.FIVE_D, new Amount(1100000L, 2, "CZK"), null, null, null)), null);
                mNetbankingJudgeClient.getCardsResource().withId(cardId).getLimitsResource().update(request, new CallbackWebApi<ChangeCardLimitsResponse>() {
                    @Override
                    public void success(final ChangeCardLimitsResponse changeCardLimitsResponse) {
                        mResponse = changeCardLimitsResponse;
                        SigningObject signingObject = changeCardLimitsResponse.signing();
                        if (!assertEquals(SigningState.OPEN, signingObject.getSigningState()))
                            return;
                        if (!assertEquals("1445623889", signingObject.getSignId()))
                            return;

                        List<CardLimit> limits = changeCardLimitsResponse.getLimits();
                        if (!assertEquals(3, limits.size()))
                            return;
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
        return "JCaseMobileCardLimitsUpdateTest";
    }


    private void callSigningGET(SigningObject signingObject) {
        signingObject.getInfo(new CallbackWebApi<FilledSigningObject>() {
            @Override
            public void success(FilledSigningObject filledSigningObject) {
                if (!assertEquals(AuthorizationType.MOBILE_CASE, filledSigningObject.getAuthorizationType()))
                    return;
                List<List<AuthorizationType>> scenarios = new ArrayList<List<AuthorizationType>>();
                List<AuthorizationType> scenario = new ArrayList<AuthorizationType>();
                scenario.add(AuthorizationType.MOBILE_CASE);
                scenarios.add(scenario);
                if (!assertEquals(scenarios, filledSigningObject.getScenarios()))
                    return;
                if (!assertEquals(SigningState.OPEN, filledSigningObject.getSigningState()))
                    return;
                if (!assertEquals("1445623889", filledSigningObject.getSignId()))
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
        filledSigningObject.startSigningWithMobileCase(new CallbackWebApi<MobileCaseSigningProcess>() {
            @Override
            public void success(MobileCaseSigningProcess mobileCaseSigningProcess) {
                if (!assertNotNull(mobileCaseSigningProcess, MobileCaseSigningProcess.class.getName()))
                    return;
                callSigningPUT(mobileCaseSigningProcess);

            }

            @Override
            public void failure(CsSDKError error) {
                handleError(error);
            }
        });
    }

    private void callSigningPUT(MobileCaseSigningProcess mobileCaseSigningProcess) {
        mobileCaseSigningProcess.finishSigning("000000", new CallbackWebApi<SigningObject>() {
            @Override
            public void success(SigningObject signingObject) {
                if (!assertEquals("1445623889", signingObject.getSignId()))
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
