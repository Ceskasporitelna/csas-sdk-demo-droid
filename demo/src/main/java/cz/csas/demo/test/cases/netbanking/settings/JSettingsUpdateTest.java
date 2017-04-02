package cz.csas.demo.test.cases.netbanking.settings;

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
import cz.csas.cscore.webapi.signing.SigningState;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.Language;
import cz.csas.netbanking.settings.SettingsUpdateRequest;
import cz.csas.netbanking.settings.SettingsUpdateResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JSettingsUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "settings.update";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        final Map<String, String> headers = new HashMap<>();
                        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                        mNetbankingJudgeClient.setGlobalHeaders(headers);

                        SettingsUpdateRequest request = new SettingsUpdateRequest(Language.CS,
                                Arrays.asList("displayInsurances", "displayBuildings"));

                        mNetbankingJudgeClient.getSettingsResource().update(request, new CallbackWebApi<SettingsUpdateResponse>() {
                            @Override
                            public void success(SettingsUpdateResponse settingsUpdateResponse) {
                                List<String> flags = new ArrayList<String>();
                                flags.add("displayInvestments");
                                flags.add("displayCreditCards");
                                flags.add("displayBuildings");
                                flags.add("displayInsurances");
                                if (!assertEquals(Language.CS, settingsUpdateResponse.getSettings().getLanguage()))
                                    return;
                                if (!assertEquals(flags, settingsUpdateResponse.getSettings().getFlags()))
                                    return;

                                if (!assertEquals(SigningState.NONE, settingsUpdateResponse.signing().getSigningState()))
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
                }

        );
    }

    @Override
    public String getName() {
        return "JSettingsUpdateTest";
    }

}
