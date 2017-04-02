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
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.templates.OrderCategory;
import cz.csas.netbanking.templates.Template;


/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JTemplatesWithIdGetTest extends TestCase {

    private final String X_JUDGE_CASE = "templates.withId.get";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String id = "template_0-123-100";
                mNetbankingJudgeClient.getTemplatesResource().withId(id).get(new CallbackWebApi<Template>() {
                    @Override
                    public void success(Template template) {

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
        return "JTemplatesWithIdGetTest";
    }

}
