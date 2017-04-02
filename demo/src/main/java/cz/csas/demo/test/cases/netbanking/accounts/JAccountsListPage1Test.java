package cz.csas.demo.test.cases.netbanking.accounts;

import java.util.ArrayList;
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
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.accounts.AccountsListResponse;
import cz.csas.netbanking.accounts.AccountsParameters;
import cz.csas.netbanking.accounts.InstallmentFrequency;
import cz.csas.netbanking.accounts.Loan;
import cz.csas.netbanking.accounts.MainAccount;
import cz.csas.netbanking.accounts.ProductSubType;
import cz.csas.netbanking.accounts.ProductType;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JAccountsListPage1Test extends TestCase {

    private final String X_JUDGE_CASE = "accounts.list.page1";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                AccountsParameters parameters = new AccountsParameters(new Pagination(1, 1));

                mNetbankingJudgeClient.getAccountsResource().list(parameters, new CallbackWebApi<AccountsListResponse>() {
                    @Override
                    public void success(AccountsListResponse accountsListResponse) {

                        List<MainAccount> accounts = accountsListResponse.getAccounts();
                        if (!assertEquals(Long.valueOf(1), accountsListResponse.getPageNumber()))
                            return;
                        if (!assertEquals(Long.valueOf(2), accountsListResponse.getPageCount()))
                            return;
                        if (!assertEquals(Long.valueOf(1), accountsListResponse.getPageSize()))
                            return;
                        if (!assertEquals(1, accounts.size()))
                            return;

                        MainAccount account = accounts.get(0);
                        AccountNumber number = account.getAccountno();
                        Amount balance = account.getBalance();
                        Loan loan = account.getLoan();

                        if (!assertEquals("EC1C13B722F726D783365D0A89D23E805098B167", account.getId()))
                            return;
                        if (!assertEquals("428602109", number.getNumber()))
                            return;
                        if (!assertEquals("0800", number.getBankCode()))
                            return;
                        if (!assertEquals("CZ", number.getCountryCode()))
                            return;
                        if (!assertEquals("CZ6408000000000428602109", number.getCzIban()))
                            return;
                        if (!assertEquals("GIBACZPX", number.getCzBic()))
                            return;
                        if (!assertEquals(Long.valueOf(83492030), balance.getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), balance.getPrecision()))
                            return;
                        if (!assertEquals("CZK", balance.getCurrency()))
                            return;
                        if (!assertEquals("15", account.getProduct()))
                            return;
                        if (!assertEquals("Hypotéka České spořitelny", account.getProductI18N()))
                            return;
                        if (!assertEquals(ProductType.LOAN, account.getType()))
                            return;
                        if (!assertEquals(ProductSubType.MORTGAGE, account.getSubtype()))
                            return;
                        if (!assertEquals(Long.valueOf(90200000), loan.getLoanAmount().getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), loan.getLoanAmount().getPrecision()))
                            return;
                        if (!assertEquals("CZK", loan.getLoanAmount().getCurrency()))
                            return;
                        if (!assertEquals(TimeUtils.getPlainDate("2037-12-31"), loan.getMaturityDate()))
                            return;
                        if (!assertEquals(Long.valueOf(0), loan.getRemainingLoanAmount().getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), loan.getRemainingLoanAmount().getPrecision()))
                            return;
                        if (!assertEquals("CZK", loan.getRemainingLoanAmount().getCurrency()))
                            return;
                        if (!assertEquals(TimeUtils.getPlainDate("2012-12-31"), loan.getDrawdownToDate()))
                            return;
                        if (!assertEquals(Long.valueOf(83492030), loan.getDrawdownAmount().getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), loan.getDrawdownAmount().getPrecision()))
                            return;
                        if (!assertEquals("CZK", loan.getDrawdownAmount().getCurrency()))
                            return;
                        if (!assertEquals(Long.valueOf(83492030), loan.getOutstandingDebt().getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), loan.getOutstandingDebt().getPrecision()))
                            return;
                        if (!assertEquals("CZK", loan.getOutstandingDebt().getCurrency()))
                            return;
                        if (!assertEquals(InstallmentFrequency.MONTHLY, loan.getInstallmentFrequency()))
                            return;
                        if (!assertEquals(Integer.valueOf(31), loan.getInstallmentDay()))
                            return;
                        if (!assertEquals(TimeUtils.getPlainDate("2016-03-31"), loan.getNextRateDate()))
                            return;
                        if (!assertEquals(Long.valueOf(429300), loan.getNextRateAmount().getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), loan.getNextRateAmount().getPrecision()))
                            return;
                        if (!assertEquals("CZK", loan.getNextRateAmount().getCurrency()))
                            return;
                        if (!assertEquals(Long.valueOf(84407467), loan.getCzLumpsumRepayment().getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), loan.getCzLumpsumRepayment().getPrecision()))
                            return;
                        if (!assertEquals("CZK", loan.getCzLumpsumRepayment().getCurrency()))
                            return;
                        if (!assertEquals(Double.valueOf(2.99), account.getDebitInterestRate()))
                            return;

                        List<String> flags = new ArrayList<String>();
                        flags.add("owner");
                        flags.add("electronicStatementAllowed");
                        flags.add("accountQueryAllowed");
                        flags.add("standingOrderNotAllowed");

                        if (!assertEquals(flags, account.getFlags()))
                            return;

                        if (!assertEquals("Aleš Vrba", account.getDescription()))
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
        return "JAccountsListPage1Test";
    }

}
