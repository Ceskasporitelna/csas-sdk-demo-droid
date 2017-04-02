package cz.csas.demo.uniforms;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.thuongnh.zprogresshud.ZProgressHUD;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.utils.csjson.CsJson;
import cz.csas.demo.Constants;
import cz.csas.demo.R;
import cz.csas.demo.components.ComboboxSpinnerAdapter;
import cz.csas.demo.components.FieldTypeAttachment;
import cz.csas.demo.components.FieldTypeCheckbox;
import cz.csas.demo.components.FieldTypeCombobox;
import cz.csas.demo.components.FieldTypePobocky;
import cz.csas.demo.components.FieldTypeRadio;
import cz.csas.demo.components.FieldTypeTextView;
import cz.csas.demo.components.FieldTypeView;
import cz.csas.demo.components.PobockyAdapter;
import cz.csas.uniforms.Attachment;
import cz.csas.uniforms.Branch;
import cz.csas.uniforms.FieldType;
import cz.csas.uniforms.FileCreateRequest;
import cz.csas.uniforms.FilledForm;
import cz.csas.uniforms.FilledFormField;
import cz.csas.uniforms.FilledFormRequest;
import cz.csas.uniforms.Form;
import cz.csas.uniforms.FormField;
import cz.csas.uniforms.SubmittedForm;
import cz.csas.uniforms.Uniforms;
import cz.csas.uniforms.UniformsClient;
import cz.csas.uniforms.UpdateFilledFormRequest;


/**
 * The type Form desc fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /01/16.
 */
public class FormDescFragment extends Fragment implements DialogInterface.OnDismissListener{

    /**
     * The Ll variable layout.
     */
    @Bind(R.id.ll_variable_form_layout)
    LinearLayout llVariableLayout;

    /**
     * The Sv fields.
     */
    @Bind(R.id.sv_fields)
    ScrollView svFields;

    /**
     * The Lv pobocky.
     */
    @Bind(R.id.lv_pobocky)
    ListView lvPobocky;

    /**
     * The Tv title.
     */
    @Bind(R.id.tv_title)
    TextView tvTitle;

    /**
     * The Btn save submit.
     */
    @Bind(R.id.btn_save_submit)
    Button btnSaveSubmit;

    private View mRootView;
    private ZProgressHUD mProgress;
    private FragmentCallback mFragmentCallback;
    private Form mForm;
    private FilledForm mFilledForm;
    private List<FilledFormField> mFilledFormFields;
    private boolean mProgressResult = false;
    private Attachment mAttachment;
    private FieldTypeAttachment mFieldTypeAttachment;
    private UniformsClient mUniformsClient;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentCallback = (FragmentCallback) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.form_desc_fragment, container, false);
        ButterKnife.bind(this, mRootView);

        mProgress = ZProgressHUD.getInstance(getActivity());
        mProgress.setMessage(getString(R.string.progress_loading));
        mProgress.show();
        mProgress.setOnDismissListener(this);
        mUniformsClient = Uniforms.getInstance().getUniformsClient();

        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.get(Constants.FORM_ID_EXTRA) != null){
            mUniformsClient.getFormsResource().withId(bundle.getLong(Constants.FORM_ID_EXTRA)).get(new CallbackWebApi<Form>() {
                @Override
                public void success(Form form) {
                    mForm = form;
                    setFormLayout();
                    mProgress.dismiss();
                }

                @Override
                public void failure(CsSDKError error) {
                    mProgress.dismissWithFailure();
                }
            });

        }else
            mFragmentCallback.changeFragmentToList();

        btnSaveSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.show();
                if(btnSaveSubmit.getText().equals(getString(R.string.form_save))){
                    saveFilledForm();
                }else if(mFilledForm != null){
                    submitFilledForm();
                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            mProgress = ZProgressHUD.getInstance(getActivity());
            mProgress.setMessage(getString(R.string.progress_uploading));
            mProgress.setOnDismissListener(this);
            mProgress.show();
            final File file = new CsJson().fromJson(data.getStringExtra(Constants.FILE_EXTRA),File.class);
            mUniformsClient.getAttachmentsResource().upload(new FileCreateRequest(file, file.getName()), new CallbackWebApi<Attachment>() {
                @Override
                public void success(Attachment attachment) {
                    mAttachment = attachment;
                    mFieldTypeAttachment.setTitleAttachment(attachment.getId());
                    mProgress.dismiss();
                }

                @Override
                public void failure(CsSDKError error) {
                    mProgress.dismissWithFailure();
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setFormLayout(){
        tvTitle.setText(mForm.getNameI18N());
        for (FormField formField : mForm.getFormFields()){
            FieldType fieldType = formField.getFieldType();
            if(fieldType.equals(FieldType.TEXTBOX) || fieldType.equals(FieldType.TEXTAREA)){
                FieldTypeTextView fieldTypeTextView = new FieldTypeTextView(getActivity());
                fieldTypeTextView.setTitle(formField.getNameI18N());
                fieldTypeTextView.setHint(getString(R.string.hint_et) + " " + formField.getNameI18N());
                fieldTypeTextView.setFieldId(formField.getId());
                llVariableLayout.addView(fieldTypeTextView);
            }else if (fieldType.equals(FieldType.COMBOBOX)) {
                final FieldTypeCombobox fieldTypeCombobox = new FieldTypeCombobox(getActivity());
                fieldTypeCombobox.setTitle(formField.getNameI18N());
                fieldTypeCombobox.setFieldId(formField.getId());
                Spinner spinner = fieldTypeCombobox.getSpinner();
                final ComboboxSpinnerAdapter comboboxSpinnerAdapter = new ComboboxSpinnerAdapter(formField.getOptionsI18N(),getActivity());
                spinner.setAdapter(comboboxSpinnerAdapter);
                llVariableLayout.addView(fieldTypeCombobox);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        fieldTypeCombobox.setText((String)comboboxSpinnerAdapter.getItem(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        fieldTypeCombobox.setText((String)comboboxSpinnerAdapter.getItem(0));
                    }
                });
            }else if (fieldType.equals(FieldType.POBOCKY)) {
                final FieldTypePobocky fieldTypePobocky = new FieldTypePobocky(getActivity());
                fieldTypePobocky.setTitle(formField.getNameI18N());
                fieldTypePobocky.setFieldId(formField.getId());
                final PobockyAdapter pobockyAdapter = new PobockyAdapter(formField.getBranchTypeMap(),getActivity());
                lvPobocky.setAdapter(pobockyAdapter);
                lvPobocky.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Branch branch = (Branch) pobockyAdapter.getItem(position);
                        fieldTypePobocky.setTitlePobocky(branch.getAddress() + ", " + branch.getCityName());
                        llVariableLayout.setVisibility(View.VISIBLE);
                        lvPobocky.setVisibility(View.INVISIBLE);
                    }
                });
                fieldTypePobocky.getTvPobocky().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llVariableLayout.setVisibility(View.INVISIBLE);
                        lvPobocky.setVisibility(View.VISIBLE);
                    }
                });
                llVariableLayout.addView(fieldTypePobocky);
            }else if (fieldType.equals(FieldType.CHECKBOX)){
                FieldTypeCheckbox fieldTypeCheckbox = new FieldTypeCheckbox(getActivity(),formField.getOptionsI18N());
                fieldTypeCheckbox.setFieldId(formField.getId());
                fieldTypeCheckbox.setTitle(formField.getNameI18N());
                llVariableLayout.addView(fieldTypeCheckbox);
            }else if (fieldType.equals(FieldType.RADIO)){
                FieldTypeRadio fieldTypeRadio = new FieldTypeRadio(getActivity(),formField.getOptionsI18N());
                fieldTypeRadio.setTitle(formField.getNameI18N());
                fieldTypeRadio.setFieldId(formField.getId());
                llVariableLayout.addView(fieldTypeRadio);
            }else if (fieldType.equals(FieldType.PRILOHA)){
                mFieldTypeAttachment = new FieldTypeAttachment(getActivity());
                mFieldTypeAttachment.setTitle(formField.getNameI18N());
                mFieldTypeAttachment.setFieldId(formField.getId());
                mFieldTypeAttachment.getTvAttachment().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().startActivityForResult(new Intent(getActivity(),CameraActivity.class),Constants.CAMERA_REQUEST_CODE);
                    }
                });
                llVariableLayout.addView(mFieldTypeAttachment);
            }else if(fieldType.equals(FieldType.GROUP)) {
                // GALERIE
            }
        }
    }

    private void evaluateFilledFormFields(){
        mFilledFormFields = new ArrayList<>();
        for (int i=0; i < llVariableLayout.getChildCount(); i++){
            View v = llVariableLayout.getChildAt(i);
            FilledFormField filledFormField;
            if(v instanceof FieldTypeView) {
                filledFormField = new FilledFormField(mForm.getFormFields().get(i).getId(), ((FieldTypeView) v).getText());
                mFilledFormFields.add(filledFormField);
            }
        }
    }

    private void saveFilledForm(){
        evaluateFilledFormFields();
        if(mFilledForm == null) {
            FilledFormRequest filledFormRequest = new FilledFormRequest(mForm.getId(), mFilledFormFields);
            mUniformsClient.getFilledFormsResource().create(filledFormRequest, new CallbackWebApi<FilledForm>() {
                @Override
                public void success(FilledForm filledForm) {
                    mFilledForm = filledForm;
                    checkForErrors();
                    checkFormOk();
                    mProgress.dismiss();
                }

                @Override
                public void failure(CsSDKError error) {
                    mProgress.dismissWithFailure();
                }
            });
        }else {
            UpdateFilledFormRequest updateFilledFormRequest = new UpdateFilledFormRequest(mFilledForm.getId(),mForm.getId(),mFilledFormFields);
            mUniformsClient.getFilledFormsResource().withId(mFilledForm.getId()).update(updateFilledFormRequest, new CallbackWebApi<FilledForm>() {
                @Override
                public void success(FilledForm filledForm) {
                    mFilledForm = filledForm;
                    checkForErrors();
                    checkFormOk();
                    mProgress.dismiss();
                }

                @Override
                public void failure(CsSDKError error) {
                    mProgress.dismissWithFailure();
                }
            });
        }

    }

    private void submitFilledForm(){
        mUniformsClient.getFilledFormsResource().withId(mFilledForm.getId()).submit(new CallbackWebApi<SubmittedForm>() {
            @Override
            public void success(SubmittedForm submittedForm) {
                mProgressResult = true;
                mProgress.dismissWithSuccess();
            }

            @Override
            public void failure(CsSDKError error) {
                btnSaveSubmit.setText(getString(R.string.form_save));
                mProgress.dismissWithFailure();
            }
        });
    }

    private void checkForErrors(){
        for (int i = 0; i<mFilledForm.getMessages().size(); i++){
            List<String> error;
            if(mFilledForm.isOk())
                error = null;
            else
                error = mFilledForm.getMessages().get(i).getMessages();
            for (int j = 0; j< llVariableLayout.getChildCount();j++) {
                View v = llVariableLayout.getChildAt(j);
                if(v instanceof FieldTypeView && ((FieldTypeView) v).getFieldId() == mFilledForm.getMessages().get(i).getFieldId()) {
                    ((FieldTypeView) v).setError(error);
                    break;
                }
            }
        }
    }

    private void checkFormOk(){
        if(mFilledForm.isOk())
            btnSaveSubmit.setText(getString(R.string.form_submit));
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        if(mProgressResult)
            mFragmentCallback.changeFragmentToList();
    }

    /**
     * Check pobocky visible boolean.
     *
     * @return the boolean
     */
    public boolean checkPobockyVisible(){
        if(lvPobocky.getVisibility() == View.VISIBLE){
            llVariableLayout.setVisibility(View.VISIBLE);
            lvPobocky.setVisibility(View.INVISIBLE);
            return true;
        }
        return false;
    }
}
