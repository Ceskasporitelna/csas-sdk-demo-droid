package cz.csas.demo.test.cases.netbanking.signing;

import java.util.ArrayList;
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
import cz.csas.cscore.webapi.signing.AuthorizationType;
import cz.csas.cscore.webapi.signing.FilledSigningObject;
import cz.csas.cscore.webapi.signing.SigningObject;
import cz.csas.cscore.webapi.signing.SigningState;
import cz.csas.cscore.webapi.signing.TacSigningProcess;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.orders.ApplicationId;
import cz.csas.netbanking.orders.ChannelId;
import cz.csas.netbanking.orders.DomesticPaymentUpdateRequest;
import cz.csas.netbanking.orders.DomesticPaymentUpdateResponse;
import cz.csas.netbanking.orders.Payment;
import cz.csas.netbanking.orders.PaymentCategory;
import cz.csas.netbanking.orders.PaymentOrderType;
import cz.csas.netbanking.orders.PaymentState;
import cz.csas.netbanking.orders.PaymentStateDetail;


/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JTacPaymentsDomesticUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "signing.tac.payments.domestic.withId.update";
    private DomesticPaymentUpdateResponse mResponse;

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);
                String requestId = "160429968927553";
                DomesticPaymentUpdateRequest request = new DomesticPaymentUpdateRequest.Builder()
                        .setSenderName("Vrba")
                        .setSender(new AccountNumber("2059930033", "0800"))
                        .setReceiverName("Vojtíšková Alena")
                        .setReceiver(new AccountNumber("2328489013", "0800"))
                        .setAmount(new Amount(110L, 2, "CZK"))
                        .build();

                mNetbankingJudgeClient.getOrdersResource().getPaymentsResource().getDomesticResource().withId(requestId)
                        .update(request, new CallbackWebApi<DomesticPaymentUpdateResponse>() {
                            @Override
                            public void success(DomesticPaymentUpdateResponse domesticPaymentUpdateResponse) {
                                mResponse = domesticPaymentUpdateResponse;

                                Payment payment = mResponse;
                                SigningObject signingObject = mResponse.signing();

                                if (!assertEquals("160429968927553", payment.getId()))
                                    return;
                                if (!assertEquals(Long.valueOf(110), payment.getAmount().getValue()))
                                    return;
                                if (!assertEquals(Integer.valueOf(2), payment.getAmount().getPrecision()))
                                    return;
                                if (!assertEquals("CZK", payment.getAmount().getCurrency()))
                                    return;
                                if (!assertEquals(ApplicationId.GEORGE, payment.getApplicationId()))
                                    return;
                                if (!assertEquals(ChannelId.NET_BANKING, payment.getChannelId()))
                                    return;
                                if (!assertEquals(TimeUtils.getISO8601Date("2016-04-14T15:39:48+02:00"), payment.getCzOrderingDate()))
                                    return;
                                if (!assertEquals(TimeUtils.getISO8601Date("2016-04-14T00:00:00+02:00"), payment.getExecutionDate()))
                                    return;
                                List<String> flags = new ArrayList<String>();
                                flags.add("editable");
                                flags.add("deletable");
                                if (!assertEquals(flags, payment.getFlags()))
                                    return;
                                if (!assertEquals(TimeUtils.getISO8601Date("2016-04-14T15:39:48+02:00"), payment.getModificationDate()))
                                    return;
                                if (!assertEquals(PaymentCategory.DOMESTIC, payment.getOrderCategory()))
                                    return;
                                if (!assertEquals(PaymentOrderType.PAYMENT_OUT, payment.getOrderType()))
                                    return;
                                if (!assertEquals("2059930033", payment.getReceiver().getNumber()))
                                    return;
                                if (!assertEquals("0800", payment.getReceiver().getBankCode()))
                                    return;
                                if (!assertEquals("CZ", payment.getReceiver().getCountryCode()))
                                    return;
                                if (!assertEquals("CZ1208000000002059930033", payment.getReceiver().getCzIban()))
                                    return;
                                if (!assertEquals("GIBACZPX", payment.getReceiver().getCzBic()))
                                    return;
                                if (!assertEquals("Vojtíšková", payment.getReceiverName()))
                                    return;

                                if (!assertEquals("2328489013", payment.getSender().getNumber()))
                                    return;
                                if (!assertEquals("0800", payment.getSender().getBankCode()))
                                    return;
                                if (!assertEquals("CZ", payment.getSender().getCountryCode()))
                                    return;
                                if (!assertEquals("CZ5908000000002328489013", payment.getSender().getCzIban()))
                                    return;
                                if (!assertEquals("GIBACZPX", payment.getSender().getCzBic()))
                                    return;
                                if (!assertEquals("Vrba", payment.getSenderName()))
                                    return;

                                if (!assertEquals("ba739d14b5371c959346c4607d37ad99ae05c607576e5d803f052224575e9e6c", payment.signing().getSignId()))
                                    return;
                                if (!assertEquals(SigningState.OPEN, payment.signing().getSigningState()))
                                    return;

                                if (!assertEquals(PaymentState.OPEN, payment.getState()))
                                    return;
                                if (!assertEquals(PaymentStateDetail.OPN, payment.getStateDetail()))
                                    return;
                                if (!assertEquals(Boolean.valueOf(true), payment.getStateOk()))
                                    return;
                                if (!assertEquals(TimeUtils.getPlainDate("2016-04-14"), payment.getTransferDate()))
                                    return;
                                callSigningGET(signingObject);
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
        return "JTacPaymentsDomesticUpdateTest";
    }


    private void callSigningGET(SigningObject signingObject) {
        signingObject.getInfo(new CallbackWebApi<FilledSigningObject>() {
            @Override
            public void success(FilledSigningObject filledSigningObject) {
                if (!assertEquals(AuthorizationType.TAC, filledSigningObject.getAuthorizationType()))
                    return;
                List<List<AuthorizationType>> scenarios = new ArrayList<List<AuthorizationType>>();
                List<AuthorizationType> scenario = new ArrayList<AuthorizationType>();
                scenario.add(AuthorizationType.TAC);
                scenarios.add(scenario);
                if (!assertEquals(scenarios, filledSigningObject.getScenarios()))
                    return;
                if (!assertEquals(SigningState.OPEN, filledSigningObject.getSigningState()))
                    return;
                if (!assertEquals("ba739d14b5371c959346c4607d37ad99ae05c607576e5d803f052224575e9e6c", filledSigningObject.getSignId()))
                    return;
                callSigningPOST(filledSigningObject);
            }

            @Override
            public void failure(CsSDKError error) {
                handleError(error);
            }
        });
    }

    private void callSigningPOST(FilledSigningObject filledSigningObject) {
        filledSigningObject.startSigningWithTac(new CallbackWebApi<TacSigningProcess>() {
            @Override
            public void success(TacSigningProcess tacSigningProcess) {
                if (!assertNotNull(tacSigningProcess, TacSigningProcess.class.getName()))
                    return;
                callSigningPUT(tacSigningProcess);

            }

            @Override
            public void failure(CsSDKError error) {
                handleError(error);
            }
        });
    }

    private void callSigningPUT(TacSigningProcess tacSigningProcess) {
        tacSigningProcess.finishSigning("00000000", new CallbackWebApi<SigningObject>() {
            @Override
            public void success(SigningObject signingObject) {
                if (!assertEquals("ba739d14b5371c959346c4607d37ad99ae05c607576e5d803f052224575e9e6c", signingObject.getSignId()))
                    return;
                if (!assertEquals(SigningState.DONE, signingObject.getSigningState()))
                    return;
                if (!assertEquals(SigningState.DONE, mResponse.signing().getSigningState()))
                    return;
                mTestCallback.result(mTestResult);
            }

            @Override
            public void failure(CsSDKError error) {
                handleError(error);
            }
        });
    }
}
