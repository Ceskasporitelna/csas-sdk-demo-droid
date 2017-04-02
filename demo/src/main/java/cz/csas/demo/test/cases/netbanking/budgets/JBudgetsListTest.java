package cz.csas.demo.test.cases.netbanking.budgets;

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
import cz.csas.netbanking.budgets.Budget;
import cz.csas.netbanking.budgets.BudgetsListResponse;
import cz.csas.netbanking.budgets.Category;
import cz.csas.netbanking.budgets.CategoryId;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JBudgetsListTest extends TestCase {

    private final String X_JUDGE_CASE = "budgets.list";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                mNetbankingJudgeClient.getBudgetsResource().list(new CallbackWebApi<BudgetsListResponse>() {
                    @Override
                    public void success(BudgetsListResponse budgetsListResponse) {
                        List<Budget> categories = budgetsListResponse.getBudgets();
                        if (!assertEquals(3, categories.size()))
                            return;
                        if (!assertEquals(Boolean.valueOf(true), budgetsListResponse.getManuallySet()))
                            return;

                        for (int i = 0; i < categories.size(); i++) {
                            Budget budget = categories.get(i);
                            Category category = budget.getCategory();
                            Amount amount = budget.getBudget();
                            switch (i) {
                                case 0:
                                    if (!assertEquals(CategoryId.CAR, category.getId()))
                                        return;
                                    if (!assertEquals("mainCategory", category.getLevel()))
                                        return;
                                    if (!assertEquals(Long.valueOf(2000000), amount.getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), amount.getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", amount.getCurrency()))
                                        return;
                                    break;
                                case 1:
                                    if (!assertEquals(CategoryId.WITHDRAWAL, category.getId()))
                                        return;
                                    if (!assertEquals("mainCategory", category.getLevel()))
                                        return;
                                    if (!assertNull(amount, "amount"))
                                        return;
                                    break;
                                case 2:
                                    if (!assertEquals(CategoryId.CLOTHING, category.getId()))
                                        return;
                                    if (!assertEquals("mainCategory", category.getLevel()))
                                        return;
                                    if (!assertNull(amount, "amount"))
                                        return;
                                    break;
                            }
                            mTestCallback.result(mTestResult);
                        }
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
        return "JBudgetsListTest";
    }

}
