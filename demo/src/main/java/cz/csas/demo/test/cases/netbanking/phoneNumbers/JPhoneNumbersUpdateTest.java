package cz.csas.demo.test.cases.netbanking.phoneNumbers;

import java.util.ArrayList;
import java.util.Arrays;
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
import cz.csas.netbanking.phoneNumbers.PhoneNumber;
import cz.csas.netbanking.phoneNumbers.PhoneNumberUpdateRequest;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JPhoneNumbersUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "phoneBook.withId.update";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                PhoneNumberUpdateRequest request = new PhoneNumberUpdateRequest("Graham B.", "777952341", Arrays.asList("isFavourite"));
                mNetbankingJudgeClient.getPhoneNumbersResource().withId("2195").update(request, new CallbackWebApi<PhoneNumber>() {
                    @Override
                    public void success(PhoneNumber phoneNumber) {
                        List<String> flags = new ArrayList<String>();
                        flags.add("isFavourite");
                        if (!assertEquals("2195", phoneNumber.getId()))
                            return;
                        if (!assertEquals("Graham B.", phoneNumber.getAlias()))
                            return;
                        if (!assertEquals("777952341", phoneNumber.getPhoneNumber()))
                            return;
                        if (!assertEquals(flags, phoneNumber.getFlags()))
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
        return "JPhoneNumbersUpdateTest";
    }

}
