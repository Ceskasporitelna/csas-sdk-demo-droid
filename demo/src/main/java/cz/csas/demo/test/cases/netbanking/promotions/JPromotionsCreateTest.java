package cz.csas.demo.test.cases.netbanking.promotions;

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
import cz.csas.netbanking.promotions.ActionType;
import cz.csas.netbanking.promotions.ExecutedAction;
import cz.csas.netbanking.promotions.InfoItem;
import cz.csas.netbanking.promotions.PromotionActionCreateRequest;
import cz.csas.netbanking.promotions.PromotionActionCreateResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JPromotionsCreateTest extends TestCase {

    private final String X_JUDGE_CASE = "promotions.create";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                PromotionActionCreateRequest request = new PromotionActionCreateRequest("218", new ExecutedAction("HIDE", ActionType.HIDE));
                mNetbankingJudgeClient.getPromotionsResource().getActionsResource().create(request, new CallbackWebApi<PromotionActionCreateResponse>() {
                    @Override
                    public void success(PromotionActionCreateResponse createPromotionResponse) {
                        if (!assertEquals(1, createPromotionResponse.getInfoItems().size()))
                            return;
                        InfoItem item = createPromotionResponse.getInfoItems().get(0);
                        if (!assertEquals("RETURN_MESSAGE", item.getInfoName()))
                            return;
                        if (!assertEquals("successfully executed", item.getInfoValue()))
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
        return "JPromotionsCreateTest";
    }

}
