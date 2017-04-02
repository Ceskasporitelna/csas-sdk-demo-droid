package cz.csas.demo.netbanking;

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
import cz.csas.demo.R;
import cz.csas.demo.components.TestsAdapter;

import static cz.csas.demo.Constants.ACCOUNTS_TEST_LIST;
import static cz.csas.demo.Constants.CARDS_TEST_LIST;
import static cz.csas.demo.Constants.CONTACTS_TEST_LIST;
import static cz.csas.demo.Constants.CONTRACTS_TEST_LIST;
import static cz.csas.demo.Constants.MESSAGES_TEST_LIST;
import static cz.csas.demo.Constants.ORDERS_TEST_LIST;
import static cz.csas.demo.Constants.PROFILE_TEST_LIST;
import static cz.csas.demo.Constants.SECURITIES_TEST_LIST;
import static cz.csas.demo.Constants.SETTINGS_TEST_LIST;
import static cz.csas.demo.Constants.SIGNING_TEST_LIST;

/**
 * The type Form list fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class TestListFragment extends Fragment {

    @Bind(R.id.lv_netbanking_test)
    ListView lvNetbankingTest;

    private View mRootView;
    private List<String> mData;
    private TestsAdapter mTestsAdapter;
    private FragmentCallback mFragmentCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentCallback = (FragmentCallback) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.netbanking_test_list_fragment, container, false);
        ButterKnife.bind(this, mRootView);

        mData = new ArrayList<>();
        mData.add(ACCOUNTS_TEST_LIST);
        mData.add(CARDS_TEST_LIST);
        mData.add(CONTACTS_TEST_LIST);
        mData.add(CONTRACTS_TEST_LIST);
        mData.add(MESSAGES_TEST_LIST);
        mData.add(ORDERS_TEST_LIST);
        mData.add(PROFILE_TEST_LIST);
        mData.add(SECURITIES_TEST_LIST);
        mData.add(SETTINGS_TEST_LIST);
        mData.add(SIGNING_TEST_LIST);
        mTestsAdapter = new TestsAdapter(mData, getActivity());
        lvNetbankingTest.setAdapter(mTestsAdapter);

        lvNetbankingTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFragmentCallback.changeFragmentToSpecificTestList(mData.get(position));
            }
        });

        return mRootView;
    }
}
