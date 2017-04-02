package cz.csas.demo.test.cases.netbanking.accounts;

import java.util.HashMap;
import java.util.Map;

import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeRestService;
import cz.csas.cscore.utils.TimeUtils;
import cz.csas.cscore.webapi.signing.SigningState;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.accounts.DirectDebitCreateRequest;
import cz.csas.netbanking.accounts.DirectDebitResponse;
import cz.csas.netbanking.accounts.DirectDebitType;
import cz.csas.netbanking.accounts.PeriodCycle;
import cz.csas.netbanking.orders.Symbols;

/**
 * @author Frantisek Kratochvil <kratochvilf@gmail.com>
 * @since 01.09.16.
 */
public class JDirectDebitsWithIdCreateTest extends TestCase {
    private final String X_JUDGE_CASE = "accounts.withId.directDebts.create";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String accountId = "4B2F9EBE742BCAE1E98A78E12F6FBC62464A74EE";

                DirectDebitCreateRequest request = new DirectDebitCreateRequest.Builder()
                        .setType(DirectDebitType.DIRECT_DEBIT)
                        .setReceiver(new AccountNumber("428602109", "0800"))
                        .setAlias("moje inkaso")
                        .setPeriodicity(1)
                        .setPeriodCycle(PeriodCycle.MONTHLY)
                        .setLimit(new Amount(100000l, 2, "CZK"))
                        .setLimitSum(new Amount(300000l, 2, "CZK"))
                        .setNumberLimit(5)
                        .setStartDate(TimeUtils.getPlainDate("2017-07-14"))
                        .setEndDate(TimeUtils.getPlainDate("2018-07-14"))
                        .setSymbols(new Symbols("4567", null, "800"))
                        .build();

                mNetbankingJudgeClient.getAccountsResource().withId(accountId).getDirectDebitsResource()
                        .create(request, new CallbackWebApi<DirectDebitResponse>() {
                            @Override
                            public void success(DirectDebitResponse directDebit) {

                                DirectDebitResponse order = directDebit;
                                AccountNumber receiver = order.getReceiver();
                                Symbols symbols = order.getSymbols();
                                Amount limit = order.getLimit();
                                Amount limitSum = order.getLimitSum();

                                if (!assertEquals("428602109", receiver.getNumber()))
                                    return;
                                if (!assertEquals("0800", receiver.getBankCode()))
                                    return;

                                if (!assertEquals(DirectDebitType.DIRECT_DEBIT, order.getType()))
                                    return;
                                if (!assertEquals("moje inkaso", order.getAlias()))
                                    return;
                                if (!assertEquals(Integer.valueOf(1), order.getPeriodicity()))
                                    return;
                                if (!assertEquals(PeriodCycle.MONTHLY, order.getPeriodCycle()))
                                    return;

                                if (!assertEquals(Long.valueOf(100000l), limit.getValue()))
                                    return;
                                if (!assertEquals(Integer.valueOf(2), limit.getPrecision()))
                                    return;
                                if (!assertEquals("CZK", limit.getCurrency()))
                                    return;

                                if (!assertEquals(Long.valueOf(300000l), limitSum.getValue()))
                                    return;
                                if (!assertEquals(Integer.valueOf(2), limitSum.getPrecision()))
                                    return;
                                if (!assertEquals("CZK", limitSum.getCurrency()))
                                    return;

                                if (!assertEquals(TimeUtils.getPlainDate("2017-07-14"), order.getStartDate()))
                                    return;
                                if (!assertEquals(TimeUtils.getPlainDate("2018-07-14"), order.getEndDate()))
                                    return;

                                if (!assertEquals("4567", symbols.getVariableSymbol()))
                                    return;
                                if (!assertEquals("800", symbols.getSpecificSymbol()))
                                    return;

                                if (!assertEquals(SigningState.OPEN, order.signing().getSigningState()))
                                    return;
                                if (!assertEquals("160530104689642", order.signing().getSignId()))
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
        return "JDirectDebitsWithIdCreateTest";
    }

}
