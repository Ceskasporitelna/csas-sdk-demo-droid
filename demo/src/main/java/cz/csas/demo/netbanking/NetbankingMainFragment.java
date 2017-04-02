package cz.csas.demo.netbanking;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.demo.R;
import cz.csas.lockerui.utils.TypefaceUtils;

/**
 * The type Form list fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class NetbankingMainFragment extends Fragment {

    @Bind(R.id.btn_accounts)
    Button btnAccounts;

    @Bind(R.id.btn_payments)
    Button btnPayments;

    @Bind(R.id.btn_tests)
    Button btnTests;

    @Bind(R.id.tv_netbanking_title)
    TextView tvNetbankingTitle;

    private View mRootView;
    private FragmentCallback mFragmentCallback;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentCallback = (FragmentCallback) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.netbanking_main_fragment, container, false);
        ButterKnife.bind(this, mRootView);
        tvNetbankingTitle.setTypeface(TypefaceUtils.getRobotoMedium(getActivity()));

        btnAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentCallback.changeFragmentToAccountList();
            }
        });


        btnPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentCallback.changeFragmentToPaymentList();
            }
        });


        btnTests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentCallback.changeFragmentToTestList();
            }
        });
        return mRootView;
    }

}
