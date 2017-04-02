package cz.csas.demo.test.cases.netbanking.promotions;

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
import cz.csas.netbanking.promotions.Action;
import cz.csas.netbanking.promotions.ActionType;
import cz.csas.netbanking.promotions.ButtonDesign;
import cz.csas.netbanking.promotions.CardDesign;
import cz.csas.netbanking.promotions.DisplayType;
import cz.csas.netbanking.promotions.DisplayTypeKind;
import cz.csas.netbanking.promotions.ProductCode;
import cz.csas.netbanking.promotions.Promotion;
import cz.csas.netbanking.promotions.PromotionsListResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JPromotionsListTest extends TestCase {

    private final String X_JUDGE_CASE = "promotions.list";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                mNetbankingJudgeClient.getPromotionsResource().list(new CallbackWebApi<PromotionsListResponse>() {
                    @Override
                    public void success(PromotionsListResponse promotionsListResponse) {
                        List<Promotion> promotions = promotionsListResponse.getPromotions();
                        if (!assertEquals(1, promotions.size()))
                            return;
                        Promotion promotion = promotions.get(0);

                        if (!assertEquals("218", promotion.getPromotionId()))
                            return;

                        DisplayType displayType = promotion.getDisplayType();
                        if (!assertEquals("Plugin Mobilní Platby", displayType.getTitleText()))
                            return;
                        if (!assertEquals("Aktivace pluginu zdarma", displayType.getSublineText()))
                            return;
                        if (!assertEquals(DisplayTypeKind.OVERVIEW_CARD, displayType.getDisplayType()))
                            return;
                        if (!assertEquals(CardDesign.PLUGIN_PROMOTION, displayType.getCardDesign()))
                            return;
                        if (!assertEquals("banner_ie_680x180_3020.xml", displayType.getBackgroundImage()))
                            return;
                        if (!assertEquals("banner_ie_222x137_26.xml", displayType.getMainImage()))
                            return;
                        if (!assertEquals(Integer.valueOf(1), displayType.getPosition()))
                            return;
                        if (!assertEquals(Integer.valueOf(4), displayType.getColumn()))
                            return;
                        if (!assertEquals("Aktivovat", displayType.getBtnText()))
                            return;
                        if (!assertEquals(ButtonDesign.PRIMARY, displayType.getBtnDesign()))
                            return;

                        List<Action> actions = promotion.getActions();
                        if (!assertEquals(2, actions.size()))
                            return;

                        Action action = actions.get(0);
                        if (!assertEquals("SHOWURL", action.getActionId()))
                            return;
                        if (!assertEquals(ActionType.SHOWURL, action.getActionType()))
                            return;
                        if (!assertEquals("_self", action.getTarget()))
                            return;
                        if (!assertEquals("#shop/plugins/PI-MOBILEPAYMENTS", action.getUrl()))
                            return;
                        if (!assertEquals(ProductCode.PI_MOBILEPAYMENTS, action.getProductCode()))
                            return;

                        action = actions.get(1);
                        if (!assertEquals("HIDE", action.getActionId()))
                            return;
                        if (!assertEquals(ActionType.HIDE, action.getActionType()))
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
        return "JPromotionsListTest";
    }

}
