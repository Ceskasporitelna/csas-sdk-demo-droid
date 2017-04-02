package cz.csas.demo.test.cases.netbanking.contracts.insurances;

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
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.contacts.ContactAddress;
import cz.csas.netbanking.contacts.ContactAddressType;
import cz.csas.netbanking.contracts.insurances.Insuree;
import cz.csas.netbanking.contracts.insurances.InsureeType;
import cz.csas.netbanking.contracts.insurances.InsureesListResponse;
import cz.csas.netbanking.contracts.insurances.Risk;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JInsurancesInsureesListTest extends TestCase {

    private final String X_JUDGE_CASE = "contracts.insurances.withId.insurees.list";

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
                mNetbankingJudgeClient.getContractsResource().getInsurancesResource().withId(id).getInsureesResource().list(new CallbackWebApi<InsureesListResponse>() {
                    @Override
                    public void success(InsureesListResponse insureesListResponse) {
                        List<Insuree> insurees = insureesListResponse.getInsurees();
                        if (!assertEquals(1, insurees.size()))
                            return;

                        Insuree insuree = insurees.get(0);
                        if (!assertEquals("78afefe2d55e124cbd4a1bbfa1a1bbb0b1ec5bc8b434a2a17703ea6c6d597092", insuree.getId()))
                            return;
                        if (!assertEquals(InsureeType.POLICY_HOLDER, insuree.getType()))
                            return;
                        if (!assertEquals("Hana Bielčíková", insuree.getName()))
                            return;

                        List<ContactAddress> addresses = insuree.getAddresses();
                        if (!assertEquals(1, addresses.size()))
                            return;
                        ContactAddress address = addresses.get(0);
                        if (!assertEquals(ContactAddressType.PERMANENT_RESIDENCE, address.getType()))
                            return;
                        if (!assertEquals("CZ", address.getCountry()))
                            return;
                        if (!assertEquals("STARÉ SMRKOVICE", address.getCity()))
                            return;
                        if (!assertEquals("Staré Smrkovice 6", address.getStreet()))
                            return;
                        if (!assertEquals("508 01", address.getZipCode()))
                            return;
                        if (!assertEquals("8152152602", insuree.getBirthNumber()))
                            return;

                        List<Risk> risks = insuree.getRisks();
                        if (!assertEquals(2, risks.size()))
                            return;
                        for (int i = 0; i < risks.size(); ++i) {
                            Risk risk = risks.get(i);
                            switch (i) {
                                case 0:
                                    if (!assertEquals("Pojištění smrti následkem úrazu", risk.getProductName()))
                                        return;
                                    if (!assertEquals("Úmrtí", risk.getRiskGroup()))
                                        return;
                                    if (!assertEquals(Long.valueOf(15000000), risk.getInsuredSum().getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), risk.getInsuredSum().getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", risk.getInsuredSum().getCurrency()))
                                        return;
                                    break;
                                case 1:
                                    if (!assertEquals("Základní pojištění smrti z jakýchkoliv příčin", risk.getProductName()))
                                        return;
                                    if (!assertEquals("Úmrtí", risk.getRiskGroup()))
                                        return;
                                    if (!assertEquals(Long.valueOf(1000000), risk.getInsuredSum().getValue()))
                                        return;
                                    if (!assertEquals(Integer.valueOf(2), risk.getInsuredSum().getPrecision()))
                                        return;
                                    if (!assertEquals("CZK", risk.getInsuredSum().getCurrency()))
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
        return "JInsurancesInsureesListTest";
    }

}
