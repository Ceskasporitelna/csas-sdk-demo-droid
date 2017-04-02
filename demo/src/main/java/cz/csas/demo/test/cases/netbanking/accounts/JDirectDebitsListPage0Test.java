package cz.csas.demo.test.cases.netbanking.accounts;

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
import cz.csas.cscore.utils.TimeUtils;
import cz.csas.cscore.webapi.Pagination;
import cz.csas.cscore.webapi.Sort;
import cz.csas.cscore.webapi.SortDirection;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.accounts.DirectDebit;
import cz.csas.netbanking.accounts.DirectDebitType;
import cz.csas.netbanking.accounts.DirectDebitsListResponse;
import cz.csas.netbanking.accounts.DirectDebitsParameters;
import cz.csas.netbanking.accounts.DirectDebitsSortableFields;
import cz.csas.netbanking.accounts.PeriodCycle;
import cz.csas.netbanking.orders.Symbols;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JDirectDebitsListPage0Test extends TestCase {

    private final String X_JUDGE_CASE = "accounts.withId.directDebts.list.page0";

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
                DirectDebitsParameters parameters = new DirectDebitsParameters(new Pagination(0, 2),
                        Sort.by(DirectDebitsSortableFields.PERIOD_CYCLE, SortDirection.DESCENDING));

                mNetbankingJudgeClient.getAccountsResource().withId(accountId).getDirectDebitsResource().list(parameters, new CallbackWebApi<DirectDebitsListResponse>() {
                    @Override
                    public void success(DirectDebitsListResponse standingOrdersListResponse) {

                        List<DirectDebit> directDebits = standingOrdersListResponse.getDirectDebits();

                        if (!assertEquals(Long.valueOf(0), standingOrdersListResponse.getPageNumber()))
                            return;
                        if (!assertEquals(Long.valueOf(2), standingOrdersListResponse.getPageCount()))
                            return;
                        if (!assertEquals(Long.valueOf(2), standingOrdersListResponse.getPageSize()))
                            return;
                        if (!assertEquals(Long.valueOf(1), standingOrdersListResponse.getNextPage()))
                            return;
                        if (!assertEquals(2, directDebits.size()))
                            return;

                        for (int i = 0; i < directDebits.size(); ++i) {
                            DirectDebit order = directDebits.get(i);
                            AccountNumber receiver = order.getReceiver();
                            Symbols symbols = order.getSymbols();
                            Amount limitSum = order.getLimitSum();
                            switch (i) {
                                case 0:
                                    if (!assertEquals("2", order.getNumber()))
                                        return;
                                    if (!assertEquals("428602109", receiver.getNumber()))
                                        return;
                                    if (!assertEquals("0800", receiver.getBankCode()))
                                        return;
                                    if (!assertEquals("CZ", receiver.getCountryCode()))
                                        return;

                                    if (!assertEquals(DirectDebitType.DIRECT_DEBIT, order.getType()))
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

                                    if (!assertEquals(TimeUtils.getPlainDate("2012-11-26"), order.getStartDate()))
                                        return;

                                    if (!assertEquals("0", symbols.getVariableSymbol()))
                                        return;
                                    if (!assertEquals("0", symbols.getSpecificSymbol()))
                                        return;

                                    if (!assertEquals(Integer.valueOf(1), order.getVersionId()))
                                        return;
                                    if (!assertEquals(TimeUtils.getPlainDate("2012-11-26"), order.getVersionValidityDate()))
                                        return;
                                    if (!assertEquals("Vrba Aleš", order.getReceiverName()))
                                        return;
                                    break;

                                case 1:
                                    if (!assertEquals("3", order.getNumber()))
                                        return;
                                    if (!assertEquals("1330052", receiver.getNumber()))
                                        return;
                                    if (!assertEquals("0800", receiver.getBankCode()))
                                        return;
                                    if (!assertEquals("CZ", receiver.getCountryCode()))
                                        return;

                                    if (!assertEquals(DirectDebitType.DIRECT_DEBIT, order.getType()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(12), order.getPeriodicity()))
                                        return;
                                    if (!assertEquals(PeriodCycle.MONTHLY, order.getPeriodCycle()))
                                        return;

                                    if (!assertEquals(Long.valueOf(88400), limitSum.getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), limitSum.getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", limitSum.getCurrency()))
                                        return;

                                    if (!assertEquals(TimeUtils.getPlainDate("2012-11-30"), order.getStartDate()))
                                        return;

                                    if (!assertEquals("0", symbols.getVariableSymbol()))
                                        return;
                                    if (!assertEquals("0", symbols.getSpecificSymbol()))
                                        return;

                                    if (!assertEquals(Integer.valueOf(1), order.getVersionId()))
                                        return;
                                    if (!assertEquals(TimeUtils.getPlainDate("2012-11-30"), order.getVersionValidityDate()))
                                        return;
                                    break;
                            }
                            mTestCallback.result(mTestResult);
                        }
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
        return "JDirectDebitsListPage0Test";
    }

}
