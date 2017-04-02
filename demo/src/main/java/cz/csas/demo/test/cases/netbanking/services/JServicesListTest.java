package cz.csas.demo.test.cases.netbanking.services;

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
import cz.csas.netbanking.Service;
import cz.csas.netbanking.ServicesListResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JServicesListTest extends TestCase {

    private final String X_JUDGE_CASE = "services.list";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        final Map<String, String> headers = new HashMap<>();
                        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                        mNetbankingJudgeClient.setGlobalHeaders(headers);

                        mNetbankingJudgeClient.getServicesResource().list(new PaginatedParameters(new Pagination(0, 1)), new CallbackWebApi<ServicesListResponse>() {
                            @Override
                            public void success(ServicesListResponse response) {
                                if (!assertEquals(Long.valueOf(0), response.getPageNumber()))
                                    return;
                                if (!assertEquals(Long.valueOf(1), response.getPageCount()))
                                    return;
                                if (!assertEquals(Long.valueOf(1), response.getPageSize()))
                                    return;

                                List<Service> services = response.getServices();
                                if (!assertEquals(1, services.size()))
                                    return;

                                Service service = services.get(0);
                                if (!assertEquals("EB8816A9C0E29A47F564E0BC2F30F8BB5A2FDB84", service.getId()))
                                    return;
                                if (!assertEquals("SERVIS 24", service.getNameI18N()))
                                    return;
                                if (!assertEquals("S24", service.getIconGroup()))
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
        return "JServicesListTest";
    }

}
