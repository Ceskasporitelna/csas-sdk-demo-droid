package cz.csas.demo.test.cases.netbanking.contracts.insurances;

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
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.contracts.insurances.ContractPayment;
import cz.csas.netbanking.contracts.insurances.ContractPaymentsListResponse;
import cz.csas.netbanking.contracts.insurances.PaymentType;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JInsurancePaymentsListTest extends TestCase {

    private final String X_JUDGE_CASE = "contracts.insurances.withId.payments.list";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String id = "3961D3F9E922EEE93E2581E896B34566645FE7E3";
                mNetbankingJudgeClient.getContractsResource().getInsurancesResource().withId(id).getPaymentsResource().list(new CallbackWebApi<ContractPaymentsListResponse>() {
                    @Override
                    public void success(ContractPaymentsListResponse contractPaymentsListResponse) {
                        List<String> flags = new ArrayList<String>();
                        flags.add("extraDepositAllowed");
                        flags.add("paymentExpected");
                        if (!assertEquals(flags, contractPaymentsListResponse.getFlags()))
                            return;
                        List<ContractPayment> payments = contractPaymentsListResponse.getPayments();
                        if (!assertEquals(2, payments.size()))
                            return;

                        for (int i = 0; i < payments.size(); ++i) {
                            ContractPayment payment = payments.get(i);
                            switch (i) {
                                case 0:
                                    if (!assertEquals("33", payment.getId()))
                                        return;
                                    if (!assertEquals(PaymentType.FUTURE, payment.getType()))
                                        return;
                                    if (!assertEquals(Long.valueOf(0), payment.getAmount().getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), payment.getAmount().getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", payment.getAmount().getCurrency()))
                                        return;
                                    if (!assertEquals(Long.valueOf(60000), payment.getRestToPay().getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), payment.getRestToPay().getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", payment.getRestToPay().getCurrency()))
                                        return;
                                    if (!assertEquals(Long.valueOf(60000), payment.getInstruction().getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), payment.getInstruction().getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", payment.getInstruction().getCurrency()))
                                        return;
                                    if (!assertEquals(TimeUtils.getPlainDate("2016-09-01"), payment.getInstructionFrom()))
                                        return;
                                    if (!assertEquals(TimeUtils.getPlainDate("2016-09-30"), payment.getInstructionTo()))
                                        return;
                                    break;
                                case 1:
                                    if (!assertEquals("32", payment.getId()))
                                        return;
                                    if (!assertEquals(PaymentType.OVERDUE, payment.getType()))
                                        return;
                                    if (!assertEquals(Long.valueOf(0), payment.getAmount().getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), payment.getAmount().getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", payment.getAmount().getCurrency()))
                                        return;
                                    if (!assertEquals(Long.valueOf(60000), payment.getRestToPay().getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), payment.getRestToPay().getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", payment.getRestToPay().getCurrency()))
                                        return;
                                    if (!assertEquals(Long.valueOf(60000), payment.getInstruction().getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), payment.getInstruction().getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", payment.getInstruction().getCurrency()))
                                        return;
                                    if (!assertEquals(TimeUtils.getPlainDate("2016-08-01"), payment.getInstructionFrom()))
                                        return;
                                    if (!assertEquals(TimeUtils.getPlainDate("2016-08-31"), payment.getInstructionTo()))
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
        return "JInsurancePaymentsListTest";
    }

}
