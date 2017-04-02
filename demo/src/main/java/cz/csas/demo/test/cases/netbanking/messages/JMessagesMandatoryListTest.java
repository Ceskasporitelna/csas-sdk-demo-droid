package cz.csas.demo.test.cases.netbanking.messages;

import java.util.ArrayList;
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
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.messages.Attachment;
import cz.csas.netbanking.messages.Message;
import cz.csas.netbanking.messages.MessagesListResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JMessagesMandatoryListTest extends TestCase {

    private final String X_JUDGE_CASE = "messages.mandatory.list";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                mNetbankingJudgeClient.getMessagesResource().getMandatoryResource().list(new CallbackWebApi<MessagesListResponse>() {
                    @Override
                    public void success(MessagesListResponse messagesListResponse) {
                        List<String> flags = new ArrayList<String>();
                        flags.add("mandatory");
                        List<Message> messages = messagesListResponse.getMessages();
                        if (!assertEquals(1, messages.size()))
                            return;

                        Message message = messages.get(0);
                        List<Attachment> attachments = message.getAttachments();

                        if (!assertEquals("278583", message.getId()))
                            return;
                        if (!assertEquals("WCM", message.getFrom()))
                            return;
                        if (!assertEquals("Pozor - evidence dluhu na Vašem úvěrovém případně osobním účtu! Hrozí naúčtování poplatků", message.getSubject()))
                            return;
                        if (!assertEquals("<html>\n <head></head>\n <body>\n  <p>Vážená paní/Vážený pane,</p>\n  <p>dovolujeme si Váš upozornit, že u Vás evidujeme dlužnou částku po splatnosti nebo nepovolený záporný zůstatek na osobním účtu. Uhraďte, prosím, tuto dlužnou částku co nejdříve. Zastavíte si tak navyšování úroku z prodlení.</p>\n  <p>Pokud byla dlužná částka již uhrazena, děkujeme Vám a této naší zprávy&nbsp;si nevšímejte.</p>\n  <p>.....................</p>\n  <p>Česká spořitelna</p> \n </body>\n</html>", message.getBody()))
                            return;
                        if (!assertEquals(flags, message.getFlags()))
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
        return "JMessagesMandatoryListTest";
    }

}
