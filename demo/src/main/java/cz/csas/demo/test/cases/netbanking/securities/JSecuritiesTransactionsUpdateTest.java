package cz.csas.demo.test.cases.netbanking.securities;

import java.util.ArrayList;
import java.util.Arrays;
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
import cz.csas.netbanking.Transaction;
import cz.csas.netbanking.TransactionUpdateRequest;
import cz.csas.netbanking.TransactionUpdateResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JSecuritiesTransactionsUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "securities.withId.transactions.withId.update";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);
                String securityId = "420A817C20E4814C7C516A53ABA8E78F0CDBE324";
                String transactionId = "100000189114334";
                TransactionUpdateRequest request = new TransactionUpdateRequest(
                        "100000189114334", "New client's personal note for transaction", Arrays.asList("hasStar"));
                mNetbankingJudgeClient.getSecuritiesResource().withId(securityId).getTransactionsResource()
                        .withId(transactionId).update(request, new CallbackWebApi<TransactionUpdateResponse>() {
                    @Override
                    public void success(TransactionUpdateResponse transactionUpdateResponse) {
                        List<String> flags = new ArrayList<String>();
                        flags.add("hasStar");
                        flags.add("hasNote");
                        if (!assertEquals(SigningState.NONE, transactionUpdateResponse.signing().getSigningState()))
                            return;
                        Transaction transaction = transactionUpdateResponse.getTransaction();

                        if (!assertEquals("100000189114334", transaction.getId()))
                            return;
                        if (!assertEquals("New client's personal note for transaction", transaction.getNote()))
                            return;
                        if (!assertEquals(flags, transaction.getFlags()))
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
        return "JSecuritiesTransactionsUpdateTest";
    }

}
