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
import cz.csas.netbanking.accounts.ExecutionDueMode;
import cz.csas.netbanking.accounts.ExecutionInterval;
import cz.csas.netbanking.accounts.ExecutionMode;
import cz.csas.netbanking.accounts.StandingOrderCreateRequest;
import cz.csas.netbanking.accounts.StandingOrderResponse;
import cz.csas.netbanking.accounts.StandingOrderType;
import cz.csas.netbanking.orders.Symbols;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JStandingOrdersCreateTest extends TestCase {

    private final String X_JUDGE_CASE = "accounts.withId.standingOrders.create";

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

                StandingOrderCreateRequest request = new StandingOrderCreateRequest.Builder()
                        .setType(StandingOrderType.STANDING_ORDER)
                        .setAlias("Monthly standing order executed on the last day of month")
                        .setReceiverName("Name of the receiver")
                        .setReceiver(new AccountNumber("188505042", "0300"))
                        .setAmount(new Amount(30000l, 2, "CZK"))
                        .setNextExecutionDate(TimeUtils.getPlainDate("2016-12-31"))
                        .setExecutionMode(ExecutionMode.UNTIL_CANCELLATION)
                        .setExecutionDueMode(ExecutionDueMode.DUE_LAST_DAY_OF_MONTH)
                        .setExecutionInterval(ExecutionInterval.MONTHLY)
                        .setSymbols(new Symbols("854259", "0305", "785421"))
                        .build();

                mNetbankingJudgeClient.getAccountsResource().withId(accountId).getStandingOrdersResource().create(request, new CallbackWebApi<StandingOrderResponse>() {
                    @Override
                    public void success(StandingOrderResponse standingOrder) {
                        StandingOrderResponse order = standingOrder;
                        AccountNumber receiver = order.getReceiver();
                        Amount amount = order.getAmount();
                        Symbols symbols = order.getSymbols();

                        if (!assertEquals("160526104005956", order.getNumber()))
                            return;
                        if (!assertEquals(StandingOrderType.STANDING_ORDER, order.getType()))
                            return;
                        if (!assertEquals("OK", order.getStatus()))
                            return;
                        if (!assertEquals("Monthly standing order executed on the last day of month", order.getAlias()))
                            return;

                        if (!assertEquals("188505042", receiver.getNumber()))
                            return;
                        if (!assertEquals("0300", receiver.getBankCode()))
                            return;
                        if (!assertEquals("Name of the receiver", order.getReceiverName()))
                            return;

                        if (!assertEquals(Long.valueOf(30000), amount.getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), amount.getPrecision()))
                            return;
                        if (!assertEquals("CZK", amount.getCurrency()))
                            return;

                        if (!assertEquals(TimeUtils.getISO8601Date("2016-12-31T00:00:00+01:00"), order.getStartDate()))
                            return;
                        if (!assertEquals(TimeUtils.getPlainDate("2016-12-31"), order.getNextExecutionDate()))
                            return;
                        if (!assertEquals(ExecutionDueMode.DUE_LAST_DAY_OF_MONTH, order.getExecutionDueMode()))
                            return;
                        if (!assertEquals(ExecutionMode.UNTIL_CANCELLATION, order.getExecutionMode()))
                            return;

                        if (!assertEquals("854259", symbols.getVariableSymbol()))
                            return;
                        if (!assertEquals("0305", symbols.getConstantSymbol()))
                            return;
                        if (!assertEquals("785421", symbols.getSpecificSymbol()))
                            return;

                        if (!assertEquals(SigningState.OPEN, order.signing().getSigningState()))
                            return;
                        if (!assertEquals("160526104005956", order.signing().getSignId()))
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
        return "JStandingOrdersCreateTest";
    }

}
