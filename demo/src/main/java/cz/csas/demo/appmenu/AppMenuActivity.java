package cz.csas.demo.appmenu;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.appmenu.AppInformation;
import cz.csas.appmenu.AppItem;
import cz.csas.appmenu.AppMenu;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.demo.App;
import cz.csas.demo.R;
import cz.csas.demo.components.AppMenuAdapter;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 24/05/16.
 */
public class AppMenuActivity extends AppCompatActivity {

    @Bind(R.id.lv_app_menu)
    ListView lvAppmenu;

    @Bind(R.id.sw_fake_version)
    Switch swFakeVersion;

    @Bind(R.id.activity_main_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private List<AppItem> mApplicationData;
    private AppMenuAdapter mAppMenuAdapter;
    private Activity mActivity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appmenu);
        ButterKnife.bind(this);

        mActivity = this;
        mApplicationData = new ArrayList<>();
        mAppMenuAdapter = new AppMenuAdapter(this, mApplicationData);
        lvAppmenu.setAdapter(mAppMenuAdapter);

        getAppInfo();

        swFakeVersion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                App.setFakeVersion(isChecked);
            }
        });

        swFakeVersion.setChecked(App.getFakeVersion());

        lvAppmenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((AppItem) mAppMenuAdapter.getItem(position)).open(mActivity);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAppInfo();
            }
        });
    }

    private void getAppInfo() {
        AppMenu.getInstance().getAppMenuManager().getAppInformation(0L, new CallbackWebApi<AppInformation>() {
            @Override
            public void success(AppInformation appInformation) {
                mApplicationData = new ArrayList<>();
                mApplicationData.add(appInformation.getThisApp());
                mApplicationData.addAll(appInformation.getOtherApps());
                mAppMenuAdapter.changeData(mApplicationData);
                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void failure(CsSDKError error) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}
