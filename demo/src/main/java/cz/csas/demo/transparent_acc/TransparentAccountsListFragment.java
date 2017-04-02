package cz.csas.demo.transparent_acc;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import cz.csas.cscore.webapi.PaginatedParameters;
import cz.csas.cscore.webapi.Pagination;
import cz.csas.demo.R;
import cz.csas.demo.components.TransparentAccountsAdapter;
import cz.csas.transparent_acc.TransparentAccount;
import cz.csas.transparent_acc.TransparentAccounts;
import cz.csas.transparent_acc.TransparentAccountsListResponse;

/**
 * The type Form list fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class TransparentAccountsListFragment extends Fragment {

    @Bind(R.id.lv_transparent_acc)
    ListView lvTransparentAcc;

    private View mRootView;
    private ZProgressHUD mProgress;
    private List<TransparentAccount> mData;
    private TransparentAccountsAdapter mTransparentAccountsAdapter;
    private cz.csas.demo.transparent_acc.FragmentCallback mFragmentCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentCallback = (cz.csas.demo.transparent_acc.FragmentCallback) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.transparent_acc_list_fragment, container, false);
        ButterKnife.bind(this, mRootView);

        mData = new ArrayList<>();
        mTransparentAccountsAdapter = new TransparentAccountsAdapter(mData, getActivity());
        lvTransparentAcc.setAdapter(mTransparentAccountsAdapter);

        mProgress = ZProgressHUD.getInstance(getActivity());

        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();
        TransparentAccounts.getInstance().getTransparentAccountsClient().getTransparentAccountsResource().list(new PaginatedParameters(new Pagination(0, 70)), new CallbackWebApi<TransparentAccountsListResponse>() {
            @Override
            public void success(TransparentAccountsListResponse transparentAccountsListResponse) {
                mData = transparentAccountsListResponse.getTransparentAccounts();
                mTransparentAccountsAdapter.changeData(mData);
                mProgress.dismiss();
            }

            @Override
            public void failure(CsSDKError error) {
                mProgress.dismissWithFailure();
            }
        });

        lvTransparentAcc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TransparentAccount transparentAccount = (TransparentAccount) mTransparentAccountsAdapter.getItem(position);
                if (transparentAccount != null)
                    mFragmentCallback.changeFragmentToAccount(transparentAccount.getId());
            }
        });

        return mRootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mProgress != null && mProgress.isShowing())
            mProgress.dismiss();
        mProgress = null;
    }
}
