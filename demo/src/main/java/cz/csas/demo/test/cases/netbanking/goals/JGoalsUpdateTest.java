package cz.csas.demo.test.cases.netbanking.goals;

import java.util.Arrays;
import java.util.Date;
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
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.goals.Goal;
import cz.csas.netbanking.goals.GoalsListResponse;
import cz.csas.netbanking.goals.GoalsUpdateRequest;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JGoalsUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "goals.update";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                GoalsUpdateRequest request = new GoalsUpdateRequest(Arrays.asList(
                        new Goal("Auto", new Amount(4000l, 2, "CZK"), new Date(1445554800000l), false)
                ));
                mNetbankingJudgeClient.getGoalsResource().update(request, new CallbackWebApi<GoalsListResponse>() {
                    @Override
                    public void success(GoalsListResponse goalsListResponse) {
                        List<Goal> goals = goalsListResponse.getGoals();
                        if (!assertEquals(1, goals.size()))
                            return;
                        Goal goal = goals.get(0);

                        if (!assertEquals("Auto", goal.getName()))
                            return;
                        if (!assertEquals(Long.valueOf(4000), goal.getPrice().getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), goal.getPrice().getPrecision()))
                            return;
                        if (!assertEquals("CZK", goal.getPrice().getCurrency()))
                            return;
                        if (!assertEquals(new Date(1445554800000l), goal.getDeadline()))
                            return;
                        if (!assertEquals(Boolean.valueOf(false), goal.getCompleted()))
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
        return "JGoalsUpdateTest";
    }

}
