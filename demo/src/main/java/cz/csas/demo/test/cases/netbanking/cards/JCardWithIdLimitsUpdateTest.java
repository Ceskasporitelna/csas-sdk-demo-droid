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
public class JCardWithIdLimitsUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "cards.withId.limits.update";

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

                CardLimit newLimit = new CardLimit(CardLimitType.ATM, CardLimitPeriod.FIVE_D,
                        new Amount(1100000L, 2, "CZK"), null, null, null);
                ChangeCardLimitsRequest request = new ChangeCardLimitsRequest(Arrays.asList(newLimit), null);

                mNetbankingJudgeClient.getCardsResource().withId(cardId).getLimitsResource().update(request, new CallbackWebApi<ChangeCardLimitsResponse>() {
                    @Override
                    public void success(ChangeCardLimitsResponse cardLimitsListResponse) {
                        if (!assertEquals(SigningState.OPEN, cardLimitsListResponse.signing().getSigningState()))
                            return;
                        if (!assertEquals("1480132234", cardLimitsListResponse.signing().getSignId()))
                            return;

                        List<CardLimit> limits = cardLimitsListResponse.getLimits();
                        if (!assertEquals(3, limits.size()))
                            return;

                        for (int i = 0; i < limits.size(); ++i) {
                            CardLimit limit = limits.get(i);
                            Amount limitAmount = limit.getLimit();
                            Amount bankLimitAmount = limit.getBankLimit();

                            switch (i) {
                                case 0:
                                    if (!assertEquals(CardLimitType.ATM, limit.getLimitType()))
                                        return;
                                    if (!assertEquals(CardLimitPeriod.ONE_D, limit.getLimitPeriod()))
                                        return;
                                    if (!assertEquals(Long.valueOf(1100000), limitAmount.getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), limitAmount.getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", limitAmount.getCurrency()))
                                        return;
                                    if (!assertEquals(Long.valueOf(7000000), bankLimitAmount.getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), bankLimitAmount.getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", bankLimitAmount.getCurrency()))
                                        return;
                                    break;
                                case 1:
                                    if (!assertEquals(CardLimitType.POS, limit.getLimitType()))
                                        return;
                                    if (!assertEquals(CardLimitPeriod.ONE_D, limit.getLimitPeriod()))
                                        return;
                                    if (!assertEquals(Long.valueOf(5000000), limitAmount.getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), limitAmount.getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", limitAmount.getCurrency()))
                                        return;
                                    if (!assertEquals(Long.valueOf(50000000), bankLimitAmount.getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), bankLimitAmount.getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", bankLimitAmount.getCurrency()))
                                        return;
                                    break;
                                case 2:
                                    if (!assertEquals(CardLimitType.INTERNET, limit.getLimitType()))
                                        return;
                                    if (!assertEquals(CardLimitPeriod.ONE_D, limit.getLimitPeriod()))
                                        return;
                                    if (!assertEquals(Long.valueOf(500000), limitAmount.getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), limitAmount.getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", limitAmount.getCurrency()))
                                        return;
                                    if (!assertEquals(Long.valueOf(50000000), bankLimitAmount.getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), bankLimitAmount.getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", bankLimitAmount.getCurrency()))
                                        return;
                                    break;
                            }
                        }
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
        return "JCardWithIdLimitsUpdateTest";
    }

}
