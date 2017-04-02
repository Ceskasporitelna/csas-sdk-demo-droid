package cz.csas.demo.test.cases.netbanking.securities;

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
import cz.csas.cscore.webapi.PaginatedParameters;
import cz.csas.cscore.webapi.Pagination;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.securities.SecuritiesListResponse;
import cz.csas.netbanking.securities.Security;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JSecuritiesListPage0Test extends TestCase {

    private final String X_JUDGE_CASE = "securities.list.page0";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        final Map<String, String> headers = new HashMap<>();
                        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                        mNetbankingJudgeClient.setGlobalHeaders(headers);

                        mNetbankingJudgeClient.getSecuritiesResource().list(new PaginatedParameters(new Pagination(0, 1)), new CallbackWebApi<SecuritiesListResponse>() {
                            @Override
                            public void success(SecuritiesListResponse securitiesListResponse) {
                                if (!assertEquals(Long.valueOf(0), securitiesListResponse.getPageNumber()))
                                    return;
                                if (!assertEquals(Long.valueOf(2), securitiesListResponse.getPageCount()))
                                    return;
                                if (!assertEquals(Long.valueOf(1), securitiesListResponse.getPageSize()))
                                    return;
                                if (!assertEquals(Long.valueOf(1), securitiesListResponse.getNextPage()))
                                    return;

                                List<Security> accounts = securitiesListResponse.getSecuritiesAccounts();
                                if (!assertEquals(1, accounts.size()))
                                    return;

                                Security account = accounts.get(0);
                                if (!assertEquals("420A817C20E4814C7C516A53ABA8E78F0CDBE324", account.getId()))
                                    return;
                                if (!assertEquals(Long.valueOf(1345437), account.getBalance().getValue()))
                                    return;
                                if (!assertEquals(Integer.valueOf(2), account.getBalance().getPrecision()))
                                    return;
                                if (!assertEquals("CZK", account.getBalance().getCurrency()))
                                    return;
                                if (!assertEquals("Aleš Vrba", account.getDescription()))
                                    return;
                                if (!assertEquals("1034176627", account.getAccountno()))
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
                }

        );
    }

    @Override
    public String getName() {
        return "JSecuritiesListPage0Test";
    }

}
