package cz.csas.demo.test.cases.netbanking.plugins;

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
import cz.csas.cscore.webapi.PaginatedParameters;
import cz.csas.cscore.webapi.Pagination;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.plugins.PeriodOfCharging;
import cz.csas.netbanking.plugins.Plugin;
import cz.csas.netbanking.plugins.PluginsListResponse;
import cz.csas.netbanking.plugins.StandardFee;
import cz.csas.netbanking.plugins.TimeOfCharging;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JPluginsListPage1Test extends TestCase {

    private final String X_JUDGE_CASE = "plugins.list.page1";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                mNetbankingJudgeClient.getPluginsResource().list(new PaginatedParameters(new Pagination(1, 1)),
                        new CallbackWebApi<PluginsListResponse>() {
                            @Override
                            public void success(PluginsListResponse pluginsListResponse) {
                                if (!assertEquals(Long.valueOf(1), pluginsListResponse.getPageNumber()))
                                    return;
                                if (!assertEquals(Long.valueOf(2), pluginsListResponse.getPageCount()))
                                    return;
                                if (!assertEquals(Long.valueOf(1), pluginsListResponse.getPageSize()))
                                    return;

                                List<Plugin> plugins = pluginsListResponse.getPlugins();
                                if (!assertEquals(1, plugins.size()))
                                    return;
                                Plugin plugin = plugins.get(0);

                                if (!assertEquals("PFM_1", plugin.getProductCode()))
                                    return;
                                if (!assertEquals("PluginBudgets pro CZ", plugin.getName()))
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
        return "JPluginsListPage1Test";
    }

}
