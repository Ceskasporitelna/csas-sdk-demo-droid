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
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.accounts.AccountBalance;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JAccountWithIdBalancesTest extends TestCase {

    private final String X_JUDGE_CASE = "accounts.withId.balances.get";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String accountId = "076E1DBCCCD38729A99D93AC8D3E8273237C7E36";
                mNetbankingJudgeClient.getAccountsResource().withId(accountId).getBalanceResource().get(new CallbackWebApi<AccountBalance>() {
                    @Override
                    public void success(AccountBalance accountBalance) {

                        Amount balance = accountBalance.getBalance();
                        Amount disposable = accountBalance.getDisposable();

                        if (!assertEquals(Long.valueOf(2650706), balance.getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), balance.getPrecision()))
                            return;
                        if (!assertEquals("CZK", balance.getCurrency()))
                            return;
                        if (!assertEquals(Long.valueOf(2650706), disposable.getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), disposable.getPrecision()))
                            return;
                        if (!assertEquals("CZK", disposable.getCurrency()))
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
        return "JAccountWithIdBalancesTest";
    }

}
