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
import cz.csas.netbanking.contracts.insurances.InsuranceBeneficiariesListResponse;
import cz.csas.netbanking.contracts.insurances.InsuranceBeneficiary;
import cz.csas.netbanking.contracts.insurances.InsuranceBeneficiaryType;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JInsurancesWithIdBeneficiariesGetTest extends TestCase {

    private final String X_JUDGE_CASE = "contracts.insurances.withId.beneficiaries.get";

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
                mNetbankingJudgeClient.getContractsResource().getInsurancesResource().withId(id).getBeneficiariesResource().list(new CallbackWebApi<InsuranceBeneficiariesListResponse>() {
                    @Override
                    public void success(InsuranceBeneficiariesListResponse beneficiariesListResponse) {
                        List<String> flags = new ArrayList<String>();
                        flags.add("beneficiariesChangeAllowed");
                        if (!assertEquals(flags, beneficiariesListResponse.getFlags()))
                            return;
                        List<InsuranceBeneficiary> beneficiaries = beneficiariesListResponse.getBeneficiaries();
                        for (int i = 0; i < beneficiaries.size(); ++i) {
                            InsuranceBeneficiary beneficiary = beneficiaries.get(i);
                            switch (i) {
                                case 0:
                                    if (!assertEquals(InsuranceBeneficiaryType.PERSON, beneficiary.getType()))
                                        return;
                                    if (!assertEquals("Bielčik Tomáš", beneficiary.getName()))
                                        return;
                                    if (!assertEquals(TimeUtils.getPlainDate("2003-09-10"), beneficiary.getBirthdate()))
                                        return;
                                    if (!assertEquals(Double.valueOf(50), beneficiary.getPercentage()))
                                        return;
                                    break;
                                case 1:
                                    if (!assertEquals(InsuranceBeneficiaryType.PERSON, beneficiary.getType()))
                                        return;
                                    if (!assertEquals("Bielčiková Eliška", beneficiary.getName()))
                                        return;
                                    if (!assertEquals(TimeUtils.getPlainDate("2008-06-09"), beneficiary.getBirthdate()))
                                        return;
                                    if (!assertEquals(Double.valueOf(50), beneficiary.getPercentage()))
                                        return;
                                    break;
                            }
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
        return "JInsurancesWithIdBeneficiariesGetTest";
    }

}
