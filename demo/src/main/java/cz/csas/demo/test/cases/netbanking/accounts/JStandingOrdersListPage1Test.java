package cz.csas.demo.test.cases.netbanking.accounts;

import java.util.ArrayList;
import java.util.Date;
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
import cz.csas.netbanking.accounts.ExecutionDueMode;
import cz.csas.netbanking.accounts.ExecutionMode;
import cz.csas.netbanking.accounts.StandingOrder;
import cz.csas.netbanking.accounts.StandingOrderType;
import cz.csas.netbanking.accounts.StandingOrdersListResponse;
import cz.csas.netbanking.accounts.StandingOrdersParameters;
import cz.csas.netbanking.accounts.StandingOrdersSortableFields;
import cz.csas.netbanking.orders.Symbols;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JStandingOrdersListPage1Test extends TestCase {

    private final String X_JUDGE_CASE = "accounts.withId.standingOrders.list.page1";

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
                StandingOrdersParameters parameters = new StandingOrdersParameters(new Pagination(1, 2),
                        Sort.by(StandingOrdersSortableFields.NEXT_EXECUTION_DATE, SortDirection.DESCENDING));

                mNetbankingJudgeClient.getAccountsResource().withId(accountId).getStandingOrdersResource().list(parameters, new CallbackWebApi<StandingOrdersListResponse>() {
                    @Override
                    public void success(StandingOrdersListResponse standingOrdersListResponse) {

                        List<StandingOrder> standingOrders = standingOrdersListResponse.getStandingOrders();
                        if (!assertEquals(Long.valueOf(1), standingOrdersListResponse.getPageNumber()))
                            return;
                        if (!assertEquals(Long.valueOf(3), standingOrdersListResponse.getPageCount()))
                            return;
                        if (!assertEquals(Long.valueOf(2), standingOrdersListResponse.getPageSize()))
                            return;
                        if (!assertEquals(Long.valueOf(2), standingOrdersListResponse.getNextPage()))
                            return;
                        if (!assertEquals(2, standingOrders.size()))
                            return;

                        for (int i = 0; i < standingOrders.size(); ++i) {
                            StandingOrder order = standingOrders.get(i);
                            AccountNumber receiver = order.getReceiver();
                            Amount amount = order.getAmount();
                            Symbols symbols = order.getSymbols();
                            switch (i) {
                                case 0:
                                    if (!assertEquals("3", order.getNumber()))
                                        return;
                                    if (!assertEquals(StandingOrderType.STANDING_ORDER, order.getType()))
                                        return;
                                    if (!assertEquals("OK", order.getStatus()))
                                        return;

                                    if (!assertEquals("35-2001269369", receiver.getNumber()))
                                        return;
                                    if (!assertEquals("0800", receiver.getBankCode()))
                                        return;
                                    if (!assertEquals("CZ", receiver.getCountryCode()))
                                        return;
                                    if (!assertEquals("CZ7908000000352001269369", receiver.getCzIban()))
                                        return;
                                    if (!assertEquals("pravidelný nákupPF-8846245-1", order.getSenderReference()))
                                        return;

                                    if (!assertEquals(Long.valueOf(50000), amount.getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), amount.getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", amount.getCurrency()))
                                        return;

                                    if (!assertEquals(TimeUtils.getISO8601Date("2014-04-24T00:00:00+02:00"), order.getStartDate()))
                                        return;
                                    if (!assertEquals(TimeUtils.getPlainDate("2016-06-15"), order.getNextExecutionDate()))
                                        return;
                                    if (!assertEquals(TimeUtils.getPlainDate("2016-06-15"), order.getRealExecutionDate()))
                                        return;
                                    if (!assertEquals(ExecutionDueMode.DUE_DAY_OF_MONTH, order.getExecutionDueMode()))
                                        return;
                                    if (!assertEquals(ExecutionMode.UNTIL_CANCELLATION, order.getExecutionMode()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(15), order.getIntervalDueDay()))
                                        return;

                                    if (!assertEquals("1034176627", symbols.getVariableSymbol()))
                                        return;

                                    List<String> flags = new ArrayList<String>();
                                    flags.add("deletable");
                                    if (!assertEquals(flags, order.getFlags()))
                                        return;

                                    List<Date> executionDates = new ArrayList<Date>();
                                    executionDates.add(TimeUtils.getPlainDate("2016-06-17"));
                                    if (!assertEquals(executionDates, order.getScheduledExecutionDates()))
                                        return;
                                    break;

                                case 1:
                                    if (!assertEquals("4", order.getNumber()))
                                        return;
                                    if (!assertEquals(StandingOrderType.STANDING_ORDER, order.getType()))
                                        return;
                                    if (!assertEquals("OK", order.getStatus()))
                                        return;

                                    if (!assertEquals("35-2001269369", receiver.getNumber()))
                                        return;
                                    if (!assertEquals("0800", receiver.getBankCode()))
                                        return;
                                    if (!assertEquals("CZ", receiver.getCountryCode()))
                                        return;
                                    if (!assertEquals("CZ7908000000352001269369", receiver.getCzIban()))
                                        return;
                                    if (!assertEquals("pravidelný nákupPF-10007570-1ISČS Konzervativní Mix FF", order.getSenderReference()))
                                        return;

                                    if (!assertEquals(Long.valueOf(100000), amount.getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), amount.getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", amount.getCurrency()))
                                        return;

                                    if (!assertEquals(TimeUtils.getISO8601Date("2015-07-31T00:00:00+02:00"), order.getStartDate()))
                                        return;
                                    if (!assertEquals(TimeUtils.getPlainDate("2016-06-15"), order.getNextExecutionDate()))
                                        return;
                                    if (!assertEquals(TimeUtils.getPlainDate("2016-06-15"), order.getRealExecutionDate()))
                                        return;
                                    if (!assertEquals(ExecutionDueMode.DUE_DAY_OF_MONTH, order.getExecutionDueMode()))
                                        return;
                                    if (!assertEquals(ExecutionMode.UNTIL_CANCELLATION, order.getExecutionMode()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(15), order.getIntervalDueDay()))
                                        return;
                                    flags = new ArrayList<String>();
                                    flags.add("deletable");
                                    if (!assertEquals(flags, order.getFlags()))
                                        return;
                                    executionDates = new ArrayList<Date>();
                                    executionDates.add(TimeUtils.getPlainDate("2016-06-17"));
                                    if (!assertEquals(executionDates, order.getScheduledExecutionDates()))
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
        return "JStandingOrdersListPage1Test";
    }

}
