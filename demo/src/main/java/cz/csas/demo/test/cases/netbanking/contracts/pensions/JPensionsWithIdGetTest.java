package cz.csas.demo.test.cases.netbanking.contracts.pensions;

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
import cz.csas.netbanking.contracts.pensions.Entitlement;
import cz.csas.netbanking.contracts.pensions.Pension;
import cz.csas.netbanking.contracts.pensions.PensionStatus;
import cz.csas.netbanking.contracts.pensions.PensionSubtype;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JPensionsWithIdGetTest extends TestCase {

    private final String X_JUDGE_CASE = "contracts.pensions.withId.get";

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;
        ((JudgeRestService) mJudgeClient.getService()).nextCase(X_JUDGE_CASE, mXJudgeSessionHeader, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                final Map<String, String> headers = new HashMap<>();
                headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
                mNetbankingJudgeClient.setGlobalHeaders(headers);

                String id = "E7DD68AA3FF4487AF75626F901761B071E72FFFC";
                mNetbankingJudgeClient.getContractsResource().getPensionsResource().withId(id).get(new CallbackWebApi<Pension>() {
                    @Override
                    public void success(Pension pension) {
                        List<String> flags = new ArrayList<String>();
                        flags.add("accountQueryAllowed");

                        if (!assertEquals(TimeUtils.getPlainDate("2015-12-01"), pension.getValidFrom()))
                            return;
                        if (!assertEquals(Long.valueOf(0), pension.getPaidBenefits().getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), pension.getPaidBenefits().getPrecision()))
                            return;
                        if (!assertEquals("CZK", pension.getPaidBenefits().getCurrency()))
                            return;
                        if (!assertEquals("Hana Bielčíková", pension.getOwner()))
                            return;
                        if (!assertEquals("E7DD68AA3FF4487AF75626F901761B071E72FFFC", pension.getId()))
                            return;
                        if (!assertEquals("700117447", pension.getAgreementNumber()))
                            return;
                        if (!assertEquals(PensionStatus.ACTIVE, pension.getStatus()))
                            return;
                        if (!assertEquals(Long.valueOf(767292), pension.getProductAccount().getAmount().getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), pension.getProductAccount().getAmount().getPrecision()))
                            return;
                        if (!assertEquals("CZK", pension.getProductAccount().getAmount().getCurrency()))
                            return;

                        if (!assertEquals("Doplňkové penzijní spoření", pension.getProductI18N()))
                            return;
                        if (!assertEquals("55", pension.getProduct()))
                            return;
                        if (!assertEquals("8152152602", pension.getBirthNumber()))
                            return;
                        if (!assertEquals(Double.valueOf(100), pension.getStrategy().getConservative()))
                            return;
                        if (!assertEquals(Boolean.valueOf(true), pension.getPensionAgreed().getOldAge()))
                            return;
                        if (!assertEquals(Boolean.valueOf(true), pension.getPensionAgreed().getDisability()))
                            return;
                        if (!assertEquals(Boolean.valueOf(true), pension.getContribution().getEmployer()))
                            return;
                        if (!assertEquals(Long.valueOf(30000), pension.getContribution().getParticipantAmount().getValue()))
                            return;
                        if (!assertEquals(Integer.valueOf(2), pension.getContribution().getParticipantAmount().getPrecision()))
                            return;
                        if (!assertEquals("CZK", pension.getContribution().getParticipantAmount().getCurrency()))
                            return;

                        if (!assertEquals(Boolean.valueOf(false), pension.getSupplementary().getMaxService()))
                            return;
                        if (!assertEquals(Boolean.valueOf(false), pension.getSupplementary().getOptService()))
                            return;

                        if (!assertEquals(2, pension.getBeneficiaries().size()))
                            return;
                        if (!assertEquals("Eliška Bielčíková", pension.getBeneficiaries().get(0).getName()))
                            return;
                        if (!assertEquals(TimeUtils.getPlainDate("2008-06-09"), pension.getBeneficiaries().get(0).getBirthDate()))
                            return;
                        if (!assertEquals("0806099999", pension.getBeneficiaries().get(0).getBirthNumber()))
                            return;
                        if (!assertEquals("CZ", pension.getBeneficiaries().get(0).getAddress().getCountry()))
                            return;
                        if (!assertEquals(Double.valueOf(50), pension.getBeneficiaries().get(0).getShare()))
                            return;
                        if (!assertEquals(Entitlement.TAKE_OVER, pension.getBeneficiaries().get(0).getEntitlement()))
                            return;

                        if (!assertEquals("Tomáš Bielčík", pension.getBeneficiaries().get(1).getName()))
                            return;
                        if (!assertEquals(TimeUtils.getPlainDate("2003-09-10"), pension.getBeneficiaries().get(1).getBirthDate()))
                            return;
                        if (!assertEquals("0309109999", pension.getBeneficiaries().get(1).getBirthNumber()))
                            return;
                        if (!assertEquals("CZ", pension.getBeneficiaries().get(1).getAddress().getCountry()))
                            return;
                        if (!assertEquals(Double.valueOf(50), pension.getBeneficiaries().get(1).getShare()))
                            return;
                        if (!assertEquals(Entitlement.TAKE_OVER, pension.getBeneficiaries().get(1).getEntitlement()))
                            return;
                        if (!assertEquals(PensionSubtype.SUPPLEMENTARY_SAVINGS, pension.getSubtype()))
                            return;
                        if (!assertEquals("SUPPLEMENTARY_SAVINGS", pension.getSubtypeRaw()))
                            return;
                        if (!assertEquals(flags, pension.getFlags()))
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
        return "JPensionsWithIdGetTest";
    }

}
