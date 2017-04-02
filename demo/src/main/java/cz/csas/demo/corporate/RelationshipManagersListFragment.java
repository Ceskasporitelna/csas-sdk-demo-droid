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
import cz.csas.corporate.companies.RelationshipManager;
import cz.csas.corporate.companies.RelationshipManagersFilter;
import cz.csas.corporate.companies.RelationshipManagersListResponse;
import cz.csas.corporate.companies.RelationshipManagersParameters;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.demo.Constants;
import cz.csas.demo.R;
import cz.csas.demo.components.CorporateRelationshipManagersAdapter;

/**
 * The type Companies list fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class RelationshipManagersListFragment extends Fragment {

    @Bind(R.id.srl_manager_list)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.lv_managers)
    ListView mLvRelationshipManagers;

    private View mRootView;
    private FragmentCallback mFragmentCallback;
    private ZProgressHUD mProgress;
    private List<RelationshipManager> mRelationshipManagers;
    private CorporateRelationshipManagersAdapter mRelationshipManagersAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentCallback = (FragmentCallback) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.corporate_relationship_managers_list_fragment, container, false);
        ButterKnife.bind(this, mRootView);

        mRelationshipManagers = new ArrayList<>();
        mRelationshipManagersAdapter = new CorporateRelationshipManagersAdapter(mRelationshipManagers, getActivity());
        mLvRelationshipManagers.setAdapter(mRelationshipManagersAdapter);

        mProgress = ZProgressHUD.getInstance(getActivity());
        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();

        final Bundle bundle = this.getArguments();
        if (bundle != null && bundle.get(Constants.COMPANY_ID_EXTRA) != null) {

            final String id = bundle.getString(Constants.COMPANY_ID_EXTRA);

            getRelationshipManagers(id);

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getRelationshipManagers(id);
                }
            });
        } else {
            mProgress.dismissWithFailure();
        }
        return mRootView;
    }

    private void getRelationshipManagers(String id) {

        RelationshipManagersParameters parameters = new RelationshipManagersParameters(RelationshipManagersFilter.ALL);
        Corporate.getInstance().getCorporateClient().getCompaniesResource().withId(id).getRelationshipManagersResource().list(parameters, new CallbackWebApi<RelationshipManagersListResponse>() {
            @Override
            public void success(RelationshipManagersListResponse relationshipManagersListResponse) {
                if (relationshipManagersListResponse.getRelationshipManagers() != null) {
                    mRelationshipManagers = relationshipManagersListResponse.getRelationshipManagers();
                    mRelationshipManagersAdapter.changeData(mRelationshipManagers);
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
