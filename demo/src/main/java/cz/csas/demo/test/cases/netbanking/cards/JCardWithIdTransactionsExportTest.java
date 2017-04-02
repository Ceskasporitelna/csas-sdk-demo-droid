package cz.csas.demo.test.cases.netbanking.cards;

import java.io.IOException;
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
import cz.csas.cscore.utils.TimeUtils;
import cz.csas.cscore.webapi.WebApiStream;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.TransactionField;
import cz.csas.netbanking.ExportTransactionsParameters;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JCardWithIdTransactionsExportTest extends TestCase {

    private final String X_JUDGE_CASE = "cards.withId.transactions.export";

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

                ExportTransactionsParameters parameters = new ExportTransactionsParameters.Builder()
                        .setFields(Arrays.asList(TransactionField.BOOKING_DATE, TransactionField.PARTNER,
                                TransactionField.AMOUNT, TransactionField.CURRENCY))
                        .setShowAccountName(true)
                        .setShowAccountNumber(true)
                        .setShowTimespan(true)
                        .setShowBalance(true)
                        .setDateFrom(TimeUtils.getISO8601Date("1999-09-27T00:00:00+02:00"))
                        .setDateTo(TimeUtils.getISO8601Date("2000-09-27T00:00:00+02:00"))
                        .build();

                mNetbankingJudgeClient.getCardsResource().withId(cardId).getTransactionsResource().getExportResource()
                        .export(parameters, new CallbackWebApi<WebApiStream>() {
                            @Override
                            public void success(WebApiStream webApiStream) {
                                try {
                                    Long size = (long) webApiStream.getInputStream().available();
                                    if (!assertEquals("test-pdf.pdf", webApiStream.getFilename()))
                                        return;
                                    if (!assertEquals(webApiStream.getContentLength(), size))
                                        return;
                                    if (!assertEquals("application/pdf", webApiStream.getContentType().toLowerCase()))
                                        return;
                                    if (!assertPdf(webApiStream.getInputStream()))
                                        return;
                                } catch (IOException e) {
                                    mTestResult.setStatus(TestStatus.FAILURE);
                                    mTestResult.setResult(e.getLocalizedMessage());
                                }
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
        return "JCardWithIdTransactionsExportTest";
    }

}
