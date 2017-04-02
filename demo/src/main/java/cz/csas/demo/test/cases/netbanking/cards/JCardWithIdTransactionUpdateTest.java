package cz.csas.demo.test.cases.netbanking.cards;

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
public class JCardWithIdTransactionUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "cards.withId.transactions.withId.update";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String cardId = "33A813886442D946122C78305EC4E482DE9F574D";
                String transactionId = "23498";

                TransactionUpdateRequest request = new TransactionUpdateRequest("note",
                        Arrays.asList("hasStar"));

                mNetbankingJudgeClient.getCardsResource().withId(cardId).getTransactionsResource().withId(transactionId)
                        .update(request, new CallbackWebApi<TransactionUpdateResponse>() {
                            @Override
                            public void success(TransactionUpdateResponse cardTransaction) {

                                if (!assertEquals("23498", cardTransaction.getTransaction().getId()))
                                    return;
                                if (!assertEquals("note", cardTransaction.getTransaction().getNote()))
                                    return;
                                List<String> flags = new ArrayList<String>();
                                flags.add("hasNote");
                                flags.add("hasStar");
                                if (!assertEquals(flags, cardTransaction.getTransaction().getFlags()))
                                    return;
                                if (!assertEquals(SigningState.NONE, cardTransaction.signing().getSigningState()))
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
        return "JCardWithIdTransactionUpdateTest";
    }

}
