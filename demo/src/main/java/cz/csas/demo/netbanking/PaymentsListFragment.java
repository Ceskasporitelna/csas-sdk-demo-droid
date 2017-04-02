package cz.csas.demo.netbanking;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.thuongnh.zprogresshud.ZProgressHUD;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.Pagination;
import cz.csas.demo.R;
import cz.csas.demo.components.PaymentsAdapter;
import cz.csas.netbanking.Netbanking;
import cz.csas.netbanking.orders.Payment;
import cz.csas.netbanking.orders.PaymentsListResponse;
import cz.csas.netbanking.orders.PaymentsParameters;

/**
 * The type Payments list fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 16 /08/16.
 */
public class PaymentsListFragment extends Fragment {


    /**
     * The M swipe refresh layout.
     */
    @Bind(R.id.srl_payment_list)
    SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * The Lv payment.
     */
    @Bind(R.id.lv_payment)
    ListView lvPayment;

    private View mRootView;
    private FragmentCallback mFragmentCallback;
    private ZProgressHUD mProgress;
    private List<Payment> mPayments;
    private PaymentsAdapter mPaymentsAdapter;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentCallback = (FragmentCallback) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.netbanking_payment_list_fragment, container, false);
        ButterKnife.bind(this, mRootView);

        mPayments = new ArrayList<>();
        mPaymentsAdapter = new PaymentsAdapter(mPayments, getActivity());
        lvPayment.setAdapter(mPaymentsAdapter);

        mProgress = ZProgressHUD.getInstance(getActivity());
        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();
        getPayments();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPayments();
            }
        });

        lvPayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Payment payment = (Payment) mPaymentsAdapter.getItem(position);
                if (payment != null)
                    mFragmentCallback.changeFragmentToPaymentDetail(payment.getId());
            }
        });
        return mRootView;
    }


    private void getPayments() {
        PaymentsParameters parameters = new PaymentsParameters(new Pagination(0, 70), null);
        Netbanking.getInstance().getNetbankingClient().getOrdersResource().getPaymentsResource().list(parameters, new CallbackWebApi<PaymentsListResponse>() {
            @Override
            public void success(PaymentsListResponse paymentsListResponse) {
                if (paymentsListResponse.getPayments() != null) {
                    mPayments = paymentsListResponse.getPayments();
                    mPaymentsAdapter.changeData(mPayments);
                }
                if (mProgress != null && mProgress.isShowing())
                    mProgress.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(CsSDKError error) {
                if (mProgress != null && mProgress.isShowing())
                    mProgress.dismissWithFailure();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mProgress != null && mProgress.isShowing())
            mProgress.dismiss();
        mProgress = null;
    }
}
