package cz.csas.demo.test.cases.netbanking.orders;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.orders.DomesticPaymentCreateRequest;
import cz.csas.netbanking.orders.DomesticPaymentCreateResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class PaymentsDomesticCreateTest extends TestCase {

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;

        DomesticPaymentCreateRequest request = new DomesticPaymentCreateRequest.Builder()
                .setSenderName("Vrba")
                .setSender(new AccountNumber("2059930033", "0800"))
                .setReceiverName("Vojtíšková")
                .setReceiver(new AccountNumber("2328489013", "0800"))
                .setAmount(new Amount(110L, 2, "CZK"))
                .build();

        mNetbankingClient.getOrdersResource().getPaymentsResource().getDomesticResource().create(request, new CallbackWebApi<DomesticPaymentCreateResponse>() {
            @Override
            public void success(DomesticPaymentCreateResponse domesticPaymentCreateResponse) {
                mTestResult.setStatus(TestStatus.OK);
                mTestCallback.result(mTestResult);
            }

            @Override
            public void failure(CsSDKError error) {
                handleError(error);
            }
        });
    }

    @Override
    public String getName() {
        return "PaymentsDomesticCreateTest";
    }

}
