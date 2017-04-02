package cz.csas.demo.test.cases.netbanking.orders;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.Sort;
import cz.csas.cscore.webapi.SortDirection;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestStatus;
import cz.csas.netbanking.orders.PaymentsListResponse;
import cz.csas.netbanking.orders.PaymentsParameters;
import cz.csas.netbanking.orders.PaymentsSortableFields;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09/06/16.
 */
public class PaymentsListTest extends TestCase {

    @Override
    public void run(TestCallback callback) {
        mTestCallback = callback;

        PaymentsParameters parameters = new PaymentsParameters(null,
                Sort.by(PaymentsSortableFields.TRANSFER_DATE, SortDirection.ASCENDING));

        mNetbankingClient.getOrdersResource().getPaymentsResource().list(parameters, new CallbackWebApi<PaymentsListResponse>() {
            @Override
            public void success(PaymentsListResponse paymentsListResponse) {
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
        return "PaymentsListTest";
    }

}
