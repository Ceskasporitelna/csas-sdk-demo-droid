package cz.csas.demo.places;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.demo.App;
import cz.csas.demo.R;
import cz.csas.demo.components.AutocompleteAdapter;
import cz.csas.lockerui.utils.TypefaceUtils;
import cz.csas.places.AutocompleteAddress;
import cz.csas.places.AutocompleteCity;
import cz.csas.places.AutocompletePostCode;
import cz.csas.places.Place;
import cz.csas.places.PlaceListParameters;
import cz.csas.places.Places;
import cz.csas.places.autocomplete.AutocompleteAddressListResponse;
import cz.csas.places.autocomplete.AutocompleteCityListResponse;
import cz.csas.places.autocomplete.AutocompletePostCodeListResponse;
import cz.csas.places.places.PlaceListResponse;

/**
 * The type Place search fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 29 /04/16.
 */
public class PlaceAutocompleteFragment extends Fragment {

    /**
     * The M lv autocomplete.
     */
    @Bind(R.id.lv_place_autocomplete)
    ListView mLvAutocomplete;

    /**
     * The M btn address.
     */
    @Bind(R.id.btn_address)
    Button mBtnAddress;

    /**
     * The M btn city.
     */
    @Bind(R.id.btn_city)
    Button mBtnCity;

    /**
     * The M btn post code.
     */
    @Bind(R.id.btn_post_code)
    Button mBtnPostCode;

    /**
     * The M et autocomplete.
     */
    @Bind(R.id.et_autocomplete_text)
    EditText mEtAutocomplete;

    private View mRootView;
    private AutocompleteAdapter mAutocompleteAdapter;
    private FragmentCallback mFragmentCallback;
    private List mPlaces;
    private int mPressed = 1;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentCallback = (FragmentCallback) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.autocomplete_fragment, container, false);
        ButterKnife.bind(this, mRootView);
        mBtnAddress.setTypeface(TypefaceUtils.getRobotoRegular(getActivity()));
        mBtnCity.setTypeface(TypefaceUtils.getRobotoRegular(getActivity()));
        mBtnPostCode.setTypeface(TypefaceUtils.getRobotoRegular(getActivity()));
        mEtAutocomplete.setTypeface(TypefaceUtils.getRobotoRegular(getActivity()));
        mPlaces = new ArrayList<>();
        mAutocompleteAdapter = new AutocompleteAdapter(getActivity(), mPlaces);
        mLvAutocomplete.setAdapter(mAutocompleteAdapter);
        mLvAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String query = "";
                if (mAutocompleteAdapter.getItem(i) instanceof AutocompleteAddress)
                    query = ((AutocompleteAddress) mAutocompleteAdapter.getItem(i)).getAddress();
                else if (mAutocompleteAdapter.getItem(i) instanceof AutocompleteCity)
                    query = ((AutocompleteCity) mAutocompleteAdapter.getItem(i)).getCity();
                else if (mAutocompleteAdapter.getItem(i) instanceof AutocompletePostCode)
                    query = ((AutocompletePostCode) mAutocompleteAdapter.getItem(i)).getPostCode();
                PlaceListParameters parameters = new PlaceListParameters.Builder().setSearchQuery(query).create();
                Places.getInstance().getPlacesClient().getPlacesResource().list(parameters, new CallbackWebApi<PlaceListResponse>() {
                    @Override
                    public void success(PlaceListResponse placeListResponse) {
                        if (placeListResponse.getPlaces().size() > 0)
                            App.getPlaceManager().setAutocompletePlace(placeListResponse.getPlaces().get(0));
                        mFragmentCallback.changeFragmentToMap();
                    }

                    @Override
                    public void failure(CsSDKError error) {
                        App.getPlaceManager().setAutocompletePlace(new Place());
                        mFragmentCallback.changeFragmentToMap();
                    }
                });
            }
        });
        mBtnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPressed = 1;
                setButtons();
                setListData(mEtAutocomplete.getText() != null ? mEtAutocomplete.getText().toString() : null);
            }
        });

        mBtnCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPressed = 2;
                setButtons();
                setListData(mEtAutocomplete.getText() != null ? mEtAutocomplete.getText().toString() : null);
            }
        });

        mBtnPostCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPressed = 3;
                setButtons();
                setListData(mEtAutocomplete.getText() != null ? mEtAutocomplete.getText().toString() : null);
            }
        });

        mEtAutocomplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    setListData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setButtons();
        return mRootView;
    }

    private void getAddress(String query) {
        Places.getInstance().getPlacesClient().getAutocompleteResource().getAddressesResource().startingWith(query).list(new CallbackWebApi<AutocompleteAddressListResponse>() {
            @Override
            public void success(AutocompleteAddressListResponse autocompleteAddressListResponse) {
                mPlaces = autocompleteAddressListResponse.getAddresses();
                mAutocompleteAdapter.changeData(mPlaces);
            }

            @Override
            public void failure(CsSDKError error) {

            }
        });
    }

    private void getCity(String query) {
        Places.getInstance().getPlacesClient().getAutocompleteResource().getCitiesResource().startingWith(query).list(new CallbackWebApi<AutocompleteCityListResponse>() {
            @Override
            public void success(AutocompleteCityListResponse autocompleteCityListResponse) {
                mPlaces = autocompleteCityListResponse.getCities();
                mAutocompleteAdapter.changeData(mPlaces);
            }

            @Override
            public void failure(CsSDKError error) {

            }
        });
    }

    private void getPostCode(String query) {
        Places.getInstance().getPlacesClient().getAutocompleteResource().getPostCodesResource().startingWith(query).list(new CallbackWebApi<AutocompletePostCodeListResponse>() {
            @Override
            public void success(AutocompletePostCodeListResponse autocompletePostCodeListResponse) {
                mPlaces = autocompletePostCodeListResponse.getPostCodes();
                mAutocompleteAdapter.changeData(mPlaces);
            }

            @Override
            public void failure(CsSDKError error) {

            }
        });
    }

    private void setListData(String query) {
        if (query != null) {
            if (mPressed == 1)
                getAddress(query);
            else if (mPressed == 2)
                getCity(query);
            else
                getPostCode(query);
        }
    }

    private void setButtons() {
        if (mPressed == 1)
            setButtonsBackground(mBtnAddress, mBtnCity, mBtnPostCode);
        else if (mPressed == 2)
            setButtonsBackground(mBtnCity, mBtnAddress, mBtnPostCode);
        else
            setButtonsBackground(mBtnPostCode, mBtnCity, mBtnAddress);
    }

    private void setButtonsBackground(Button button1, Button button2, Button button3) {
        button1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_round_white));
        button1.setTextColor(ContextCompat.getColor(getActivity(), R.color.csasColorPrimaryDark));
        button2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_round_dark_blue));
        button2.setTextColor(ContextCompat.getColor(getActivity(), R.color.csasColorWhite));
        button3.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_round_dark_blue));
        button3.setTextColor(ContextCompat.getColor(getActivity(), R.color.csasColorWhite));
    }

}
