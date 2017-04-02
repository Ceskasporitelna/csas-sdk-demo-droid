package cz.csas.demo.test.cases.netbanking.contracts.buildings;

import java.util.HashMap;
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
public class JBuildingsWithIdServicesListTest extends TestCase {

    private final String X_JUDGE_CASE = "contracts.buildings.withId.services.list";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String id = "BCEF6B001FAE755D163A6CC9475E9FDFD9CD4A79";
                String alias = "test alias";
                mNetbankingJudgeClient.getContractsResource().getBuildingsResource().withId(id).getServicesResource().list(
                        new PaginatedParameters(new Pagination(0, 1)), new CallbackWebApi<ServicesListResponse>() {
                            @Override
                            public void success(ServicesListResponse response) {
                                for (int i = 0; i < response.getServices().size(); ++i) {
                                    Service service = response.getServices().get(i);
                                    switch (i) {
                                        case 0:
                                            if (!assertEquals("s54sdf756dfhm52879sdf23xd8744Fsdf5", service.getId()))
                                                return;
                                            if (!assertEquals("Uver k stavebnimu sporeni", service.getNameI18N()))
                                                return;
                                            if (!assertEquals("DEFAULT", service.getIconGroup()))
                                                return;
                                            break;

                                        case 1:
                                            if (!assertEquals("154dff756dfhm52879sdf23d845sd4f84f", service.getId()))
                                                return;
                                            if (!assertEquals("Preklenovaci uver", service.getNameI18N()))
                                                return;
                                            if (!assertEquals("DEFAULT", service.getIconGroup()))
                                                return;
                                            break;
                                    }
                                }
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
        return "JBuildingsWithIdServicesListTest";
    }

}
