package cz.csas.demo.test.cases.netbanking.profile;

import java.util.HashMap;
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
import cz.csas.netbanking.profile.Gender;
import cz.csas.netbanking.profile.MarketingInfoAcceptance;
import cz.csas.netbanking.profile.Profile;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JProfileGetTest extends TestCase {

    private final String X_JUDGE_CASE = "profile.get";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);
                mNetbankingJudgeClient.getProfileResource().get(new CallbackWebApi<Profile>() {
                    @Override
                    public void success(Profile profile) {
                        if (!assertEquals("Anna", profile.getFirstName()))
                            return;
                        if (!assertEquals("Vojtíšková", profile.getLastName()))
                            return;
                        if (!assertEquals("2002-12-02-00.17.40.959689", profile.getCustomerId()))
                            return;
                        if (!assertEquals(Integer.valueOf(1), profile.getInstituteId()))
                            return;
                        if (!assertEquals(MarketingInfoAcceptance.UNKNOWN, profile.getMarketingInfoAcceptance()))
                            return;
                        if (!assertEquals(Gender.FEMALE, profile.getGender()))
                            return;
                        if (!assertEquals(TimeUtils.getISO8601Date("2016-03-17T15:01:49+01:00"), profile.getLastLogin()))
                            return;
                        if (!assertEquals("Anno Vojtíšková", profile.getSalutation()))
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
        return "JProfileGetTest";
    }

}
