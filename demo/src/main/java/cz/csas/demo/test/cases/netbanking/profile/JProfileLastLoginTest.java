package cz.csas.demo.test.cases.netbanking.profile;

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
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.profile.LastLoginInfo;
import cz.csas.netbanking.profile.LastLoginListResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JProfileLastLoginTest extends TestCase {

    private final String X_JUDGE_CASE = "profile.lastLogin.list";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);
                mNetbankingJudgeClient.getProfileResource().getLastLogins().list(new CallbackWebApi<LastLoginListResponse>() {
                    @Override
                    public void success(LastLoginListResponse lastLoginListResponse) {
                        List<LastLoginInfo> infoList = lastLoginListResponse.getLastLogin();
                        if (!assertEquals(1, infoList.size()))
                            return;
                        if (!assertEquals(TimeUtils.getISO8601Date("2016-03-17T15:01:49+01:00"), infoList.get(0).getLastLogin()))
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
        return "JProfileLastLoginTest";
    }

}
