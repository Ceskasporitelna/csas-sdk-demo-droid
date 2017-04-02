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
import cz.csas.cscore.webapi.signing.AuthorizationType;
import cz.csas.cscore.webapi.signing.FilledSigningObject;
import cz.csas.cscore.webapi.signing.SigningObject;
import cz.csas.cscore.webapi.signing.SigningState;
import cz.csas.cscore.webapi.signing.TacSigningProcess;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.orders.MobilePaymentRequest;
import cz.csas.netbanking.orders.MobilePaymentResponse;
import cz.csas.netbanking.orders.MobilePaymentSender;
import cz.csas.netbanking.orders.MobilePaymentType;


/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JTacPaymentsMobileCreateTest extends TestCase {

    private final String X_JUDGE_CASE = "signing.tac.orders.payments.mobile.create";
    private MobilePaymentResponse mResponse;

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);
                MobilePaymentRequest request = new MobilePaymentRequest.Builder()
                        .setPaymentType(MobilePaymentType.VODAFONE_PAYMENT)
                        .setPhoneNumber("777952341")
                        .setSender(new MobilePaymentSender("2059930033", "0800", "CZ", "CZ1208000000002059930033", "GIBACZPX"))
                        .setAmount(new Amount(3000L, 0, "CZK"))
                        .setConfirmationPhoneNumber("777952341")
                        .build();

                mNetbankingJudgeClient.getOrdersResource().getPaymentsResource().getMobileResource().create(request, new CallbackWebApi<MobilePaymentResponse>() {
                    @Override
                    public void success(MobilePaymentResponse mobilePaymentResponse) {
                        mResponse = mobilePaymentResponse;

                        SigningObject signingObject = mResponse.signing();
                        if (!assertEquals(SigningState.OPEN, signingObject.getSigningState()))
                            return;
                        if (!assertEquals("1671744209", signingObject.getSignId()))
                            return;

                        if (!assertEquals(MobilePaymentType.VODAFONE_PAYMENT, mResponse.getPaymentType()))
                            return;
                        if (!assertEquals("777952341", mResponse.getPhoneNumber()))
                            return;
                        if (!assertEquals("2059930033", mResponse.getSender().getNumber()))
                            return;
                        if (!assertEquals("0800", mResponse.getSender().getBankCode()))
                            return;
                        if (!assertEquals("CZ", mResponse.getSender().getCountryCode()))
                            return;
                        if (!assertEquals("CZ1208000000002059930033", mResponse.getSender().getIban()))
                            return;
                        if (!assertEquals("GIBACZPX", mResponse.getSender().getBic()))
                            return;
                        if (!assertEquals(Long.valueOf(300000L), mResponse.getAmount().getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), mResponse.getAmount().getPrecision()))
                            return;
                        if (!assertEquals("CZK", mResponse.getAmount().getCurrency()))
                            return;
                        if (!assertEquals("777952341", mResponse.getConfirmationPhoneNumber()))
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
        return "JTacPaymentsMobileCreateTest";
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
                if (!assertEquals("1671744209", filledSigningObject.getSignId()))
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
                if (!assertEquals("1671744209", signingObject.getSignId()))
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
