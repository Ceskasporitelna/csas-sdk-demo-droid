package cz.csas.demo.corporate;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.thuongnh.zprogresshud.ZProgressHUD;
import cz.csas.corporate.Corporate;
import cz.csas.corporate.companies.Campaign;
import cz.csas.corporate.companies.CampaignsListResponse;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.demo.Constants;
import cz.csas.demo.R;
import cz.csas.demo.components.CorporateCampaignsAdapter;

/**
 * The type Companies list fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class CampaignsListFragment extends Fragment {

    @Bind(R.id.srl_campaing_list)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.lv_campaigns)
    ListView mLvCampaigns;

    private View mRootView;
    private FragmentCallback mFragmentCallback;
    private ZProgressHUD mProgress;
    private List<Campaign> mCampaigns;
    private CorporateCampaignsAdapter mCampaignAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentCallback = (FragmentCallback) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.corporate_campaign_list_fragment, container, false);
        ButterKnife.bind(this, mRootView);

        mCampaigns = new ArrayList<>();
        mCampaignAdapter = new CorporateCampaignsAdapter(mCampaigns, getActivity());
        mLvCampaigns.setAdapter(mCampaignAdapter);

        mProgress = ZProgressHUD.getInstance(getActivity());
        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();

        final Bundle bundle = this.getArguments();
        if (bundle != null && bundle.get(Constants.COMPANY_ID_EXTRA) != null) {

            final String id = bundle.getString(Constants.COMPANY_ID_EXTRA);

            getCampaigns(id);

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getCampaigns(id);
                }
            });
        } else {
            mProgress.dismissWithFailure();
        }
        return mRootView;
    }

    private void getCampaigns(String id) {
        Corporate.getInstance().getCorporateClient().getCompaniesResource().withId(id).getCampaignsResource().list(new CallbackWebApi<CampaignsListResponse>() {
            @Override
            public void success(CampaignsListResponse campaignsListResponse) {
                if (campaignsListResponse.getCampaigns() != null) {
                    mCampaigns = campaignsListResponse.getCampaigns();
                    mCampaignAdapter.changeData(mCampaigns);
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
