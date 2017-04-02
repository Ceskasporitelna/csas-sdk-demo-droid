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
import cz.csas.cscore.utils.TimeUtils;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.netbanking.contracts.insurances.ContractEvent;
import cz.csas.netbanking.contracts.insurances.ContractEventState;
import cz.csas.netbanking.contracts.insurances.ContractEventsListResponse;
import cz.csas.netbanking.contracts.insurances.Indemnity;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class JInsuranceEventsListTest extends TestCase {

    private final String X_JUDGE_CASE = "contracts.insurances.withId.events.list";

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

                mNetbankingJudgeClient.getContractsResource().getInsurancesResource().withId(id).getEventsResource()
                        .list(new CallbackWebApi<ContractEventsListResponse>() {
                            @Override
                            public void success(ContractEventsListResponse eventsListResponse) {
                                List<ContractEvent> events = eventsListResponse.getEvents();
                                if (!assertEquals(1, events.size()))
                                    return;
                                ContractEvent event = events.get(0);

                                if (!assertEquals("13344534534", event.getNumber()))
                                    return;
                                if (!assertEquals(TimeUtils.getPlainDate("2015-02-28"), event.getCreationDate()))
                                    return;
                                if (!assertEquals(ContractEventState.CLOSED, event.getState()))
                                    return;
                                if (!assertEquals("Odesláno pojistné plnění", event.getSubstate()))
                                    return;
                                if (!assertEquals(TimeUtils.getPlainDate("2015-10-14"), event.getSubstateDate()))
                                    return;
                                if (!assertEquals("odesláno pojistné plnění", event.getSubstateInfo()))
                                    return;
                                if (!assertEquals(Long.valueOf(1000000), event.getAmount().getValue()))
                                    return;
                                if (!assertEquals(Integer.valueOf(2), event.getAmount().getPrecision()))
                                    return;
                                if (!assertEquals("CZK", event.getAmount().getCurrency()))
                                    return;
                                if (!assertEquals(TimeUtils.getPlainDate("2015-03-02"), event.getProcessingDate()))
                                    return;

                                List<Indemnity> indemnities = event.getIndemnities();
                                if (!assertEquals(1, indemnities.size()))
                                    return;
                                Indemnity indemnity = indemnities.get(0);

                                if (!assertEquals(TimeUtils.getPlainDate("2015-10-14"), indemnity.getPaymentDate()))
                                    return;
                                if (!assertEquals("Prevod na ucet", indemnity.getTransferMethod()))
                                    return;
                                if (!assertEquals("Tonda Palecek", indemnity.getReceiverName()))
                                    return;
                                if (!assertEquals(Long.valueOf(500000), indemnity.getAmount().getValue()))
                                    return;
                                if (!assertEquals(Integer.valueOf(2), indemnity.getAmount().getPrecision()))
                                    return;
                                if (!assertEquals("CZK", indemnity.getAmount().getCurrency()))
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
        return "JInsuranceEventsListTest";
    }

}
