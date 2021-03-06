package cz.csas.demo.test.cases.netbanking.accounts;

import java.util.Date;
import java.util.HashMap;
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
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.TransferResponse;
import cz.csas.netbanking.accounts.AccountTransferRequest;
import cz.csas.netbanking.accounts.AccountsTransferType;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JAccountsWithIdTransfersUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "accounts.withId.transfers.update";

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

                AccountTransferRequest request = new AccountTransferRequest(
                        AccountsTransferType.REVOLVING_LOAN_DISBURSEMENT,
                        new Date(1425081600000L), //2015-02-28
                        "moje prve cerpanie z penize na klik",
                        new Amount(1000L, 2, "CZK"));

                mNetbankingJudgeClient.getAccountsResource().withId(accountId).getTransferResource().update(request, new CallbackWebApi<TransferResponse>() {
                    @Override
                    public void success(TransferResponse transferResponse) {

                        if (!assertEquals(SigningState.OPEN, transferResponse.signing().getSigningState()))
                            return;
                        if (!assertEquals("151112531008724", transferResponse.signing().getSignId()))
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
        return "JAccountsWithIdTransfersUpdateTest";
    }

}
