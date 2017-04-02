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
public class JStandingOrdersListPage0Test extends TestCase {

    private final String X_JUDGE_CASE = "accounts.withId.standingOrders.list.page0";

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
                StandingOrdersParameters parameters = new StandingOrdersParameters(new Pagination(0, 2),
                        Sort.by(StandingOrdersSortableFields.NEXT_EXECUTION_DATE, SortDirection.DESCENDING));

                mNetbankingJudgeClient.getAccountsResource().withId(accountId).getStandingOrdersResource().list(parameters, new CallbackWebApi<StandingOrdersListResponse>() {
                    @Override
                    public void success(StandingOrdersListResponse standingOrdersListResponse) {

                        List<StandingOrder> standingOrders = standingOrdersListResponse.getStandingOrders();

                        if (!assertEquals(Long.valueOf(0), standingOrdersListResponse.getPageNumber()))
                            return;
                        if (!assertEquals(Long.valueOf(3), standingOrdersListResponse.getPageCount()))
                            return;
                        if (!assertEquals(Long.valueOf(2), standingOrdersListResponse.getPageSize()))
                            return;
                        if (!assertEquals(Long.valueOf(1), standingOrdersListResponse.getNextPage()))
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
                                    if (!assertEquals("1", order.getNumber()))
                                        return;
                                    if (!assertEquals(StandingOrderType.STANDING_ORDER, order.getType()))
                                        return;
                                    if (!assertEquals("OK", order.getStatus()))
                                        return;
                                    if (!assertEquals("nájemné", order.getAlias()))
                                        return;

                                    if (!assertEquals("174748262", receiver.getNumber()))
                                        return;
                                    if (!assertEquals("0300", receiver.getBankCode()))
                                        return;
                                    if (!assertEquals("CZ", receiver.getCountryCode()))
                                        return;
                                    if (!assertEquals("CZ6703000000000174748262", receiver.getCzIban()))
                                        return;

                                    if (!assertEquals(Long.valueOf(235000), amount.getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), amount.getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", amount.getCurrency()))
                                        return;

                                    if (!assertEquals(TimeUtils.getISO8601Date("2013-01-09T00:00:00+01:00"), order.getStartDate()))
                                        return;
                                    if (!assertEquals(TimeUtils.getPlainDate("2016-06-17"), order.getNextExecutionDate()))
                                        return;
                                    if (!assertEquals(TimeUtils.getPlainDate("2016-06-17"), order.getRealExecutionDate()))
                                        return;
                                    if (!assertEquals(ExecutionDueMode.DUE_DAY_OF_MONTH, order.getExecutionDueMode()))
                                        return;
                                    if (!assertEquals(ExecutionMode.UNTIL_CANCELLATION, order.getExecutionMode()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(17), order.getIntervalDueDay()))
                                        return;

                                    if (!assertEquals("8840709604", symbols.getVariableSymbol()))
                                        return;
                                    if (!assertEquals("311013", symbols.getSpecificSymbol()))
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
                                    if (!assertEquals("2", order.getNumber()))
                                        return;
                                    if (!assertEquals(StandingOrderType.STANDING_ORDER, order.getType()))
                                        return;
                                    if (!assertEquals("OK", order.getStatus()))
                                        return;
                                    if (!assertEquals("spořící účet", order.getAlias()))
                                        return;

                                    if (!assertEquals("3668601379", receiver.getNumber()))
                                        return;
                                    if (!assertEquals("0800", receiver.getBankCode()))
                                        return;
                                    if (!assertEquals("CZ", receiver.getCountryCode()))
                                        return;
                                    if (!assertEquals("CZ7308000000003668601379", receiver.getCzIban()))
                                        return;

                                    if (!assertEquals(Long.valueOf(100000), amount.getValue()))
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
        return "JStandingOrdersListPage0Test";
    }

}
