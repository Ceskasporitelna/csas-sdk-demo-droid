package cz.csas.demo.test.cases.netbanking.plugins;

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
import cz.csas.cscore.utils.TimeUtils;
import cz.csas.cscore.webapi.signing.SigningState;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.plugins.PeriodOfCharging;
import cz.csas.netbanking.plugins.Plugin;
import cz.csas.netbanking.plugins.PluginUpdateRequest;
import cz.csas.netbanking.plugins.PluginUpdateResponse;
import cz.csas.netbanking.plugins.StandardFee;
import cz.csas.netbanking.plugins.TimeOfCharging;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JPluginsWithIdUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "plugins.withId.update";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String id = "PI-MOBILEPAYMENTS";
                PluginUpdateRequest request = new PluginUpdateRequest(id, null, Arrays.asList("active"));

                mNetbankingJudgeClient.getPluginsResource().withId(id).update(request, new CallbackWebApi<PluginUpdateResponse>() {
                    @Override
                    public void success(PluginUpdateResponse pluginUpdateResponse) {
                        List<String> flags = new ArrayList<String>();
                        flags.add("active");
                        Plugin plugin = pluginUpdateResponse;
                        if (!assertEquals("PI-MOBILEPAYMENTS", plugin.getProductCode()))
                            return;
                        if (!assertEquals("Plugin pro mobilní platby", plugin.getName()))
                            return;

                        List<StandardFee> standardFees = plugin.getStandardFees();
                        if (!assertEquals(1, standardFees.size()))
                            return;
                        StandardFee standardFee = standardFees.get(0);

                        if (!assertEquals(TimeOfCharging.IMMEDIATELY, standardFee.getTimeOfCharging()))
                            return;
                        if (!assertEquals(PeriodOfCharging.NON_RECURRING, standardFee.getPeriodOfCharging()))
                            return;

                        Amount amount = standardFee.getAmount();
                        if (!assertEquals(Long.valueOf(0), amount.getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), amount.getPrecision()))
                            return;
                        if (!assertEquals("CZK", amount.getCurrency()))
                            return;

                        if (!assertEquals(TimeUtils.getPlainDate("2100-01-01"), plugin.getValidUntil()))
                            return;
                        if (!assertEquals(flags, plugin.getFlags()))
                            return;

                        if (!assertEquals(SigningState.NONE, pluginUpdateResponse.signing().getSigningState()))
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
        return "JPluginsWithIdUpdateTest";
    }

}
