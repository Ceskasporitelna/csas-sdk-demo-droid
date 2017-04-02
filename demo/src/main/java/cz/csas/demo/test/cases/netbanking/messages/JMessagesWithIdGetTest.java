package cz.csas.demo.test.cases.netbanking.messages;

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
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.messages.Attachment;
import cz.csas.netbanking.messages.Message;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JMessagesWithIdGetTest extends TestCase {

    private final String X_JUDGE_CASE = "messages.withId.get";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String id = "134625";
                mNetbankingJudgeClient.getMessagesResource().withId(id).get(new CallbackWebApi<Message>() {
                    @Override
                    public void success(Message message) {
                        List<Attachment> attachments = message.getAttachments();

                        if (!assertEquals("134625", message.getId()))
                            return;
                        if (!assertEquals("WCM", message.getFrom()))
                            return;
                        if (!assertEquals("test attach", message.getSubject()))
                            return;
                        if (!assertEquals(TimeUtils.getISO8601Date("2016-04-08T09:20:32+02:00"), message.getDate()))
                            return;
                        if (!assertEquals("<html>\n <head></head>\n <body>\n  <p>asdfasdf</p> \n </body>\n</html>", message.getBody()))
                            return;
                        if (!assertEquals(1, attachments.size()))
                            return;
                        Attachment attachment = attachments.get(0);
                        if (!assertEquals("jellyfishgrg.jpg", attachment.getId()))
                            return;
                        if (!assertEquals("jellyfishgrg.jpg", attachment.getFileName()))
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
        return "JMessagesWithIdGetTest";
    }

}
