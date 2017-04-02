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
import cz.csas.cscore.webapi.signing.SigningState;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.accounts.ChangeAccountSettingsRequest;
import cz.csas.netbanking.accounts.ChangeAccountSettingsResponse;
import cz.csas.netbanking.accounts.ProductSubType;
import cz.csas.netbanking.accounts.ProductType;
import cz.csas.netbanking.accounts.SubAccount;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JAccountWithIdUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "accounts.withId.update";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader,
                new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        final Map<String, String> headers = new HashMap<>();
                        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                        mNetbankingJudgeClient.setGlobalHeaders(headers);

                        final String id = "076E1DBCCCD38729A99D93AC8D3E8273237C7E36";
                        String alias = "muj ucet";

                        ChangeAccountSettingsRequest request = new ChangeAccountSettingsRequest(alias);
                        mNetbankingJudgeClient.getAccountsResource().withId(id).update(request, new CallbackWebApi<ChangeAccountSettingsResponse>() {
                            @Override
                            public void success(ChangeAccountSettingsResponse changeAccountSettingsResponse) {

                                AccountNumber number = changeAccountSettingsResponse.getAccountno();
                                Amount balance = changeAccountSettingsResponse.getBalance();
                                Amount disposable = changeAccountSettingsResponse.getDisposable();

                                if (!assertEquals(id, changeAccountSettingsResponse.getId()))
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
                                if (!assertEquals(Long.valueOf(1208017), balance.getValue()))
                                    return;
                                if (!assertEquals(Integer.valueOf(2), balance.getPrecision()))
                                    return;
                                if (!assertEquals("CZK", balance.getCurrency()))
                                    return;
                                if (!assertEquals("50", changeAccountSettingsResponse.getProduct()))
                                    return;
                                if (!assertEquals("Osobní konto ČS", changeAccountSettingsResponse.getProductI18N()))
                                    return;
                                if (!assertEquals(ProductType.CURRENT, changeAccountSettingsResponse.getType()))
                                    return;
                                if (!assertEquals(ProductSubType.GIRO_ACCOUNT, changeAccountSettingsResponse.getSubtype()))
                                    return;
                                if (!assertEquals(Long.valueOf(1208017), disposable.getValue()))
                                    return;
                                if (!assertEquals(Integer.valueOf(2), disposable.getPrecision()))
                                    return;
                                if (!assertEquals("CZK", disposable.getCurrency()))
                                    return;
                                List<String> flags = new ArrayList<String>();
                                flags.add("owner");
                                flags.add("electronicStatementAllowed");
                                flags.add("accountQueryAllowed");
                                flags.add("directDebitAllowed");
                                flags.add("sipoDirectDebitAllowed");
                                flags.add("ownTransferAllowed");
                                flags.add("domesticTransferAllowed");
                                flags.add("urgentTransferAllowed");
                                if (!assertEquals(flags, changeAccountSettingsResponse.getFlags()))
                                    return;

                                if (!assertEquals("Aleš Vrba", changeAccountSettingsResponse.getDescription()))
                                    return;
                                if (!assertEquals("muj ucet", changeAccountSettingsResponse.getAlias()))
                                    return;
                                if (!assertEquals(SigningState.NONE, changeAccountSettingsResponse.signing().getSigningState()))
                                    return;

                                List<SubAccount> subAccounts = changeAccountSettingsResponse.getSubaccounts();
                                if (!assertEquals(1, subAccounts.size()))
                                    return;
                                SubAccount subAccount = subAccounts.get(0);
                                AccountNumber subNumber = subAccount.getAccountno();
                                Amount subBalance = subAccount.getBalance();
                                Amount interestRateLimit = subAccount.getCzInterestRateLimit();

                                if (!assertEquals("932933BABDE2A94753BAFF7FF146BA69BA90C259", subAccount.getId()))
                                    return;
                                if (!assertEquals("3668601379", subNumber.getNumber()))
                                    return;
                                if (!assertEquals("0800", subNumber.getBankCode()))
                                    return;
                                if (!assertEquals("CZ", subNumber.getCountryCode()))
                                    return;
                                if (!assertEquals("CZ7308000000003668601379", subNumber.getCzIban()))
                                    return;
                                if (!assertEquals("GIBACZPX", subNumber.getCzBic()))
                                    return;
                                if (!assertEquals(Long.valueOf(1607876), subBalance.getValue()))
                                    return;
                                if (!assertEquals(Integer.valueOf(2), subBalance.getPrecision()))
                                    return;
                                if (!assertEquals("CZK", subBalance.getCurrency()))
                                    return;
                                if (!assertEquals("53", subAccount.getProduct()))
                                    return;
                                if (!assertEquals("Spořicí účet k Osobnímu kontu", subAccount.getProductI18N()))
                                    return;
                                if (!assertEquals(ProductType.SAVING, subAccount.getType()))
                                    return;
                                if (!assertEquals(ProductSubType.SAVING_ACCOUNT, subAccount.getSubtype()))
                                    return;
                                if (!assertEquals(Double.valueOf(0.4), subAccount.getCreditInterestRate()))
                                    return;
                                if (!assertEquals(Double.valueOf(0.01), subAccount.getCzInterestRateOverLimit()))
                                    return;
                                if (!assertEquals(Long.valueOf(15000000), interestRateLimit.getValue()))
                                    return;
                                if (!assertEquals(Integer.valueOf(2), interestRateLimit.getPrecision()))
                                    return;
                                if (!assertEquals("CZK", interestRateLimit.getCurrency()))
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
        return "JAccountWithIdUpdateTest";
    }

}
