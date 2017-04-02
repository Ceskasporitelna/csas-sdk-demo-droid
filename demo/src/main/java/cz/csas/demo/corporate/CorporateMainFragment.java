package cz.csas.demo.corporate;

import android.app.Fragment;
import android.content.Context;
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
 * The type Corporate main fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class CorporateMainFragment extends Fragment {

    @Bind(R.id.btn_accounts)
    Button mBtnAccounts;

    @Bind(R.id.btn_companies)
    Button mBtnCompanies;

    @Bind(R.id.tv_corporate_title)
    TextView mTvCorporateTitle;

    private View mRootView;
    private FragmentCallback mFragmentCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentCallback = (FragmentCallback) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.corporate_main_fragment, container, false);
        ButterKnife.bind(this, mRootView);
        mTvCorporateTitle.setTypeface(TypefaceUtils.getRobotoMedium(getActivity()));

        mBtnAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentCallback.changeFragmentToAccountList();
            }
        });

        mBtnCompanies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentCallback.changeFragmentToCompaniesList();
            }
        });

        return mRootView;
    }

}
