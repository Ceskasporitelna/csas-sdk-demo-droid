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
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.accounts.MainAccount;
import cz.csas.netbanking.accounts.ProductSubType;
import cz.csas.netbanking.accounts.ProductType;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JAccountWithIdTest extends TestCase {

    private final String X_JUDGE_CASE = "accounts.withId.get";

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

                        final String accountId = "076E1DBCCCD38729A99D93AC8D3E8273237C7E36";
                        mNetbankingJudgeClient.getAccountsResource().withId(accountId).get(new CallbackWebApi<MainAccount>() {
                            @Override
                            public void success(MainAccount mainAccount) {

                                AccountNumber number = mainAccount.getAccountno();
                                Amount balance = mainAccount.getBalance();
                                Amount disposable = mainAccount.getDisposable();

                                if (!assertEquals(accountId, mainAccount.getId()))
                                    return;
                                if (!assertEquals("2328489013", number.getNumber()))
                                    return;
                                if (!assertEquals("0800", number.getBankCode()))
                                    return;
                                if (!assertEquals("CZ", number.getCountryCode()))
                                    return;
                                if (!assertEquals("CZ5908000000002328489013", number.getCzIban()))
                                    return;
                                if (!assertEquals("GIBACZPX", number.getCzBic()))
                                    return;
                                if (!assertEquals(Long.valueOf(2650706), balance.getValue()))
                                    return;
                                if (!assertEquals(Integer.valueOf(2), balance.getPrecision()))
                                    return;
                                if (!assertEquals("CZK", balance.getCurrency()))
                                    return;
                                if (!assertEquals("49", mainAccount.getProduct()))
                                    return;
                                if (!assertEquals("Osobní účet ČS II", mainAccount.getProductI18N()))
                                    return;
                                if (!assertEquals(ProductType.CURRENT, mainAccount.getType()))
                                    return;
                                if (!assertEquals(ProductSubType.GIRO_ACCOUNT, mainAccount.getSubtype()))
                                    return;
                                if (!assertEquals(Long.valueOf(2650706), disposable.getValue()))
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
                                if (!assertEquals(flags, mainAccount.getFlags()))
                                    return;

                                if (!assertEquals("Anna Vojtíšková", mainAccount.getDescription()))
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
        return "JAccountWithIdTest";
    }

}
