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
public class JAccountWithIdServicesPage0Test extends TestCase {

    private final String X_JUDGE_CASE = "accounts.withId.services.list.page0";

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
                PaginatedParameters parameters = new PaginatedParameters(new Pagination(0, 1));
                mNetbankingJudgeClient.getAccountsResource().withId(accountId).getServicesResource().list(parameters,
                        new CallbackWebApi<ServicesListResponse>() {
                            @Override
                            public void success(ServicesListResponse servicesListResponse) {

                                if (!assertEquals(Long.valueOf(0), servicesListResponse.getPageNumber()))
                                    return;
                                if (!assertEquals(Long.valueOf(4), servicesListResponse.getPageCount()))
                                    return;
                                if (!assertEquals(Long.valueOf(2), servicesListResponse.getPageSize()))
                                    return;
                                if (!assertEquals(Long.valueOf(1), servicesListResponse.getNextPage()))
                                    return;

                                List<Service> services = servicesListResponse.getServices();
                                if (!assertEquals(2, services.size()))

                                    for (int i = 0; i < services.size(); ++i) {
                                        Service service = services.get(i);
                                        switch (i) {
                                            case 0:
                                                if (!assertEquals("E878D16AD1A79FB60A520F48706C187AEFCA9D5D", service.getId()))
                                                    return;
                                                if (!assertEquals("2x výběr z bankomatů České spořitelny", service.getNameI18N()))
                                                    return;
                                                if (!assertEquals("CARDS", service.getIconGroup()))
                                                    return;
                                                break;
                                            case 1:
                                                if (!assertEquals("3FB37388FC58076DEAD3DE282E075592A299B596", service.getId()))
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
        return "JAccountWithIdServicesPage0Test";
    }

}
