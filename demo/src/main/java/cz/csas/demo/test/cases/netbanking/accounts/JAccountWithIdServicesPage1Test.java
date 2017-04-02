package cz.csas.demo.test.cases.netbanking.accounts;

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
public class JAccountWithIdServicesPage1Test extends TestCase {

    private final String X_JUDGE_CASE = "accounts.withId.services.list.page1";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String accountId = "076E1DBCCCD38729A99D93AC8D3E8273237C7E36";
                PaginatedParameters parameters = new PaginatedParameters(new Pagination(1, 1));
                mNetbankingJudgeClient.getAccountsResource().withId(accountId).getServicesResource().list(parameters,
                        new CallbackWebApi<ServicesListResponse>() {
                            @Override
                            public void success(ServicesListResponse servicesListResponse) {

                                if (!assertEquals(Long.valueOf(1), servicesListResponse.getPageNumber()))
                                    return;
                                if (!assertEquals(Long.valueOf(4), servicesListResponse.getPageCount()))
                                    return;
                                if (!assertEquals(Long.valueOf(2), servicesListResponse.getPageSize()))
                                    return;
                                if (!assertEquals(Long.valueOf(2), servicesListResponse.getNextPage()))
                                    return;

                                List<Service> services = servicesListResponse.getServices();
                                if (!assertEquals(2, services.size()))

                                    for (int i = 0; i < services.size(); ++i) {
                                        Service service = services.get(i);
                                        switch (i) {
                                            case 0:
                                                if (!assertEquals("5F66602F35A7D5A86066BC03A6882180BEF01CA3", service.getId()))
                                                    return;
                                                if (!assertEquals("Všechny platby v Kč", service.getNameI18N()))
                                                    return;
                                                if (!assertEquals("PAYMENTS", service.getIconGroup()))
                                                    return;
                                                break;
                                            case 1:
                                                if (!assertEquals("A705433CFCD205249F4B816F2C63D309AEEFF4C9", service.getId()))
                                                    return;
                                                if (!assertEquals("Platební karta", service.getNameI18N()))
                                                    return;
                                                if (!assertEquals("CARDS", service.getIconGroup()))
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
        return "JAccountWithIdServicesPage1Test";
    }

}
