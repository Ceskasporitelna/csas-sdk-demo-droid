package cz.csas.demo.test.cases.netbanking.budgets;

import java.util.Arrays;
import java.util.HashMap;
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
import cz.csas.netbanking.budgets.BudgetsUpdateRequest;
import cz.csas.netbanking.budgets.Category;
import cz.csas.netbanking.budgets.CategoryId;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JBudgetsUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "budgets.update";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                Budget requestCategory = new Budget(
                        new Category(CategoryId.OTHER_EXPENSES, "mainCategory"),
                        new Amount(5000l, 2, "CZK"));
                BudgetsUpdateRequest request = new BudgetsUpdateRequest(Arrays.asList(requestCategory));
                mNetbankingJudgeClient.getBudgetsResource().update(request, new CallbackWebApi<BudgetsListResponse>() {
                    @Override
                    public void success(BudgetsListResponse budgetsListResponse) {
                        if (!assertEquals(1, budgetsListResponse.getBudgets().size()))
                            return;
                        Budget budget = budgetsListResponse.getBudgets().get(0);
                        Category category = budget.getCategory();
                        Amount amount = budget.getBudget();

                        if (!assertEquals(CategoryId.OTHER_EXPENSES, category.getId()))
                            return;
                        if (!assertEquals("mainCategory", category.getLevel()))
                            return;
                        if (!assertEquals(Long.valueOf(5000), amount.getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), amount.getPrecision()))
                            return;
                        if (!assertEquals("CZK", amount.getCurrency()))
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
        return "JBudgetsUpdateTest";
    }

}
