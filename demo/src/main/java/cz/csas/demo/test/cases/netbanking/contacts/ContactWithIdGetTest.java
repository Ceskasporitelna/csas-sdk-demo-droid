package cz.csas.demo.test.cases.netbanking.contacts;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.contacts.Contact;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class ContactWithIdGetTest extends TestCase {

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;

        String id = "postaladdresspermanent";

        mNetbankingClient.getContactsResource().withId(id).get(new CallbackWebApi<Contact>() {
            @Override
            public void success(Contact contact) {
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
        return "ContactWithIdGetTest";
    }

}
