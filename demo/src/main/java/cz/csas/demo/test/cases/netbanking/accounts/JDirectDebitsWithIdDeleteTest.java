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
import cz.csas.netbanking.accounts.DirectDebitResponse;
import cz.csas.netbanking.accounts.DirectDebitType;
import cz.csas.netbanking.accounts.PeriodCycle;
import cz.csas.netbanking.orders.Symbols;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JDirectDebitsWithIdDeleteTest extends TestCase {

    private final String X_JUDGE_CASE = "accounts.withId.directDebts.withId.delete";

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
                String orderId = "4";

                mNetbankingJudgeClient.getAccountsResource().withId(accountId).getDirectDebitsResource()
                        .withId(orderId).delete(new CallbackWebApi<DirectDebitResponse>() {
                    @Override
                    public void success(DirectDebitResponse directDebit) {
                        DirectDebitResponse order = directDebit;
                        AccountNumber receiver = order.getReceiver();
                        Symbols symbols = order.getSymbols();
                        Amount limitSum = order.getLimitSum();

                        if (!assertEquals("4", order.getNumber()))
                            return;
                        if (!assertEquals("101082201", receiver.getNumber()))
                            return;
                        if (!assertEquals("0800", receiver.getBankCode()))
                            return;
                        if (!assertEquals("CZ", receiver.getCountryCode()))
                            return;

                        if (!assertEquals(DirectDebitType.SIPO, order.getType()))
                            return;
                        if (!assertEquals(Integer.valueOf(1), order.getPeriodicity()))
                            return;
                        if (!assertEquals(PeriodCycle.MONTHLY, order.getPeriodCycle()))
                            return;

                        if (!assertEquals(Long.valueOf(99999999900l), limitSum.getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), limitSum.getPrecision()))
                            return;
                        if (!assertEquals("CZK", limitSum.getCurrency()))
                            return;

                        if (!assertEquals(TimeUtils.getPlainDate("2013-01-08"), order.getStartDate()))
                            return;

                        if (!assertEquals("8009710218", symbols.getVariableSymbol()))
                            return;
                        if (!assertEquals("0", symbols.getSpecificSymbol()))
                            return;

                        if (!assertEquals(Integer.valueOf(1), order.getVersionId()))
                            return;
                        if (!assertEquals(TimeUtils.getPlainDate("2013-01-08"), order.getVersionValidityDate()))
                            return;

                        if (!assertEquals(SigningState.OPEN, order.signing().getSigningState()))
                            return;
                        if (!assertEquals("160530104690104", order.signing().getSignId()))
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
        return "JDirectDebitsWithIdDeleteTest";
    }

}
