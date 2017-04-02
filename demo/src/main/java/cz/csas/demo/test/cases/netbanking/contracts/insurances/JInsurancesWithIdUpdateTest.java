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
import cz.csas.netbanking.contracts.insurances.InsuranceStatus;
import cz.csas.netbanking.contracts.insurances.InsuranceType;
import cz.csas.netbanking.contracts.insurances.InsuranceUpdateRequest;
import cz.csas.netbanking.contracts.insurances.InsuranceUpdateResponse;
import cz.csas.netbanking.contracts.insurances.Interval;
import cz.csas.netbanking.contracts.insurances.Life;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JInsurancesWithIdUpdateTest extends TestCase {

    private final String X_JUDGE_CASE = "contracts.insurances.withId.update";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                final String id = "3961D3F9E922EEE93E2581E896B34566645FE7E3";
                final String alias = "test alias";
                InsuranceUpdateRequest request = new InsuranceUpdateRequest(alias);
                mNetbankingJudgeClient.getContractsResource().getInsurancesResource().withId(id).update(request, new CallbackWebApi<InsuranceUpdateResponse>() {
                    @Override
                    public void success(InsuranceUpdateResponse insurance) {
                        List<String> flags = new ArrayList<String>();
                        flags.add("fundDistribution");
                        flags.add("taxDeductible");
                        flags.add("contractTermination");

                        if (!assertEquals(id, insurance.getId()))
                            return;

                        if (!assertEquals("3961D3F9E922EEE93E2581E896B34566645FE7E3", insurance.getId()))
                            return;
                        if (!assertEquals(InsuranceType.LIFE, insurance.getType()))
                            return;
                        if (!assertEquals("264", insurance.getProduct()))
                            return;
                        if (!assertEquals("Pojištění FLEXI", insurance.getProductI18N()))
                            return;
                        if (!assertEquals(alias, insurance.getAlias()))
                            return;
                        if (!assertEquals("Hana Bielčíková", insurance.getInsurancePolicyHolder()))
                            return;
                        if (!assertEquals("7009689942", insurance.getPolicyNumber()))
                            return;

                        Life life = insurance.getLife();
                        if (!assertEquals(Long.valueOf(60000), life.getPremium().getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), life.getPremium().getPrecision()))
                            return;
                        if (!assertEquals("CZK", life.getPremium().getCurrency()))
                            return;
                        if (!assertEquals(TimeUtils.getPlainDate("2046-12-31"), life.getContractEndDate()))
                            return;
                        if (!assertEquals(TimeUtils.getPlainDate("2015-01-01"), life.getContractStartDate()))
                            return;
                        if (!assertEquals(Long.valueOf(0), life.getInsuredAmount().getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), life.getInsuredAmount().getPrecision()))
                            return;
                        if (!assertEquals("CZK", life.getInsuredAmount().getCurrency()))
                            return;
                        if (!assertEquals(Long.valueOf(0), life.getCurrentCapitalValue().getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), life.getCurrentCapitalValue().getPrecision()))
                            return;
                        if (!assertEquals("CZK", life.getCurrentCapitalValue().getCurrency()))
                            return;
                        if (!assertEquals(Interval.MONTHLY, life.getPremiumPaymentInterval()))
                            return;
                        if (!assertEquals(TimeUtils.getPlainDate("2015-01-15"), life.getLastPremiumDate()))
                            return;
                        if (!assertEquals(Long.valueOf(60000), life.getLastPremiumPaid().getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), life.getLastPremiumPaid().getPrecision()))
                            return;
                        if (!assertEquals("CZK", life.getLastPremiumPaid().getCurrency()))
                            return;
                        if (!assertEquals(TimeUtils.getPlainDate("0999-12-31"), life.getContractTerminationDate()))
                            return;
                        if (!assertEquals(flags, life.getFlags()))
                            return;
                        if (!assertEquals(InsuranceStatus.ACTIVE, insurance.getStatus()))
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
        return "JInsurancesWithIdUpdateTest";
    }

}
