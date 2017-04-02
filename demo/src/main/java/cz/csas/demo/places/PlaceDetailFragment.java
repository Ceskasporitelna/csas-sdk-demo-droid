package cz.csas.demo.places;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.demo.App;
import cz.csas.demo.Constants;
import cz.csas.demo.R;
import cz.csas.demo.components.DetailHeader;
import cz.csas.demo.components.DetailItem;
import cz.csas.demo.utils.LocationUtils;
import cz.csas.places.ATM;
import cz.csas.places.Branch;
import cz.csas.places.OpeningHours;
import cz.csas.places.Place;
import cz.csas.places.Places;
import cz.csas.places.Service;
import cz.csas.places.TimeInterval;
import cz.csas.places.Type;

/**
 * The type Place detail fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 29 /04/16.
 */
public class PlaceDetailFragment extends Fragment {

    /**
     * The Ll detail.
     */
    @Bind(R.id.ll_detail)
    LinearLayout llDetail;

    /**
     * The Pdh name.
     */
    @Bind(R.id.pdh_name)
    DetailHeader pdhName;

    /**
     * The Pdi id.
     */
    @Bind(R.id.pdi_identifier)
    DetailItem pdiId;

    /**
     * The Pdi location.
     */
    @Bind(R.id.pdi_location)
    DetailItem pdiLocation;

    /**
     * The Pdi type.
     */
    @Bind(R.id.pdi_type)
    DetailItem pdiType;

    /**
     * The Pdi state.
     */
    @Bind(R.id.pdi_state)
    DetailItem pdiState;

    /**
     * The Get pdi state note.
     */
    @Bind(R.id.pdi_state_note)
    DetailItem pdiStateNote;

    /**
     * The Pdi address.
     */
    @Bind(R.id.pdi_address)
    DetailItem pdiAddress;

    /**
     * The Pdi city.
     */
    @Bind(R.id.pdi_city)
    DetailItem pdiCity;

    /**
     * The Pdi post code.
     */
    @Bind(R.id.pdi_post_code)
    DetailItem pdiPostCode;

    /**
     * The Pdi region.
     */
    @Bind(R.id.pdi_region)
    DetailItem pdiRegion;

    /**
     * The Pdi country.
     */
    @Bind(R.id.pdi_country)
    DetailItem pdiCountry;

    /**
     * The Pdi distance.
     */
    @Bind(R.id.pdi_distance)
    DetailItem pdiDistance;

    /**
     * The Pdh opening hours.
     */
    @Bind(R.id.pdh_opening_hours)
    DetailHeader pdhOpeningHours;

    /**
     * The Pdi monday.
     */
    @Bind(R.id.pdi_monday)
    DetailItem pdiMonday;

    /**
     * The Pdi tuesday.
     */
    @Bind(R.id.pdi_tuesday)
    DetailItem pdiTuesday;

    /**
     * The Pdi wednesday.
     */
    @Bind(R.id.pdi_wednesday)
    DetailItem pdiWednesday;

    /**
     * The Pdi thursday.
     */
    @Bind(R.id.pdi_thursday)
    DetailItem pdiThursday;

    /**
     * The Pdi friday.
     */
    @Bind(R.id.pdi_friday)
    DetailItem pdiFriday;

    /**
     * The Pdi saturday.
     */
    @Bind(R.id.pdi_saturday)
    DetailItem pdiSaturday;

    /**
     * The Pdi sunday.
     */
    @Bind(R.id.pdi_sunday)
    DetailItem pdiSunday;

    /**
     * The Pdh services.
     */
    @Bind(R.id.pdh_services)
    DetailHeader pdhServices;


    private View mRootView;
    private Place mPlace;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.place_detail_fragment, container, false);
        ButterKnife.bind(this, mRootView);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String id = bundle.getString(Constants.ID_EXTRA);
            String type = bundle.getString(Constants.TYPE_EXTRA);
            if (type != null && id != null) {
                if (type.equals(Type.ATM.getValue()))
                    callATMs(id);
                else if (type.equals(Type.BRANCH.getValue()))
                    callBranches(id);
            }

        }
        return mRootView;
    }

    private void callBranches(String id) {
        Places.getInstance().getPlacesClient().getBranchesResource().withId(id).get(new CallbackWebApi<Branch>() {
            @Override
            public void success(Branch branch) {
                mPlace = branch;
                setData();
            }

            @Override
            public void failure(CsSDKError error) {

            }
        });
    }

    private void callATMs(String id) {
        Places.getInstance().getPlacesClient().getATMsResource().withId(id).get(new CallbackWebApi<ATM>() {
            @Override
            public void success(ATM atm) {
                mPlace = atm;
                setData();
            }

            @Override
            public void failure(CsSDKError error) {

            }
        });
    }

    private void setData() {
        if (mPlace != null) {
            pdhName.setDetailHeader(mPlace.getName());
            pdiId.setValue(String.valueOf(mPlace.getId()));
            pdiLocation.setValue(mPlace.getLocation().getLat() + " " + mPlace.getLocation().getLng());
            if (mPlace.getType() != null)
                pdiType.setValue(mPlace.getType().getValue());
            if (mPlace.getState() != null)
                pdiState.setValue(mPlace.getState().getValue());
            pdiStateNote.setValue(mPlace.getStateNote());
            pdiAddress.setValue(mPlace.getAddress());
            pdiCity.setValue(mPlace.getCity());
            pdiPostCode.setValue(mPlace.getPostCode());
            pdiRegion.setValue(mPlace.getRegion());
            pdiCountry.setValue(mPlace.getCountry());
            pdiDistance.setValue(String.format("%s", (int) LocationUtils.getDistance(mPlace.getLocation(), App.getLocationManager().getLocation())) + " m");
            if (mPlace.getOpeningHours() != null)
                setOpeningHours();
            if (mPlace.getServices() != null && mPlace.getServices().size() > 0)
                setServices();
        }
    }

    private void setOpeningHours() {
        for (OpeningHours openingHours : mPlace.getOpeningHours()) {
            switch (openingHours.getWeekday()) {
                case MONDAY:
                    pdiMonday.setValue(setInterval(openingHours.getIntervals()));
                    break;
                case TUESDAY:
                    pdiTuesday.setValue(setInterval(openingHours.getIntervals()));
                    break;
                case WEDNESDAY:
                    pdiWednesday.setValue(setInterval(openingHours.getIntervals()));
                    break;
                case THURSDAY:
                    pdiThursday.setValue(setInterval(openingHours.getIntervals()));
                    break;
                case FRIDAY:
                    pdiFriday.setValue(setInterval(openingHours.getIntervals()));
                    break;
                case SATURDAY:
                    pdiSaturday.setValue(setInterval(openingHours.getIntervals()));
                    break;
                case SUNDAY:
                    pdiSunday.setValue(setInterval(openingHours.getIntervals()));
                    break;
            }
        }
    }

    private String setInterval(List<TimeInterval> intervals) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < intervals.size(); i++) {
            stringBuilder.append(intervals.get(i).getFrom() + "-" + intervals.get(i).getTo());
            if (i != intervals.size() - 1)
                stringBuilder.append(",");
        }
        if (stringBuilder.length() == 0)
            stringBuilder.append("-");
        return stringBuilder.toString();
    }

    private void setServices() {
        for (Service service : mPlace.getServices()) {
            DetailItem item = new DetailItem(getActivity());
            item.setTitle(service.getFlag());
            item.setValue(service.getDesc());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
            item.setLayoutParams(params);
            llDetail.addView(item);
        }
    }
}
