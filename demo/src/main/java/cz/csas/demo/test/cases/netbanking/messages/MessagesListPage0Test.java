package cz.csas.demo.test.cases.netbanking.messages;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.Pagination;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.messages.MessagesListResponse;
import cz.csas.netbanking.messages.MessagesParameters;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class MessagesListPage0Test extends TestCase {

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;

        mNetbankingClient.getMessagesResource().list(new MessagesParameters(new Pagination(0, 1), null), new CallbackWebApi<MessagesListResponse>() {
            @Override
            public void success(MessagesListResponse messagesListResponse) {
                mTestResult.setStatus(TestStatus.OK);
                mTestCallback.result(mTestResult);
            }

            @Override
            public void failure(CsSDKError error) {
                handleError(error);
            }
        });
    }

    @Override
    public String getName() {
        return "MessagesListPage0Test";
    }

}
