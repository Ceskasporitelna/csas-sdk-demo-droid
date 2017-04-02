package cz.csas.demo.uniforms;

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
import cz.csas.demo.R;
import cz.csas.demo.components.UniformsAdapter;
import cz.csas.uniforms.FormListResponse;
import cz.csas.uniforms.FormListing;
import cz.csas.uniforms.Uniforms;

/**
 * The type Form list fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class FormListFragment extends Fragment {

    @Bind(R.id.lv_form_listing)
    ListView lvForm;

    private View mRootView;
    private ZProgressHUD mProgress;
    private List<FormListing> mData;
    private UniformsAdapter mUniformsAdapter;
    private FragmentCallback mFragmentCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentCallback = (FragmentCallback) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.form_list_fragment, container, false);
        ButterKnife.bind(this, mRootView);

        mData = new ArrayList<>();
        mUniformsAdapter = new UniformsAdapter(mData,getActivity());
        lvForm.setAdapter(mUniformsAdapter);

        mProgress = ZProgressHUD.getInstance(getActivity());

        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();
        Uniforms.getInstance().getUniformsClient().getFormsResource().list(new CallbackWebApi<FormListResponse>() {
            @Override
            public void success(FormListResponse formListResponse) {
                mData = formListResponse.getForms();
                mUniformsAdapter.changeData(mData);
                mProgress.dismiss();
            }

            @Override
            public void failure(CsSDKError error) {
                mProgress.dismissWithFailure();
            }
        });

        lvForm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFragmentCallback.changeFragmentToDesc(mUniformsAdapter.getItemId(position));
            }
        });

        return mRootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mProgress != null && mProgress.isShowing())
            mProgress.dismiss();
        mProgress = null;
    }
}
