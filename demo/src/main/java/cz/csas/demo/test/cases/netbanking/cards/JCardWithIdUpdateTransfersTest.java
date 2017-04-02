package cz.csas.demo.test.cases.netbanking.cards;

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
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.TransferResponse;
import cz.csas.netbanking.cards.CardTransferRequest;
import cz.csas.netbanking.cards.CardTransferType;
import cz.csas.netbanking.cards.Sender;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JCardWithIdUpdateTransfersTest extends TestCase {

    private final String X_JUDGE_CASE = "cards.withId.transfers.update";

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

                CardTransferRequest request = new CardTransferRequest(CardTransferType.DEBT_REPAYMENT,
                        new Sender(null, new AccountNumber("2326573123", "0800")),
                        new Amount(500000L, 2, "CZK"));

                mNetbankingJudgeClient.getCardsResource().withId(cardId).getTransferResource().update(request, new CallbackWebApi<TransferResponse>() {
                    @Override
                    public void success(TransferResponse transferResponse) {
                        if (!assertEquals(SigningState.OPEN, transferResponse.signing().getSigningState()))
                            return;
                        if (!assertEquals("151112531008554", transferResponse.signing().getSignId()))
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
        return "JCardWithIdUpdateTransfersTest";
    }

}
