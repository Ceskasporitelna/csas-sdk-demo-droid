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
import cz.csas.cscore.webapi.PaginatedParameters;
import cz.csas.cscore.webapi.Pagination;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.accounts.Reservation;
import cz.csas.netbanking.accounts.ReservationStatus;
import cz.csas.netbanking.accounts.ReservationsListResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JAccountsWithIdReservationsListPage1Test extends TestCase {

    private final String X_JUDGE_CASE = "accounts.withId.reservations.list.page1";

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
                PaginatedParameters parameters = new PaginatedParameters(new Pagination(1, 1));
                mNetbankingJudgeClient.getAccountsResource().withId(accountId).getReservationsResource().list(parameters,
                        new CallbackWebApi<ReservationsListResponse>() {
                            @Override
                            public void success(ReservationsListResponse reservationsListResponse) {

                                if (!assertEquals(Long.valueOf(1), reservationsListResponse.getPageNumber()))
                                    return;
                                if (!assertEquals(Long.valueOf(2), reservationsListResponse.getPageCount()))
                                    return;
                                if (!assertEquals(Long.valueOf(1), reservationsListResponse.getPageSize()))
                                    return;

                                List<Reservation> reservations = reservationsListResponse.getReservations();
                                if (!assertEquals(1, reservations.size()))
                                    return;

                                Reservation reservation = reservations.get(0);
                                if (!assertEquals(ReservationStatus.RESERVED, reservation.getStatus()))
                                    return;
                                if (!assertEquals(TimeUtils.getISO8601Date("2015-09-18T21:54:58Z"), reservation.getCreationDate()))
                                    return;
                                if (!assertEquals(TimeUtils.getISO8601Date("2015-09-25T21:54:58Z"), reservation.getExpirationDate()))
                                    return;
                                if (!assertEquals("AAA Taxi", reservation.getMerchantName()))
                                    return;
                                if (!assertEquals("Platba kartou", reservation.getDescription()))
                                    return;

                                if (!assertEquals(Long.valueOf(12750), reservation.getAmount().getValue()))
                                    return;
                                if (!assertEquals(Integer.valueOf(2), reservation.getAmount().getPrecision()))
                                    return;
                                if (!assertEquals("CZK", reservation.getAmount().getCurrency()))
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
        return "JAccountsWithIdReservationsListPage1Test";
    }

}
