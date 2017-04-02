package cz.csas.demo.test.cases.netbanking.messages;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cz.csas.cscore.client.rest.Callback;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeRestService;
import cz.csas.cscore.webapi.WebApiStream;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JMessagesWithIdDownloadTest extends TestCase {

    private final String X_JUDGE_CASE = "messages.withId.attachments.withId.download";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String id = "1421721";
                String attachmentId = "palec.png";
                mNetbankingJudgeClient.getMessagesResource().withId(id).getAttachmentsResource()
                        .withId(attachmentId).download(new CallbackWebApi<WebApiStream>() {
                    @Override
                    public void success(WebApiStream stream) {
                        try {
                            Long size = (long) stream.getInputStream().available();
                            assertEquals("test-file.png", stream.getFilename());
                            assertEquals(stream.getContentLength(), size);
                            assertEquals("application/octet-stream", stream.getContentType().toLowerCase());
                            assertPng(stream.getInputStream());
                            mTestCallback.result(mTestResult);
                        } catch (IOException e) {
                        }
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
        return "JMessagesWithIdDownloadTest";
    }

}
