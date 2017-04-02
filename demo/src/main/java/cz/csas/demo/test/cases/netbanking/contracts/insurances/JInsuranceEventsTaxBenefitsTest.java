package cz.csas.demo.test.cases.netbanking.contracts.insurances;

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
import cz.csas.netbanking.contracts.insurances.PaymentTemplate;
import cz.csas.netbanking.contracts.insurances.TaxBenefit;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JInsuranceEventsTaxBenefitsTest extends TestCase {

    private final String X_JUDGE_CASE = "contracts.insurances.withId.taxBenefits.get";

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

                mNetbankingJudgeClient.getContractsResource().getInsurancesResource().withId(id).getTaxBenefitsResource()
                        .get(new CallbackWebApi<TaxBenefit>() {
                            @Override
                            public void success(TaxBenefit taxBenefitsListResponse) {
                                TaxBenefit benefit = taxBenefitsListResponse;
                                if (!assertEquals(Long.valueOf(29000), benefit.getTaxDeductiblePremium().getValue()))
                                    return;
                                if (!assertEquals(Integer.valueOf(2), benefit.getTaxDeductiblePremium().getPrecision()))
                                    return;
                                if (!assertEquals("CZK", benefit.getTaxDeductiblePremium().getCurrency()))
                                    return;
                                if (!assertEquals(Long.valueOf(1190400), benefit.getRecommendedDeposit().getValue()))
                                    return;
                                if (!assertEquals(Integer.valueOf(2), benefit.getRecommendedDeposit().getPrecision()))
                                    return;
                                if (!assertEquals("CZK", benefit.getRecommendedDeposit().getCurrency()))
                                    return;
                                if (!assertEquals("za předpokladu doplacení 4 splátek po 600 Kč v roce 2016", benefit.getRecommendedDepositText()))
                                    return;
                                PaymentTemplate template = benefit.getPaymentTemplate();
                                if (!assertEquals("7909689942", template.getSymbols().getVariableSymbol()))
                                    return;
                                if (!assertEquals("1210230319", template.getReceiver().getNumber()))
                                    return;
                                if (!assertEquals("0800", template.getReceiver().getBankCode()))
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
        return "JInsuranceEventsTaxBenefitsTest";
    }

}
