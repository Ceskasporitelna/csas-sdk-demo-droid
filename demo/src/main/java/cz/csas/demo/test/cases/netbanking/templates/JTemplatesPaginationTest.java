package cz.csas.demo.test.cases.netbanking.templates;

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
import cz.csas.netbanking.templates.OrderCategory;
import cz.csas.netbanking.templates.Template;
import cz.csas.netbanking.templates.TemplatesListResponse;


/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JTemplatesPaginationTest extends TestCase {

    private final String X_JUDGE_CASE = "templates.list.pagination";
    private boolean flag = false;

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        final Map<String, String> headers = new HashMap<>();
                        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                        mNetbankingJudgeClient.setGlobalHeaders(headers);


                        int pages[] = {0, 1, 0};
                        for (int i = 0; i < pages.length; i++) {
                            final int page = pages[i];
                            if (i == 2)
                                flag = true;
                            mNetbankingJudgeClient.getTemplatesResource().list(new PaginatedParameters(new Pagination(page, 1)), new CallbackWebApi<TemplatesListResponse>() {
                                @Override
                                public void success(TemplatesListResponse templatesListResponse) {
                                    Template template = templatesListResponse.getTemplates().get(0);

                                    switch (page) {
                                        case 0:
                                            if (!assertEquals(1, templatesListResponse.getTemplates().size()))
                                                return;
                                            if (!assertEquals(Long.valueOf(0), templatesListResponse.getPageNumber()))
                                                return;
                                            if (!assertEquals(Long.valueOf(2), templatesListResponse.getPageCount()))
                                                return;
                                            if (!assertEquals(Long.valueOf(1), templatesListResponse.getPageSize()))
                                                return;
                                            if (!assertEquals(Long.valueOf(1), templatesListResponse.getNextPage()))
                                                return;

                                            if (!assertEquals("template_0-123-100", template.getId()))
                                                return;
                                            if (!assertEquals("Jan Novák", template.getName()))
                                                return;
                                            if (!assertEquals(OrderCategory.DOMESTIC, template.getOrderCategory()))
                                                return;
                                            if (!assertEquals("2326573123", template.getReceiver().getNumber()))
                                                return;
                                            if (!assertEquals("0800", template.getReceiver().getBankCode()))
                                                return;
                                            break;
                                        case 1:
                                            if (!assertEquals(1, templatesListResponse.getTemplates().size()))
                                                return;
                                            if (!assertEquals(Long.valueOf(1), templatesListResponse.getPageNumber()))
                                                return;
                                            if (!assertEquals(Long.valueOf(2), templatesListResponse.getPageCount()))
                                                return;
                                            if (!assertEquals(Long.valueOf(1), templatesListResponse.getPageSize()))
                                                return;

                                            if (!assertEquals("template_0-124-100", template.getId()))
                                                return;
                                            if (!assertEquals("Marek Nový", template.getName()))
                                                return;
                                            if (!assertEquals(OrderCategory.DOMESTIC, template.getOrderCategory()))
                                                return;
                                            if (!assertEquals("CZ3308000000001592286253", template.getReceiver().getIban()))
                                                return;
                                            if (!assertEquals("GIBACZPX", template.getReceiver().getBic()))
                                                return;
                                            break;
                                    }
                                    if (flag)
                                        mTestCallback.result(mTestResult);
                                }

                                @Override
                                public void failure(CsSDKError error) {
                                    handleError(error);
                                }
                            });
                        }
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
        return "JTemplatesPaginationTest";
    }

}
