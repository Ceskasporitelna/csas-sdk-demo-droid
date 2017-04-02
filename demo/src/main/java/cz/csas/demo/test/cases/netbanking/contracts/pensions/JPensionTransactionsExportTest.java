package cz.csas.demo.test.cases.netbanking.contracts.pensions;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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
import cz.csas.netbanking.ExportTransactionsParameters;
import cz.csas.netbanking.TransactionField;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JPensionTransactionsExportTest extends TestCase {

    private final String X_JUDGE_CASE = "contracts.pensions.withId.transactions.export";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String pensionId = "BCEF6B001FAE755D163A6CC9475E9FDFD9CD4A79";
                TimeZone.setDefault(TimeZone.getTimeZone("GMT+2"));

                ExportTransactionsParameters parameters = new ExportTransactionsParameters.Builder()
                        .setFields(Arrays.asList(TransactionField.BOOKING_DATE, TransactionField.PARTNER, TransactionField.AMOUNT, TransactionField.CURRENCY))
                        .setShowAccountName(true)
                        .setShowAccountNumber(true)
                        .setShowTimespan(true)
                        .setShowBalance(true)
                        .setDateFrom(TimeUtils.getISO8601Date("1999-09-27T00:00:00+02:00"))
                        .setDateTo(TimeUtils.getISO8601Date("2000-09-27T00:00:00+02:00"))
                        .build();
                mNetbankingJudgeClient.getContractsResource().getPensionsResource().withId(pensionId).getTransactionsResource()
                        .getExportResource().export(parameters, new CallbackWebApi<WebApiStream>() {
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
                            mTestCallback.result(mTestResult);
                        } catch (IOException e) {
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
        return "JPensionTransactionsExportTest";
    }

}
