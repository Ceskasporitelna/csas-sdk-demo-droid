package cz.csas.demo.netbanking;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.thuongnh.zprogresshud.ZProgressHUD;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.signing.AuthorizationType;
import cz.csas.cscore.webapi.signing.FilledSigningObject;
import cz.csas.cscore.webapi.signing.MobileCaseSigningProcess;
import cz.csas.cscore.webapi.signing.NoAuthorizationSigningProcess;
import cz.csas.cscore.webapi.signing.SigningObject;
import cz.csas.cscore.webapi.signing.TacSigningProcess;
import cz.csas.demo.Constants;
import cz.csas.demo.R;
import cz.csas.demo.components.DetailHeader;
import cz.csas.demo.components.FieldTypeTextView;
import cz.csas.netbanking.AccountNumber;
import cz.csas.netbanking.Amount;
import cz.csas.netbanking.Netbanking;
import cz.csas.netbanking.accounts.MainAccount;
import cz.csas.netbanking.orders.DomesticPaymentCreateRequest;
import cz.csas.netbanking.orders.DomesticPaymentCreateResponse;

import static java.math.MathContext.DECIMAL64;


/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class DomesticPaymentFragment extends Fragment {

    @Bind(R.id.sv_fields)
    ScrollView svFields;

    @Bind(R.id.btn_start_signing)
    Button btnStartSigning;

    @Bind(R.id.ftw_sender_name)
    FieldTypeTextView ftwSenderName;

    @Bind(R.id.ftw_sender_number)
    FieldTypeTextView ftwSenderNumber;

    @Bind(R.id.ftw_sender_bank_code)
    FieldTypeTextView ftwSenderBankCode;

    @Bind(R.id.ftw_receiver_name)
    FieldTypeTextView ftwReceiverName;

    @Bind(R.id.ftw_receiver_number)
    FieldTypeTextView ftwReceiverNumber;

    @Bind(R.id.ftw_receiver_bank_code)
    FieldTypeTextView ftwReceiverBankCode;

    @Bind(R.id.ftw_amount)
    FieldTypeTextView ftwAmount;

    @Bind(R.id.sv_signing)
    ScrollView svSigning;

    @Bind(R.id.dh_step)
    DetailHeader dhStep;

    @Bind(R.id.tv_signing)
    TextView tvSigning;

    @Bind(R.id.et_pasword)
    EditText etPassword;

    @Bind(R.id.dh_select_authorization_type)
    DetailHeader dhSelectAuthorizationType;

    @Bind(R.id.sv_select_authorization_type)
    ScrollView svSelectAuthorizationType;

    @Bind(R.id.rl_btn_mobile_case)
    RelativeLayout rlBtnMobileCase;

    @Bind(R.id.btn_mobile_case)
    Button btnMobileCase;

    @Bind(R.id.rl_btn_tac)
    RelativeLayout rlBtnTac;

    @Bind(R.id.btn_tac)
    Button btnTac;

    @Bind(R.id.rl_btn_none)
    RelativeLayout rlBtnNone;

    @Bind(R.id.btn_none)
    Button btnNone;

    private final int NUM_OF_DIGITS_MOBILE_CASE = 6;
    private final int NUM_OF_DIGITS_TAC = 8;
    private final int NUM_OF_GROUP_DIGITS_MOBILE_CASE = 3;
    private final int NUM_OF_GROUP_DIGITS_TAC = 4;
    private View mRootView;
    private ZProgressHUD mProgress;
    private FragmentCallback mFragmentCallback;
    private MainAccount mSenderAccout;
    private FilledSigningObject mFilledSigningObject;
    private List<List<AuthorizationType>> mScenarios;
    private List<AuthorizationType> mScenario;
    private Map<AuthorizationType, Integer> mScenariosMap = new HashMap<>();
    private int mStepNumber = 1;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentCallback = (FragmentCallback) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.netbanking_domestic_payment_fragment, container, false);
        ButterKnife.bind(this, mRootView);

        mProgress = ZProgressHUD.getInstance(getActivity());
        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();
        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.get(Constants.ACCOUNT_ID_EXTRA) != null) {

            Netbanking.getInstance().getNetbankingClient().getAccountsResource().withId(bundle.getString(Constants.ACCOUNT_ID_EXTRA)).get(new CallbackWebApi<MainAccount>() {
                @Override
                public void success(MainAccount mainAccount) {
                    mSenderAccout = mainAccount;
                    setSenderLayout();
                    mProgress.dismiss();
                }

                @Override
                public void failure(CsSDKError error) {
                    mProgress.dismissWithFailure();
                    getActivity().onBackPressed();
                }
            });
        } else {
            mProgress.dismissWithFailure();
            mFragmentCallback.changeFragmentToAccountList();
        }

        btnStartSigning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setMessage(getString(R.string.netbanking_progress_signing));
                mProgress.show();
                if (btnStartSigning.getText().toString().equals(getString(R.string.netbanking_domestic_payment_start_signing))) {
                    if (checkPaymentFields()) {
                        BigDecimal value = new BigDecimal(ftwAmount.getText().get(0), MathContext.DECIMAL64);
                        Amount amount = new Amount(value.unscaledValue().longValue(), value.scale(), "CZK");
                        AccountNumber sender = new AccountNumber(ftwSenderNumber.getText().get(0), ftwSenderBankCode.getText().get(0));
                        AccountNumber receiver = new AccountNumber(ftwReceiverNumber.getText().get(0), ftwReceiverBankCode.getText().get(0));
                        DomesticPaymentCreateRequest request = new DomesticPaymentCreateRequest.Builder()
                                .setAmount(amount)
                                .setReceiver(receiver)
                                .setReceiverName(ftwReceiverName.getText().get(0))
                                .setSender(sender)
                                .setSenderName(ftwSenderName.getText().get(0))
                                .build();

                        Netbanking.getInstance().getNetbankingClient().getOrdersResource().getPaymentsResource().getDomesticResource().create(request, new CallbackWebApi<DomesticPaymentCreateResponse>() {
                            @Override
                            public void success(DomesticPaymentCreateResponse domesticPaymentCreateResponse) {
                                SigningObject signingObject = domesticPaymentCreateResponse.signing();
                                signingObject.getInfo(new CallbackWebApi<FilledSigningObject>() {
                                    @Override
                                    public void success(FilledSigningObject filledSigningObject) {
                                        mScenarios = filledSigningObject.getScenarios();
                                        mFilledSigningObject = filledSigningObject;
                                        setSelectScenarioLayout();
                                        dismissProgress(true);
                                    }

                                    @Override
                                    public void failure(CsSDKError error) {
                                        dismissProgress(false);
                                    }
                                });
                            }

                            @Override
                            public void failure(CsSDKError error) {
                                dismissProgress(false);
                            }
                        });

                    } else
                        mProgress.dismissWithFailure();
                } else {
                    finishSigning();
                }
            }
        });

        btnMobileCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScenario = mScenarios.get(mScenariosMap.get(AuthorizationType.MOBILE_CASE));
                setSigningLayout();
            }
        });

        btnTac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScenario = mScenarios.get(mScenariosMap.get(AuthorizationType.TAC));
                setSigningLayout();
            }
        });

        btnNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScenario = mScenarios.get(mScenariosMap.get(AuthorizationType.NONE));
                setSigningLayout();
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

    private void setSenderLayout() {
        AccountNumber accountNumber = mSenderAccout.getAccountno();
        if (accountNumber != null) {
            if (accountNumber.getNumber() != null) {
                ftwSenderNumber.setText(accountNumber.getNumber());
                ftwSenderNumber.setEnabled(false);
            } else
                ftwSenderNumber.setEnabled(true);
            if (accountNumber.getBankCode() != null) {
                ftwSenderBankCode.setText(accountNumber.getBankCode());
                ftwSenderBankCode.setEnabled(false);
            } else
                ftwSenderBankCode.setEnabled(true);
        }
        ftwSenderName.setText("Ales Vrba");
        ftwReceiverName.setText("Anna Vojtiskova");
        ftwReceiverNumber.setText("2328489013");
        ftwReceiverBankCode.setText("0800");
        ftwReceiverName.setText("Anna Vojtiskova");
    }

    private void setSelectScenarioLayout() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mScenarios.size() > 1) {
                    dhSelectAuthorizationType.setDetailHeader(String.format("Step %s. Select Authorization Type:", mStepNumber));
                    for (int i = 0; i < mScenarios.size(); i++) {
                        AuthorizationType type = mScenarios.get(i).get(0);
                        if (type == AuthorizationType.MOBILE_CASE) {
                            rlBtnMobileCase.setVisibility(View.VISIBLE);
                            mScenariosMap.put(AuthorizationType.MOBILE_CASE, i);
                        } else if (type == AuthorizationType.TAC) {
                            rlBtnTac.setVisibility(View.VISIBLE);
                            mScenariosMap.put(AuthorizationType.TAC, i);
                        } else {
                            rlBtnNone.setVisibility(View.VISIBLE);
                            mScenariosMap.put(AuthorizationType.NONE, i);
                        }
                    }
                    svFields.setVisibility(View.GONE);
                    svSelectAuthorizationType.setVisibility(View.VISIBLE);
                } else {
                    mScenario = mScenarios.get(0);
                    setSigningLayout();
                }
            }
        });
    }

    private void setSigningLayout() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AuthorizationType type = mScenario.get(mStepNumber - 1);
                dhStep.setDetailHeader(String.format("Step %s, Signing Type: %s", mStepNumber, type));
                btnStartSigning.setText(getString(R.string.netbanking_domestic_payment_finish_signing));
                svSelectAuthorizationType.setVisibility(View.GONE);
                svFields.setVisibility(View.GONE);
                svSigning.setVisibility(View.VISIBLE);
                if (type == AuthorizationType.NONE)
                    etPassword.setEnabled(false);
                else if (type == AuthorizationType.MOBILE_CASE) {
                    etPassword.setEnabled(true);
                    etPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(NUM_OF_DIGITS_MOBILE_CASE)});
                    //etPassword.addTextChangedListener(new VariableDigitsFormatTextWatcher(NUM_OF_GROUP_DIGITS_MOBILE_CASE));
                } else {
                    etPassword.setEnabled(true);
                    etPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(NUM_OF_DIGITS_TAC)});
                    //etPassword.addTextChangedListener(new VariableDigitsFormatTextWatcher(NUM_OF_GROUP_DIGITS_TAC));
                }
            }
        });
    }

    private void finishSigning() {
        switch (mScenario.get(mStepNumber - 1)) {
            case NONE:
                mFilledSigningObject.startSigningWithNoAuthorization(new CallbackWebApi<NoAuthorizationSigningProcess>() {
                    @Override
                    public void success(NoAuthorizationSigningProcess noAuthorizationSigningProcess) {
                        noAuthorizationSigningProcess.finishSigning(new CallbackWebApi<SigningObject>() {
                            @Override
                            public void success(SigningObject signingObject) {
                                dismissProgress(true);
                                checkSigning();
                            }

                            @Override
                            public void failure(CsSDKError error) {
                                dismissProgress(false);
                            }
                        });
                    }

                    @Override
                    public void failure(CsSDKError error) {
                        dismissProgress(false);
                    }
                });
                break;
            case MOBILE_CASE:
                if (!etPassword.getText().toString().matches("")) {
                    mFilledSigningObject.startSigningWithMobileCase(new CallbackWebApi<MobileCaseSigningProcess>() {
                        @Override
                        public void success(MobileCaseSigningProcess mobileCaseSigningProcess) {
                            mobileCaseSigningProcess.finishSigning(etPassword.getText().toString().trim(), new CallbackWebApi<SigningObject>() {
                                @Override
                                public void success(SigningObject signingObject) {
                                    dismissProgress(true);
                                    checkSigning();
                                }

                                @Override
                                public void failure(CsSDKError error) {
                                    dismissProgress(false);
                                }
                            });
                        }

                        @Override
                        public void failure(CsSDKError error) {
                            dismissProgress(false);
                        }
                    });
                } else
                    dismissProgress(false);
                break;
            case TAC:
                if (!etPassword.getText().toString().matches("")) {

                    mFilledSigningObject.startSigningWithTac(new CallbackWebApi<TacSigningProcess>() {
                        @Override
                        public void success(TacSigningProcess tacSigningProcess) {
                            tacSigningProcess.finishSigning(etPassword.getText().toString().trim(), new CallbackWebApi<SigningObject>() {
                                @Override
                                public void success(SigningObject signingObject) {
                                    dismissProgress(true);
                                    checkSigning();
                                }

                                @Override
                                public void failure(CsSDKError error) {
                                    dismissProgress(false);
                                }
                            });
                        }

                        @Override
                        public void failure(CsSDKError error) {
                            dismissProgress(false);
                        }
                    });
                } else
                    dismissProgress(false);
                break;
        }
    }

    private void checkSigning() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mFilledSigningObject.isDone()) {
                    getActivity().onBackPressed();
                } else {
                    mStepNumber++;
                    setSigningLayout();
                }
            }
        });
    }

    private boolean checkPaymentFields() {
        List<String> error = null;
        boolean hasErrors = false;
        try {
            BigDecimal v = new BigDecimal(ftwAmount.getText().get(0), DECIMAL64);
            ftwAmount.setError(error);
        } catch (NumberFormatException e) {
            ftwAmount.setError(e.toString());
            hasErrors = true;
        }
        if (ftwReceiverName.getText().get(0) == null) {
            ftwReceiverName.setError("Value cannot be null");
            hasErrors = true;
        } else
            ftwReceiverName.setError(error);

        if (ftwReceiverNumber.getText().get(0) == null) {
            ftwReceiverNumber.setError("Value cannot be null");
            hasErrors = true;
        } else
            ftwReceiverNumber.setError(error);

        if (ftwReceiverBankCode.getText().get(0) == null) {
            ftwReceiverBankCode.setError("Value cannot be null");
            hasErrors = true;
        } else
            ftwReceiverBankCode.setError(error);

        if (ftwSenderName.getText().get(0) == null) {
            ftwSenderName.setError("Value cannot be null");
            hasErrors = true;
        } else
            ftwSenderName.setError(error);

        if (ftwSenderNumber.getText().get(0) == null) {
            ftwSenderNumber.setError("Value cannot be null");
            hasErrors = true;
        } else
            ftwSenderNumber.setError(error);
        if (ftwSenderBankCode.getText().get(0) == null) {
            ftwSenderBankCode.setError("Value cannot be null");
            hasErrors = true;
        } else
            ftwSenderBankCode.setError(error);
        return !hasErrors;
    }

    private void dismissProgress(final boolean success) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success)
                    mProgress.dismissWithSuccess();
                else
                    mProgress.dismissWithFailure();
            }
        });
    }
}
