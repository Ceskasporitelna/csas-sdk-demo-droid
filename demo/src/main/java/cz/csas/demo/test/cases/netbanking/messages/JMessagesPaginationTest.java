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
import cz.csas.cscore.utils.TimeUtils;
import cz.csas.cscore.webapi.Pagination;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.messages.Attachment;
import cz.csas.netbanking.messages.Message;
import cz.csas.netbanking.messages.MessagesListResponse;
import cz.csas.netbanking.messages.MessagesParameters;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JMessagesPaginationTest extends TestCase {

    private final String X_JUDGE_CASE = "messages.pagination";
    private boolean flag = false;

    @Override
    public void run(final TestCallback callback) {
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
                    mNetbankingJudgeClient.getMessagesResource().list(new MessagesParameters(new Pagination(page, 1), null), new CallbackWebApi<MessagesListResponse>() {
                        @Override
                        public void success(MessagesListResponse messagesListResponse) {
                            switch (page) {
                                case 0:
                                    Message message = messagesListResponse.getMessages().get(0);
                                    if (!assertEquals(1, messagesListResponse.getMessages().size()))
                                        return;
                                    if (!assertEquals(Long.valueOf(0), messagesListResponse.getPageNumber()))
                                        return;
                                    if (!assertEquals(Long.valueOf(5), messagesListResponse.getPageCount()))
                                        return;
                                    if (!assertEquals(Long.valueOf(1), messagesListResponse.getNextPage()))
                                        return;
                                    if (!assertEquals(Long.valueOf(1), messagesListResponse.getPageSize()))
                                        return;

                                    List<Attachment> attachments = message.getAttachments();
                                    if (!assertEquals("134625", message.getId()))
                                        return;
                                    if (!assertEquals("WCM", message.getFrom()))
                                        return;
                                    if (!assertEquals("test attach", message.getSubject()))
                                        return;
                                    if (!assertEquals(TimeUtils.getISO8601Date("2016-04-08T09:20:32+02:00"), message.getDate()))
                                        return;
                                    if (!assertEquals(1, attachments.size()))
                                        return;
                                    Attachment attachment = attachments.get(0);
                                    if (!assertEquals("jellyfishgrg.jpg", attachment.getId()))
                                        return;
                                    if (!assertEquals("jellyfishgrg.jpg", attachment.getFileName()))
                                        return;
                                    break;
                                case 1:
                                    List<String> flags = new ArrayList<String>();
                                    flags.add("mandatory");
                                    message = messagesListResponse.getMessages().get(0);
                                    if (!assertEquals(1, messagesListResponse.getMessages().size()))
                                        return;
                                    if (!assertEquals(Long.valueOf(1), messagesListResponse.getPageNumber()))
                                        return;
                                    if (!assertEquals(Long.valueOf(5), messagesListResponse.getPageCount()))
                                        return;
                                    if (!assertEquals(Long.valueOf(2), messagesListResponse.getNextPage()))
                                        return;
                                    if (!assertEquals(Long.valueOf(1), messagesListResponse.getPageSize()))
                                        return;

                                    if (!assertEquals("278583", message.getId()))
                                        return;
                                    if (!assertEquals("WCM", message.getFrom()))
                                        return;
                                    if (!assertEquals("Pozor - evidence dluhu na Vašem úvěrovém případně osobním účtu! Hrozí naúčtování poplatků", message.getSubject()))
                                        return;
                                    if (!assertEquals(TimeUtils.getISO8601Date("2016-04-27T08:20:32+02:00"), message.getDate()))
                                        return;
                                    if (!assertEquals("<html>\n <head></head>\n <body>\n  <p>Vážená paní/Vážený pane,</p>\n  <p>dovolujeme si Váš upozornit, že u Vás evidujeme dlužnou částku po splatnosti nebo nepovolený záporný zůstatek na osobním účtu. Uhraďte, prosím, tuto dlužnou částku co nejdříve. Zastavíte si tak navyšování úroku z prodlení.</p>\n  <p>Pokud byla dlužná částka již uhrazena, děkujeme Vám a této naší zprávy&nbsp;si nevšímejte.</p>\n  <p>.....................</p>\n  <p>Česká spořitelna</p> \n </body>\n</html>", message.getBody()))
                                        return;
                                    if (!assertEquals(flags, message.getFlags()))
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
        });
    }

    @Override
    public String getName() {
        return "JMessagesPaginationTest";
    }

}
