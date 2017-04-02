package cz.csas.demo.corporate;

import android.app.Fragment;
import android.content.Context;
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
import cz.csas.corporate.Corporate;
import cz.csas.corporate.companies.CompaniesListResponse;
import cz.csas.corporate.companies.Company;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.demo.R;
import cz.csas.demo.components.CorporateCompaniesAdapter;

/**
 * The type Companies list fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class CompaniesListFragment extends Fragment {

    @Bind(R.id.srl_company_list)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.lv_companies)
    ListView mLvCompanies;

    private View mRootView;
    private FragmentCallback mFragmentCallback;
    private ZProgressHUD mProgress;
    private List<Company> mCompanies;
    private CorporateCompaniesAdapter mCompaniesAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentCallback = (FragmentCallback) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.corporate_company_list_fragment, container, false);
        ButterKnife.bind(this, mRootView);

        mCompanies = new ArrayList<>();
        mCompaniesAdapter = new CorporateCompaniesAdapter(mCompanies, getActivity());
        mLvCompanies.setAdapter(mCompaniesAdapter);

        mProgress = ZProgressHUD.getInstance(getActivity());
        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();
        getCompanies();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCompanies();
            }
        });

        mLvCompanies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Company company = (Company) mCompaniesAdapter.getItem(position);
                if (company != null)
                    mFragmentCallback.changeFragmentToCompanyDetail(company.getRegNum());
            }
        });
        return mRootView;
    }

    private void getCompanies() {
        Corporate.getInstance().getCorporateClient().getCompaniesResource().list(new CallbackWebApi<CompaniesListResponse>() {
            @Override
            public void success(CompaniesListResponse companiesListResponse) {
                if (companiesListResponse.getCompanies() != null) {
                    mCompanies = companiesListResponse.getCompanies();
                    mCompaniesAdapter.changeData(mCompanies);
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
