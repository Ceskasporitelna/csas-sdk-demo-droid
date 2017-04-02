package cz.csas.demo.uniforms;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import cz.csas.demo.Constants;
import cz.csas.demo.R;

/**
 * The type Uniforms activity.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class UniformsActivity extends AppCompatActivity implements FragmentCallback {

    private final String FORM_LIST_FRAGMENT_TAG = "form_list_fragment";
    private final String FORM_DESC_FRAGMENT_TAG = "form_desc_fragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uniforms);
        ButterKnife.bind(this);
        changeFragmentToList();
    }

    @Override
    public void changeFragmentToList() {
        changeFragment(new FormListFragment(),FORM_LIST_FRAGMENT_TAG);
    }

    @Override
    public void changeFragmentToDesc(long id) {
        FormDescFragment formDescFragment = new FormDescFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.FORM_ID_EXTRA, id);
        formDescFragment.setArguments(bundle);
        changeFragment(formDescFragment,FORM_DESC_FRAGMENT_TAG);
    }

    private void changeFragment(Fragment fragment, String tag){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_uniforms_activity, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        FormDescFragment formDescFragment = (FormDescFragment) getFragmentManager().findFragmentByTag(FORM_DESC_FRAGMENT_TAG);
        if(formDescFragment != null && formDescFragment.isVisible()) {
            if(!formDescFragment.checkPobockyVisible())
                getFragmentManager().popBackStackImmediate();
        }else
            finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.CAMERA_REQUEST_CODE) {
            FormDescFragment formDescFragment = (FormDescFragment) getFragmentManager().findFragmentByTag(FORM_DESC_FRAGMENT_TAG);
            if (formDescFragment != null)
                formDescFragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
