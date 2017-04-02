package cz.csas.demo.corporate;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.thuongnh.zprogresshud.ZProgressHUD;
import cz.csas.corporate.Corporate;
import cz.csas.corporate.companies.Company;
import cz.csas.corporate.companies.CompanyProfile;
import cz.csas.corporate.companies.RelationshipType;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.demo.Constants;
import cz.csas.demo.R;
import cz.csas.demo.components.DetailItem;


/**
 * The type Account desc fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /03/17.
 */
public class CompanyDescFragment extends Fragment {

    @Bind(R.id.di_reg_num)
    DetailItem mDiRegNum;

    @Bind(R.id.di_tax_num)
    DetailItem mDiTaxNum;

    @Bind(R.id.di_legal_form)
    DetailItem mDiLegalForm;

    @Bind(R.id.di_legal_form_i18n)
    DetailItem mDiLegalFormI18N;

    @Bind(R.id.di_name)
    DetailItem mDiName;

    @Bind(R.id.di_cnb_type)
    DetailItem mDiCnbType;

    @Bind(R.id.di_cnb_type_i18n)
    DetailItem mDiCnbTypeI18N;

    @Bind(R.id.di_industry_category)
    DetailItem mDiIndustryCategory;

    @Bind(R.id.di_industry_category_i18n)
    DetailItem mDiIndustryCategoryI18N;

    @Bind(R.id.di_sector)
    DetailItem mDiSector;

    @Bind(R.id.di_sector_i18n)
    DetailItem mDiSectorI18N;

    @Bind(R.id.di_relationship_type)
    DetailItem mDiRelationshipType;

    @Bind(R.id.di_relationship_type_i18n)
    DetailItem mDiBalance;

    @Bind(R.id.btn_campaigns)
    Button mBtnCampaigns;

    @Bind(R.id.btn_relationship_managers)
    Button mBtnRelationshipManagers;

    private View mRootView;
    private ZProgressHUD mProgress;
    private FragmentCallback mFragmentCallback;
    private Company mCompany;
    private boolean mProgressResult = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentCallback = (FragmentCallback) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.corporate_company_fragment, container, false);
        ButterKnife.bind(this, mRootView);

        mProgress = ZProgressHUD.getInstance(getActivity());
        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();
        final Bundle bundle = this.getArguments();
        if (bundle != null && bundle.get(Constants.COMPANY_ID_EXTRA) != null) {

            final String id = bundle.getString(Constants.COMPANY_ID_EXTRA);

            Corporate.getInstance().getCorporateClient().getCompaniesResource().withId(id).get(new CallbackWebApi<Company>() {
                @Override
                public void success(Company company) {
                    mCompany = company;
                    if (mCompany != null)
                        setCompanyDetail();
                    mProgress.dismiss();
                }

                @Override
                public void failure(CsSDKError error) {
                    mProgress.dismissWithFailure();
                }
            });

            mBtnCampaigns.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragmentCallback.changeFragmentToCompanyCampaigns(id);
                }
            });

            mBtnRelationshipManagers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragmentCallback.changeFragmentToCompanyRelationshipManagers(id);
                }
            });
        } else {
            mProgress.dismissWithFailure();
        }
        return mRootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mProgress != null && mProgress.isShowing())
            mProgress.dismiss();
        mProgress = null;
    }

    private void setCompanyDetail() {
        if (mCompany.getRegNum() != null)
            mDiRegNum.setValue(mCompany.getRegNum());
        if (mCompany.getTaxNum() != null)
            mDiTaxNum.setValue(mCompany.getTaxNum());
        if (mCompany.getLegalForm() != null)
            mDiLegalForm.setValue(mCompany.getLegalForm());
        if (mCompany.getLegalFormI18N() != null)
            mDiLegalFormI18N.setValue(mCompany.getLegalFormI18N());
        if (mCompany.getName() != null)
            mDiName.setValue(mCompany.getName());

        CompanyProfile companyProfile = mCompany.getCompanyProfile();
        if (companyProfile != null) {
            if (companyProfile.getCnbType() != null)
                mDiCnbType.setValue(companyProfile.getCnbType());
            if (companyProfile.getCnbTypeI18N() != null)
                mDiCnbTypeI18N.setValue(companyProfile.getCnbTypeI18N());
            if (companyProfile.getIndustryCategory() != null)
                mDiIndustryCategory.setValue(companyProfile.getIndustryCategory());
            if (companyProfile.getIndustryCategoryI18N() != null)
                mDiIndustryCategoryI18N.setValue(companyProfile.getIndustryCategoryI18N());
            if (companyProfile.getSector() != null)
                mDiSector.setValue(companyProfile.getSector());
            if (companyProfile.getSectorI18N() != null)
                mDiSectorI18N.setValue(companyProfile.getSectorI18N());
        }

        RelationshipType relationshipType = mCompany.getRelationshipType();
        if (relationshipType != null) {
            if (relationshipType.getRelationshipType() != null)
                mDiRelationshipType.setValue(relationshipType.getRelationshipType());
            if (relationshipType.getRelationshipTypeI18N() != null)
                mDiRelationshipType.setValue(relationshipType.getRelationshipTypeI18N());
        }
    }
}
