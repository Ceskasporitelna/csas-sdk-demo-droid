package cz.csas.demo.test.cases.netbanking.cards;

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
import cz.csas.cscore.webapi.Pagination;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.Format;
import cz.csas.netbanking.Language;
import cz.csas.netbanking.Periodicity;
import cz.csas.netbanking.Statement;
import cz.csas.netbanking.StatementsListResponse;
import cz.csas.netbanking.StatementsParameters;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JCardAccountsStatementsListPage0Test extends TestCase {

    private final String X_JUDGE_CASE = "cards.withId.accounts.withId.statements.list.page0";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String cardId = "33A813886442D946122C78305EC4E482DE9F574D";
                String accountId = "076E1DBCCCD38729A99D93AC8D3E8273237C7E36";

                StatementsParameters parameters = new StatementsParameters(new Pagination(0, 1), null);

                mNetbankingJudgeClient.getCardsResource().withId(cardId).getAccountsResource().withId(accountId)
                        .getStatements().list(parameters, new CallbackWebApi<StatementsListResponse>() {
                    @Override
                    public void success(StatementsListResponse statementsListResponse) {

                        if (!assertEquals(Long.valueOf(0), statementsListResponse.getPageNumber()))
                            return;
                        if (!assertEquals(Long.valueOf(2), statementsListResponse.getPageCount()))
                            return;
                        if (!assertEquals(Long.valueOf(1), statementsListResponse.getPageSize()))
                            return;
                        if (!assertEquals(Long.valueOf(1), statementsListResponse.getNextPage()))
                            return;

                        List<Statement> statements = statementsListResponse.getStatements();
                        if (!assertEquals(1, statements.size()))
                            return;

                        Statement statement = statements.get(0);
                        if (!assertEquals("06029392819b0198", statement.getId()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), statement.getNumber()))
                            return;
                        if (!assertEquals(Periodicity.MONTHLY, statement.getPeriodicity()))
                            return;
                        if (!assertEquals(Format.PDF_A4, statement.getFormat()))
                            return;
                        if (!assertEquals(Language.CS, statement.getLanguage()))
                            return;
                        if (!assertEquals(TimeUtils.getISO8601Date("2016-02-29T00:00:00+01:00"), statement.getStatementDate()))
                            return;
                        if (!assertEquals(Integer.valueOf(1), statement.getCzFileTotalNumber()))
                            return;
                        if (!assertEquals(Integer.valueOf(0), statement.getCzFileOrderNumber()))
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
        return "JCardAccountsStatementsListPage0Test";
    }

}
