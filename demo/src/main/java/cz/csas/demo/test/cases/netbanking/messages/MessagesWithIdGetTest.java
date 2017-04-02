package cz.csas.demo.test.cases.netbanking.messages;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.messages.Message;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class MessagesWithIdGetTest extends TestCase {

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;

        String id = "4410486";

        mNetbankingClient.getMessagesResource().withId(id).get(new CallbackWebApi<Message>() {
            @Override
            public void success(Message message) {
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
        return "MessagesWithIdGetTest";
    }

}
