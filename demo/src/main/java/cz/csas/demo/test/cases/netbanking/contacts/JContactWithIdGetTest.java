package cz.csas.demo.test.cases.netbanking.contacts;

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
import cz.csas.netbanking.contacts.Contact;
import cz.csas.netbanking.contacts.ContactAddress;
import cz.csas.netbanking.contacts.ContactAddressType;
import cz.csas.netbanking.contacts.ContactType;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JContactWithIdGetTest extends TestCase {

    private final String X_JUDGE_CASE = "contacts.withId.get";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String id = "postaladdresspermanent";
                mNetbankingJudgeClient.getContactsResource().withId(id).get(new CallbackWebApi<Contact>() {
                    @Override
                    public void success(Contact contact) {
                        List<String> flags = new ArrayList<String>();
                        flags.add("mainContact");
                        if (!assertEquals("postaladdresspermanent", contact.getId()))
                            return;
                        if (!assertEquals(ContactType.ADDRESS, contact.getType()))
                            return;
                        if (!assertEquals(flags, contact.getFlags()))
                            return;
                        ContactAddress address = contact.getAddress();
                        if (!assertEquals(ContactAddressType.PERMANENT_RESIDENCE, address.getType()))
                            return;
                        if (!assertEquals("Trvalá adresa", address.getTypeI18N()))
                            return;
                        if (!assertEquals("CZ", address.getCountry()))
                            return;
                        if (!assertEquals("Rakovník", address.getCity()))
                            return;
                        if (!assertEquals("Pod Václavem", address.getStreet()))
                            return;
                        if (!assertEquals("2092", address.getBuildingApartment()))
                            return;
                        if (!assertEquals("26901", address.getZipCode()))
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
        return "JContactWithIdGetTest";
    }

}
