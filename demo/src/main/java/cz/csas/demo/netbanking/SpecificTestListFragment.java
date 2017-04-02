package cz.csas.demo.netbanking;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.thuongnh.zprogresshud.ZProgressHUD;
import cz.csas.demo.R;
import cz.csas.demo.components.TestCaseAdapter;
import cz.csas.demo.test.cases.netbanking.accounts.AccountWithIdStatementsListPage0Test;
import cz.csas.demo.test.cases.netbanking.accounts.AccountWithIdStatementsListPage1Test;
import cz.csas.demo.test.cases.netbanking.accounts.AccountWithIdStatementsListTest;
import cz.csas.demo.test.cases.netbanking.accounts.AccountWithIdSubAccountStatementsListPage0Test;
import cz.csas.demo.test.cases.netbanking.accounts.AccountWithIdSubAccountStatementsListPage1Test;
import cz.csas.demo.test.cases.netbanking.accounts.AccountWithIdSubAccountStatementsListTest;
import cz.csas.demo.test.cases.netbanking.accounts.AccountWithIdTest;
import cz.csas.demo.test.cases.netbanking.accounts.AccountsListPage0Test;
import cz.csas.demo.test.cases.netbanking.accounts.AccountsListPage1Test;
import cz.csas.demo.test.cases.netbanking.accounts.AccountsListTest;
import cz.csas.demo.test.cases.netbanking.accounts.DirectDebitsListPage0Test;
import cz.csas.demo.test.cases.netbanking.accounts.DirectDebitsListPage1Test;
import cz.csas.demo.test.cases.netbanking.accounts.DirectDebitsListTest;
import cz.csas.demo.test.cases.netbanking.accounts.DirectDebitsWithIdGetTest;
import cz.csas.demo.test.cases.netbanking.accounts.StandingOrdersListPage0Test;
import cz.csas.demo.test.cases.netbanking.accounts.StandingOrdersListPage1Test;
import cz.csas.demo.test.cases.netbanking.accounts.StandingOrdersListTest;
import cz.csas.demo.test.cases.netbanking.accounts.StandingOrdersWithIdGetTest;
import cz.csas.demo.test.cases.netbanking.cards.CardAccountsStatementsListPage0Test;
import cz.csas.demo.test.cases.netbanking.cards.CardAccountsStatementsListPage1Test;
import cz.csas.demo.test.cases.netbanking.cards.CardAccountsStatementsListTest;
import cz.csas.demo.test.cases.netbanking.cards.CardsListPage0Test;
import cz.csas.demo.test.cases.netbanking.cards.CardsListPage1Test;
import cz.csas.demo.test.cases.netbanking.cards.CardsListTest;
import cz.csas.demo.test.cases.netbanking.contacts.ContactWithIdGetTest;
import cz.csas.demo.test.cases.netbanking.contacts.ContactsListTest;
import cz.csas.demo.test.cases.netbanking.contracts.buildings.BuildingsListPage0Test;
import cz.csas.demo.test.cases.netbanking.contracts.buildings.BuildingsListPage1Test;
import cz.csas.demo.test.cases.netbanking.contracts.buildings.BuildingsListTest;
import cz.csas.demo.test.cases.netbanking.contracts.insurances.InsurancesListPage0Test;
import cz.csas.demo.test.cases.netbanking.contracts.insurances.InsurancesListPage1Test;
import cz.csas.demo.test.cases.netbanking.contracts.insurances.InsurancesListTest;
import cz.csas.demo.test.cases.netbanking.contracts.pensions.PensionsListPage0Test;
import cz.csas.demo.test.cases.netbanking.contracts.pensions.PensionsListPage1Test;
import cz.csas.demo.test.cases.netbanking.contracts.pensions.PensionsListTest;
import cz.csas.demo.test.cases.netbanking.messages.MessagesListPage0Test;
import cz.csas.demo.test.cases.netbanking.messages.MessagesListPage1Test;
import cz.csas.demo.test.cases.netbanking.messages.MessagesListTest;
import cz.csas.demo.test.cases.netbanking.messages.MessagesWithIdGetTest;
import cz.csas.demo.test.cases.netbanking.orders.PaymentsDomesticCreateTest;
import cz.csas.demo.test.cases.netbanking.orders.PaymentsListPage0Test;
import cz.csas.demo.test.cases.netbanking.orders.PaymentsListPage1Test;
import cz.csas.demo.test.cases.netbanking.orders.PaymentsListTest;
import cz.csas.demo.test.cases.netbanking.profile.ProfileGetTest;
import cz.csas.demo.test.cases.netbanking.profile.ProfileLastLoginTest;
import cz.csas.demo.test.cases.netbanking.securities.SecuritiesListPage0Test;
import cz.csas.demo.test.cases.netbanking.securities.SecuritiesListPage1Test;
import cz.csas.demo.test.cases.netbanking.securities.SecuritiesListTest;
import cz.csas.demo.test.cases.netbanking.securities.SecuritiesWithIdGetTest;
import cz.csas.demo.test.cases.netbanking.settings.SettingsGetTest;
import cz.csas.demo.test.cases.netbanking.signing.JCaseMobileCardLimitsUpdateTest;
import cz.csas.demo.test.cases.netbanking.signing.JCaseMobilePaymentsDomesticCreateTest;
import cz.csas.demo.test.cases.netbanking.signing.JTacAccountsTransferUpdateTest;
import cz.csas.demo.test.cases.netbanking.signing.JTacCardsActionsUpdateTest;
import cz.csas.demo.test.cases.netbanking.signing.JTacCardsLimitsUpdateInvalidTest;
import cz.csas.demo.test.cases.netbanking.signing.JTacCardsLimitsUpdateNotFoundTest;
import cz.csas.demo.test.cases.netbanking.signing.JTacCardsLimitsUpdateOTPTest;
import cz.csas.demo.test.cases.netbanking.signing.JTacCardsLimitsUpdateTest;
import cz.csas.demo.test.cases.netbanking.signing.JTacCardsTransferUpdateTest;
import cz.csas.demo.test.cases.netbanking.signing.JTacPaymentsDomesticCreateOTPTest;
import cz.csas.demo.test.cases.netbanking.signing.JTacPaymentsDomesticCreateTest;
import cz.csas.demo.test.cases.netbanking.signing.JTacPaymentsDomesticInvalidAuthTypeTest;
import cz.csas.demo.test.cases.netbanking.signing.JTacPaymentsDomesticUpdateNotFoundTest;
import cz.csas.demo.test.cases.netbanking.signing.JTacPaymentsDomesticUpdateTest;
import cz.csas.demo.test.cases.netbanking.signing.JTacPaymentsMobileCreateTest;
import cz.csas.demo.test.core.TestCallback;
import cz.csas.demo.test.core.TestCase;
import cz.csas.demo.test.core.TestResult;
import cz.csas.demo.test.core.TestStatus;

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
import static cz.csas.demo.Constants.TYPE_EXTRA;

/**
 * The type Accounts test list fragment.
 */
public class SpecificTestListFragment extends Fragment {

    /**
     * The Lv netbanking test.
     */
    @Bind(R.id.lv_netbanking_test)
    ListView lvNetbankingTest;

    /**
     * The Btn run all.
     */
    @Bind(R.id.btn_netbanking_run_all)
    Button btnRunAll;

    private View mRootView;
    private ZProgressHUD mProgress;
    private int mCounter;
    private List<TestCase> mData;
    private TestCaseAdapter mTestCaseAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.netbanking_test_case_list_fragment, container, false);
        ButterKnife.bind(this, mRootView);
        mProgress = ZProgressHUD.getInstance(getActivity());
        mProgress.setMessage(getString(R.string.progress_loading));
        mData = new ArrayList<>();

        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.get(TYPE_EXTRA) != null) {
            String type = bundle.getString(TYPE_EXTRA);
            switch (type) {
                case ACCOUNTS_TEST_LIST:
                    mData.add(new AccountsListPage0Test());
                    mData.add(new AccountsListPage1Test());
                    mData.add(new AccountsListTest());
                    mData.add(new AccountWithIdStatementsListPage0Test());
                    mData.add(new AccountWithIdStatementsListPage1Test());
                    mData.add(new AccountWithIdStatementsListTest());
                    mData.add(new AccountWithIdSubAccountStatementsListPage0Test());
                    mData.add(new AccountWithIdSubAccountStatementsListPage1Test());
                    mData.add(new AccountWithIdSubAccountStatementsListTest());
                    mData.add(new AccountWithIdTest());
                    mData.add(new DirectDebitsListPage0Test());
                    mData.add(new DirectDebitsListPage1Test());
                    mData.add(new DirectDebitsListTest());
                    mData.add(new DirectDebitsWithIdGetTest());
                    mData.add(new StandingOrdersListPage0Test());
                    mData.add(new StandingOrdersListPage1Test());
                    mData.add(new StandingOrdersListTest());
                    mData.add(new StandingOrdersWithIdGetTest());
                    break;
                case CARDS_TEST_LIST:
                    mData.add(new CardAccountsStatementsListPage0Test());
                    mData.add(new CardAccountsStatementsListPage1Test());
                    mData.add(new CardAccountsStatementsListTest());
                    mData.add(new CardsListPage0Test());
                    mData.add(new CardsListPage1Test());
                    mData.add(new CardsListTest());
                    break;
                case CONTACTS_TEST_LIST:
                    mData.add(new ContactWithIdGetTest());
                    mData.add(new ContactsListTest());
                    break;
                case CONTRACTS_TEST_LIST:
                    mData.add(new BuildingsListPage0Test());
                    mData.add(new BuildingsListPage1Test());
                    mData.add(new BuildingsListTest());
                    mData.add(new InsurancesListPage0Test());
                    mData.add(new InsurancesListPage1Test());
                    mData.add(new InsurancesListTest());
                    mData.add(new PensionsListPage0Test());
                    mData.add(new PensionsListPage1Test());
                    mData.add(new PensionsListTest());
                    break;
                case MESSAGES_TEST_LIST:
                    mData.add(new MessagesListPage0Test());
                    mData.add(new MessagesListPage1Test());
                    mData.add(new MessagesListTest());
                    mData.add(new MessagesWithIdGetTest());
                    break;
                case ORDERS_TEST_LIST:
                    mData.add(new PaymentsDomesticCreateTest());
                    mData.add(new PaymentsListPage0Test());
                    mData.add(new PaymentsListPage1Test());
                    mData.add(new PaymentsListTest());
                    break;
                case PROFILE_TEST_LIST:
                    mData.add(new ProfileGetTest());
                    mData.add(new ProfileLastLoginTest());
                    break;
                case SECURITIES_TEST_LIST:
                    mData.add(new SecuritiesListTest());
                    mData.add(new SecuritiesListPage0Test());
                    mData.add(new SecuritiesListPage1Test());
                    mData.add(new SecuritiesWithIdGetTest());
                    break;
                case SETTINGS_TEST_LIST:
                    mData.add(new SettingsGetTest());
                    break;
                case SIGNING_TEST_LIST:
                    mData.add(new JCaseMobileCardLimitsUpdateTest());
                    mData.add(new JCaseMobilePaymentsDomesticCreateTest());
                    mData.add(new JTacAccountsTransferUpdateTest());
                    mData.add(new JTacCardsActionsUpdateTest());
                    mData.add(new JTacCardsLimitsUpdateInvalidTest());
                    mData.add(new JTacCardsLimitsUpdateNotFoundTest());
                    mData.add(new JTacCardsLimitsUpdateOTPTest());
                    mData.add(new JTacCardsLimitsUpdateTest());
                    mData.add(new JTacCardsTransferUpdateTest());
                    mData.add(new JTacPaymentsDomesticCreateOTPTest());
                    mData.add(new JTacPaymentsDomesticCreateTest());
                    mData.add(new JTacPaymentsDomesticInvalidAuthTypeTest());
                    mData.add(new JTacPaymentsDomesticUpdateNotFoundTest());
                    mData.add(new JTacPaymentsDomesticUpdateTest());
                    mData.add(new JTacPaymentsMobileCreateTest());
                    break;
            }
        }
        mTestCaseAdapter = new TestCaseAdapter(getActivity(), mData);
        lvNetbankingTest.setAdapter(mTestCaseAdapter);

        lvNetbankingTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mProgress.show();
                mData.get(position).getTestResult().setStatus(TestStatus.UNKNOWN);
                mTestCaseAdapter.changeData(mData);
                mData.get(position).run(new TestCallback() {
                    @Override
                    public void result(TestResult testResult) {
                        changeData();
                        dismissProgress();
                    }
                });
            }
        });

        btnRunAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runAll();
            }
        });
        return mRootView;
    }

    private void runAll() {
        mCounter = 0;
        mProgress.show();
        for (int i = 0; i < mData.size(); i++) {
            mData.get(i).getTestResult().setStatus(TestStatus.UNKNOWN);
            mTestCaseAdapter.changeData(mData);
            mData.get(i).run(new TestCallback() {
                @Override
                public void result(TestResult testResult) {
                    mCounter++;
                    changeData();
                    if (mCounter == mData.size())
                        dismissProgress();
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mProgress != null && mProgress.isShowing())
            mProgress.dismiss();
        mProgress = null;
    }

    private void changeData() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTestCaseAdapter.changeData(mData);
            }
        });
    }

    private void dismissProgress() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgress.dismiss();
            }
        });
    }
}
