package cz.csas.demo.test.cases.netbanking.contracts.pensions;

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
import cz.csas.netbanking.TransactionUpdateRequest;
import cz.csas.netbanking.TransactionUpdateResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JPensionTransactionUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "contracts.pensions.withId.transactions.withId.update";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String pensionId = "E7DD68AA3FF4487AF75626F901761B071E72FFFC";
                final String transactionId = "ADAD9879ADKJH9713";
                TransactionUpdateRequest request = new TransactionUpdateRequest("ADAD9879ADKJH9713", "New client's personal note for transaction", Arrays.asList("hasStar"));
                mNetbankingJudgeClient.getContractsResource().getPensionsResource().withId(pensionId)
                        .getTransactionsResource().withId(transactionId).update(request, new CallbackWebApi<TransactionUpdateResponse>() {
                    @Override
                    public void success(TransactionUpdateResponse transactionUpdateResponse) {
                        List<String> flags = new ArrayList<String>();
                        flags.add("hasStar");
                        flags.add("hasNote");
                        if (!assertEquals(transactionId, transactionUpdateResponse.getTransaction().getId()))
                            return;
                        if (!assertEquals("New client's personal note for transaction", transactionUpdateResponse.getTransaction().getNote()))
                            return;
                        if (!assertEquals(flags, transactionUpdateResponse.getTransaction().getFlags()))
                            return;
                        if (!assertEquals(SigningState.NONE, transactionUpdateResponse.signing().getSigningState()))
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
        return "JPensionTransactionUpdateTest";
    }

}
