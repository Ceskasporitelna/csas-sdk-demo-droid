package cz.csas.demo.test.cases.netbanking.accounts;

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
import cz.csas.netbanking.accounts.Repayment;
import cz.csas.netbanking.accounts.RepaymentsListResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JAccountWithIdRepaymentsListTest extends TestCase {

    private final String X_JUDGE_CASE = "accounts.withId.repayments.list";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        final Map<String, String> headers = new HashMap<>();
                        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                        mNetbankingJudgeClient.setGlobalHeaders(headers);

                        String accountId = "076E1DBCCCD38729A99D93AC8D3E8273237C7E36";

                        mNetbankingJudgeClient.getAccountsResource().withId(accountId).getRepaymentsResource().list(
                                new CallbackWebApi<RepaymentsListResponse>() {
                                    @Override
                                    public void success(RepaymentsListResponse repaymentsListResponse) {

                                        List<Repayment> repayments = repaymentsListResponse.getRepayments();
                                        assertEquals(2, repayments.size());

                                        for (int i = 0; i < repayments.size(); ++i) {
                                            Repayment repayment = repayments.get(i);
                                            switch (i) {
                                                case 0:
                                                    if (!assertEquals(TimeUtils.getPlainDate("2016-01-18"), repayment.getRepaymentDate()))
                                                        return;
                                                    if (!assertEquals(Long.valueOf(32500), repayment.getAmount().getValue()))
                                                        return;
                                                    if (!assertEquals(Integer.valueOf(2), repayment.getAmount().getPrecision()))
                                                        return;
                                                    if (!assertEquals("CZK", repayment.getAmount().getCurrency()))
                                                        return;
                                                    if (!assertEquals(Long.valueOf(32500), repayment.getPaidAmount().getValue()))
                                                        return;
                                                    if (!assertEquals(Integer.valueOf(2), repayment.getPaidAmount().getPrecision()))
                                                        return;
                                                    if (!assertEquals("CZK", repayment.getPaidAmount().getCurrency()))
                                                        return;
                                                    break;
                                                case 1:
                                                    if (!assertEquals(TimeUtils.getPlainDate("2016-02-18"), repayment.getRepaymentDate()))
                                                        return;
                                                    if (!assertEquals(Long.valueOf(32500), repayment.getAmount().getValue()))
                                                        return;
                                                    if (!assertEquals(Integer.valueOf(2), repayment.getAmount().getPrecision()))
                                                        return;
                                                    if (!assertEquals("CZK", repayment.getAmount().getCurrency()))
                                                        return;
                                                    if (!assertEquals(Long.valueOf(25000), repayment.getPaidAmount().getValue()))
                                                        return;
                                                    if (!assertEquals(Integer.valueOf(2), repayment.getPaidAmount().getPrecision()))
                                                        return;
                                                    if (!assertEquals("CZK", repayment.getPaidAmount().getCurrency()))
                                                        return;
                                                    break;
                                            }
                                        }
                                        mTestCallback.result(mTestResult);

                                    }

                                    @Override
                                    public void failure(CsSDKError error) {
                                        handleError(error);
                                    }
                                }

                        );
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
        return "JAccountWithIdRepaymentsListTest";
    }

}
