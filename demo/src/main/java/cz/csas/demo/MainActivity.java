package cz.csas.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.appmenu.AppIsOutdatedCallback;
import cz.csas.appmenu.AppItem;
import cz.csas.appmenu.AppMenu;
import cz.csas.cscore.CoreSDK;
import cz.csas.cscore.Environment;
import cz.csas.cscore.client.rest.CallbackUI;
import cz.csas.cscore.client.rest.CsCallback;
import cz.csas.cscore.client.rest.CsRestError;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.locker.CsNavBarColor;
import cz.csas.cscore.locker.LockerConfig;
import cz.csas.cscore.locker.LockerStatus;
import cz.csas.cscore.locker.OnLockerStatusChangeListener;
import cz.csas.cscore.locker.State;
import cz.csas.cscore.logger.LogLevel;
import cz.csas.cscore.logger.LogManagerImpl;
import cz.csas.demo.appmenu.AppMenuActivity;
import cz.csas.demo.corporate.CorporateActivity;
import cz.csas.demo.netbanking.NetbankingActivity;
import cz.csas.demo.places.PlacesActivity;
import cz.csas.demo.transparent_acc.TransparentAccActivity;
import cz.csas.demo.uniforms.UniformsActivity;
import cz.csas.lockerui.LockerUI;
import cz.csas.lockerui.components.MaterialDialog;
import cz.csas.lockerui.config.AuthFlowOptions;
import cz.csas.lockerui.config.DisplayInfoOptions;
import cz.csas.lockerui.config.FingerprintLock;
import cz.csas.lockerui.config.GestureLock;
import cz.csas.lockerui.config.LockType;
import cz.csas.lockerui.config.LockerUIOptions;
import cz.csas.lockerui.config.NoLock;
import cz.csas.lockerui.config.PinLock;
import cz.csas.lockerui.config.ShowLogo;
import cz.csas.lockerui.utils.TypefaceUtils;

/**
 * The type Main activity.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class MainActivity extends AppCompatActivity implements CallbackUI<LockerStatus> {

    private final String LOG_TAG = "CsasSDKDemo";

    // common credentials
    private final String BASE_URL = "https://www.csast.csas.cz/webapi";
    private final String REDIRECT_URL = "csastest://auth-completed";// your redirect url;
    private final String WEB_API_KEY = "fe334c4e-2787-4056-a258-35176f988b68";// your csas web api key;
    private final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjhgcI8zi/UyWfVu4DHudTwDF5ur3n1gxzi1kUXbScH0gv5LhqSgZrOY61foZeoFA+Bd6G3XWYrs3uchpygQluUIyPjMByzFbDhQMPe563PEs5tUMgNxnD6D/FTCbXjqRqd5L5S9S6ar37cVTXQaS+rhengWZiXSm3ZtGZ9iccyGbya8KxwcMdegV1P3xyw3px7kXB9sNaDoCGoEG/IqNI7P5eLiS7Jo3j3Nq/pUrQFvbAxD3N0otXYIH94BLAri18EG91qfsvyFpRgBMrIAj2J/MWS/dy/PpmRpSP7InuGmx68G4CEDYeg4CLZMUO1B4sKie5W7lFPlZWEnXYSnQYQIDAQAB";

    // netbanking credentials
    private final String BASE_URL_OAUTH = "https://bezpecnost.csast.csas.cz/mep/fs/fl/oauth2";
    private final String SCOPE = "/v3/netbanking"; // your scope
    private final String CLIENT_ID = "android_sdk_demo_webapi_csas_cz"; // your client id
    private final String CLIENT_SECRET = "7173IMPJ3F4QEU1YPTDAKMYLVFK92XBV"; // your client secret

    // corporate credentials
    private final String BASE_URL_OAUTH_CORPORATE = "https://www.csast.csas.cz/widp/oauth2";
    private final String SCOPE_CORPORATE = "/v1/corporate"; // your scope
    private final String CLIENT_ID_CORPORATE = "android_sdk_corporate_demo_webapi_csas_cz"; // your client id
    private final String CLIENT_SECRET_CORPORATE = "95kky75k98rgsmb3h7z4b1oacn"; // your client secret

    private State mLockerState;
    private AuthFlowOptions mAuthFlowOptions;
    private DisplayInfoOptions mDisplayInfoOptions;
    private MaterialDialog mVersionDialog;
    private Activity mActivity;
    private boolean mOfflineAuthEnabled = false;

    @Bind(R.id.tv_welcome)
    TextView tvWelcome;

    @Bind(R.id.tv_locker_status)
    TextView tvLockerStatus;

    @Bind(R.id.tv_offline_auth_status)
    TextView tvOfflineAuthStatus;

    @Bind(R.id.btn_locker_ui)
    Button btnLockerUI;

    @Bind(R.id.btn_lock)
    Button btnLock;

    @Bind(R.id.btn_lock_background)
    Button btnLockBackground;

    @Bind(R.id.btn_uniforms)
    Button btnUniforms;

    @Bind(R.id.btn_places)
    Button btnPlaces;

    @Bind(R.id.btn_app_menu)
    Button btnAppMenu;

    @Bind(R.id.btn_trans_acc)
    Button btnTransAcc;

    @Bind(R.id.btn_netbanking)
    Button btnNetbanking;

    @Bind(R.id.btn_corporate)
    Button btnCorporate;

    @Bind(R.id.sv_offline_auth)
    Switch svOfflineAuth;

    @Bind(R.id.sv_oauth_type)
    Switch svOAuthType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        App.getLocationManager().start();
        tvWelcome.setTypeface(TypefaceUtils.getRobotoMedium(this));
        tvLockerStatus.setTypeface(TypefaceUtils.getRobotoBlack(this));
        mActivity = this;
        mVersionDialog = new MaterialDialog(this);
        btnLockerUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runLockerUI();
            }
        });
        disableSSLCertificateChecking();

        /*
         * To change color and background uncomment this:
         * LockerUIOptions lockerUIOptions = new LockerUIOptions.Builder().setAllowedLockTypes(lockTypes).setAppName(APP_NAME).setCustomColor(Color.parseColor("#DC143C")).setBackgrounImage(ContextCompat.getDrawable(this,R.drawable.kytky)).create();
         *
         * To change your texts please customize texts in string resources
         */

        mAuthFlowOptions = new AuthFlowOptions.Builder().setRegistrationScreenText(getString(R.string.registration_screen_text)).setLockedScreenText(getString(R.string.locked_screen_text)).setOfflineAuthEnabled().create();

        /*
         * To skip status screen uncomment this:
         * mAuthFlowOptions = new AuthFlowOptions.Builder().setSkipStatusScreen(SkipStatusScreen.ALWAYS).setRegistrationScreenText(REGISTRATION_SCREEN_TEXT).setLockedScreenText(LOCKED_SCREEN_TEXT).create();
         */

        mDisplayInfoOptions = new DisplayInfoOptions(getString(R.string.unregister_prompt_text));

        initLocker();
        LockerStatus lockerStatus = LockerUI.getInstance().getLocker().getStatus();
        handleLockerStatusOperation(lockerStatus);

        CoreSDK.getInstance().getLocker().setOnLockerStatusChangeListener(new OnLockerStatusChangeListener() {
            @Override
            public void onLockerStatusChanged(final State state) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLockerState = state;
                        tvLockerStatus.setText(mLockerState.toString());
                        Log.d(LOG_TAG, "Your state is: " + mLockerState.name());
                        setLockButton();
                    }
                });
            }
        });

        /*
         * Register app menu sdk for application FRIENDS24 and start checking app version
         */
        AppMenu.getInstance().useAppMenu("friends24", "FRIENDS24", this);
        if (App.getFakeVersion())
            AppMenu.getInstance().getAppMenuManager().fakeMinimalVersionFromServer(2, 2);

        AppMenu.getInstance().getAppMenuManager().startCheckingAppVersion(new AppIsOutdatedCallback() {
            @Override
            public void success(final AppItem application) {
                if (mVersionDialog != null) {
                    mVersionDialog.setTitle(R.string.appmenu_version)
                            .setMessage(application.getAppName() + mActivity.getString(R.string.appmenu_outdated))
                            .setPositiveButton(R.string.appmenu_positive, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    application.openGooglePlayPage(mActivity);
                                }
                            })
                            .setNegativeButton(R.string.appmenu_negative, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mVersionDialog.dismiss();
                                }
                            })
                            .setCanceledOnTouchOutside(true)
                            .show();
                }
            }

            @Override
            public void failure(CsSDKError error) {

            }
        });

        btnUniforms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runUniforms();
            }
        });

        btnPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runPlaces();
            }
        });

        btnAppMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runAppmenu();
            }
        });

        btnTransAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runTransAcc();
            }
        });

        btnNetbanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runNetbanking();
            }
        });

        btnCorporate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runCorporate();
            }
        });

        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLockerState == State.USER_UNLOCKED) {
                    CoreSDK.getInstance().getLocker().lock(new CsCallback<LockerStatus>() {
                        @Override
                        public void success(LockerStatus lockerStatus, Response response) {
                            handleLockerStatusOperation(lockerStatus);
                        }

                        @Override
                        public void failure(CsSDKError error) {

                        }
                    });
                }
            }
        });

        svOfflineAuth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mOfflineAuthEnabled = isChecked;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.getLocationManager().stop();
        if (mVersionDialog != null)
            mVersionDialog.dismiss();
        mVersionDialog = null;
    }

    @Override
    public void success(LockerStatus lockerStatus) {
        handleLockerStatusOperation(lockerStatus);
        if (LockerUI.getInstance().getLocker().getAccessToken() != null) {
            String accessToken = LockerUI.getInstance().getLocker().getAccessToken().getAccessToken();
            Log.d(LOG_TAG, "Your access token is: " + accessToken);
        }
    }

    @Override
    public void failure(CsRestError error) {

    }

    private void handleLockerStatusOperation(LockerStatus lockerStatus) {
        mLockerState = lockerStatus.getState();
        tvLockerStatus.setText(mLockerState.toString());
        if (lockerStatus.isVerifiedOffline())
            tvOfflineAuthStatus.setText(getString(R.string.offline_auth_verified));
        else
            tvOfflineAuthStatus.setText(getString(R.string.offline_auth_not_verified));
        Log.d(LOG_TAG, "Your state is: " + lockerStatus.getState().name());
        setLockButton();
    }

    private void runLockerUI() {
        if (mLockerState == State.USER_UNREGISTERED || mLockerState == State.USER_LOCKED) {
            initLocker();
            // offline verification settings
            mAuthFlowOptions.setOfflineAuthEnabled(mOfflineAuthEnabled);
            LockerUI.getInstance().startAuthenticationFlow(mAuthFlowOptions, this);
        } else if (mLockerState == State.USER_UNLOCKED) {
            LockerUI.getInstance().displayInfo(mDisplayInfoOptions, this);
        }
    }

    private void runUniforms() {
        startActivity(new Intent(this, UniformsActivity.class));
    }

    private void runPlaces() {
        startActivity(new Intent(this, PlacesActivity.class));
    }

    private void runAppmenu() {
        startActivity(new Intent(this, AppMenuActivity.class));
    }

    private void runTransAcc() {
        startActivity(new Intent(this, TransparentAccActivity.class));
    }

    private void runNetbanking() {
        startActivity(new Intent(this, NetbankingActivity.class));
    }

    private void runCorporate() {
        startActivity(new Intent(this, CorporateActivity.class));
    }

    private void setLockButton() {
        if (mLockerState != State.USER_UNLOCKED) {
            btnLock.setVisibility(View.GONE);
            btnLockBackground.setVisibility(View.GONE);
        } else {
            btnLock.setVisibility(View.VISIBLE);
            btnLockBackground.setVisibility(View.VISIBLE);
        }
    }

    private void initLocker() {
        LockerConfig lockerConfig = null;
        Environment environment = null;
        if (!svOAuthType.isChecked()) {
            // netbanking config
            lockerConfig = new LockerConfig.Builder().setClientId(CLIENT_ID).setClientSecret(CLIENT_SECRET).setPublicKey(PUBLIC_KEY).setRedirectUrl(REDIRECT_URL).setScope(SCOPE).setOfflineAuthEnabled().create();
            environment = new Environment(BASE_URL, BASE_URL_OAUTH, true);
        } else {
            // corporate config
            lockerConfig = new LockerConfig.Builder().setClientId(CLIENT_ID_CORPORATE).setClientSecret(CLIENT_SECRET_CORPORATE).setPublicKey(PUBLIC_KEY).setRedirectUrl(REDIRECT_URL).setScope(SCOPE_CORPORATE).setOfflineAuthEnabled().create();
            environment = new Environment(BASE_URL, BASE_URL_OAUTH_CORPORATE, true);
        }
        CoreSDK.getInstance().useContext(App.get()).useWebApiKey(WEB_API_KEY).useLogger(new LogManagerImpl("CS_LOG", LogLevel.DEBUG)).useEnvironment(environment).useLocker(lockerConfig);

        /*
         * Here you set lockTypes you are gonna allow, with all its parameters.
         */
        List<LockType> lockTypes = new ArrayList<>();
        lockTypes.add(new GestureLock(4, 5));
        lockTypes.add(new PinLock(5));
        lockTypes.add(new FingerprintLock());
        lockTypes.add(new NoLock());
        LockerUIOptions lockerUIOptions = new LockerUIOptions.Builder().setNavBarColor(CsNavBarColor.DEFAULT).setShowLogo(ShowLogo.ALWAYS).setAllowedLockTypes(lockTypes).setAppName(getString(R.string.application_name)).create();
        /*
         * To change your texts please customize texts in string resources
         */

        LockerUI.getInstance().initialize(mActivity, lockerUIOptions);
    }

    private static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}

