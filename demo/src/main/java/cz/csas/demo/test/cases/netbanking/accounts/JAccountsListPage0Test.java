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
import cz.csas.cscore.webapi.Pagination;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.accounts.AccountsListResponse;
import cz.csas.netbanking.accounts.AccountsParameters;
import cz.csas.netbanking.accounts.MainAccount;
import cz.csas.netbanking.accounts.ProductSubType;
import cz.csas.netbanking.accounts.ProductType;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JAccountsListPage0Test extends TestCase {

    private final String X_JUDGE_CASE = "accounts.list.page0";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                AccountsParameters parameters = new AccountsParameters(new Pagination(0, 1));

                mNetbankingJudgeClient.getAccountsResource().list(parameters, new CallbackWebApi<AccountsListResponse>() {
                    @Override
                    public void success(AccountsListResponse accountsListResponse) {

                        List<MainAccount> accounts = accountsListResponse.getAccounts();
                        if (!assertEquals(Long.valueOf(0), accountsListResponse.getPageNumber()))
                            return;
                        if (!assertEquals(Long.valueOf(2), accountsListResponse.getPageCount()))
                            return;
                        if (!assertEquals(Long.valueOf(1), accountsListResponse.getPageSize()))
                            return;
                        if (!assertEquals(Long.valueOf(1), accountsListResponse.getNextPage()))
                            return;
                        if (!assertEquals(1, accounts.size()))
                            return;

                        MainAccount account = accounts.get(0);
                        AccountNumber number = account.getAccountno();
                        Amount balance = account.getBalance();
                        Amount disposable = account.getDisposable();

                        if (!assertEquals("4B2F9EBE742BCAE1E98A78E12F6FBC62464A74EE", account.getId()))
                            return;
                        if (!assertEquals("2059930033", number.getNumber()))
                            return;
                        if (!assertEquals("0800", number.getBankCode()))
                            return;
                        if (!assertEquals("CZ", number.getCountryCode()))
                            return;
                        if (!assertEquals("CZ1208000000002059930033", number.getCzIban()))
                            return;
                        if (!assertEquals("GIBACZPX", number.getCzBic()))
                            return;
                        if (!assertEquals(Long.valueOf(1243017), balance.getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), balance.getPrecision()))
                            return;
                        if (!assertEquals("CZK", balance.getCurrency()))
                            return;
                        if (!assertEquals("50", account.getProduct()))
                            return;
                        if (!assertEquals("Osobní konto ČS", account.getProductI18N()))
                            return;
                        if (!assertEquals(ProductType.CURRENT, account.getType()))
                            return;
                        if (!assertEquals(ProductSubType.GIRO_ACCOUNT, account.getSubtype()))
                            return;
                        if (!assertEquals(Long.valueOf(1243017), disposable.getValue()))
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
        return "JAccountsListPage0Test";
    }

}
