package cz.csas.demo.test.cases.netbanking.orders;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.orders.Payment;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class PaymentsWithIdGetTest extends TestCase {

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;

        String orderId = "161124181563154";

        mNetbankingClient.getOrdersResource().getPaymentsResource().withId(orderId).get(new CallbackWebApi<Payment>() {
            @Override
            public void success(Payment payment) {
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
        return "PaymentsWithIdGetTest";
    }

}
