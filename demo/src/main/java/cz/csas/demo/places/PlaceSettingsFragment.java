package cz.csas.demo.places;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.csas.demo.App;
import cz.csas.demo.R;
import cz.csas.lockerui.utils.TypefaceUtils;

/**
 * The type Place settings fragment.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 03 /05/16.
 */
public class PlaceSettingsFragment extends Fragment {

    private View mRootView;
    private FragmentCallback mFragmentCallback;

    /**
     * The Tv filter.
     */
    @Bind(R.id.tv_filter)
    TextView tvFilter;

    /**
     * The Tv radius.
     */
    @Bind(R.id.tv_radius)
    TextView tvRadius;

    /**
     * The Sb radius.
     */
    @Bind(R.id.sb_radius)
    SeekBar sbRadius;

    /**
     * The Btn atm.
     */
    @Bind(R.id.btn_atm)
    Button btnAtm;

    /**
     * The Btn branch.
     */
    @Bind(R.id.btn_branch)
    Button btnBranch;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentCallback = (FragmentCallback) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.place_settings_fragment, container, false);
        ButterKnife.bind(this, mRootView);
        setButtonsBackground();
        tvRadius.setTypeface(TypefaceUtils.getRobotoRegular(getActivity()));
        btnAtm.setTypeface(TypefaceUtils.getRobotoRegular(getActivity()));
        btnBranch.setTypeface(TypefaceUtils.getRobotoRegular(getActivity()));
        btnAtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.getPlaceManager().isAtmEnabled()) {
                    App.getPlaceManager().setAtmEnabled(false);
                    if (!App.getPlaceManager().isBranchEnabled())
                        App.getPlaceManager().setBranchEnabled(true);
                } else
                    App.getPlaceManager().setAtmEnabled(true);
                setButtonsBackground();
            }
        });
        btnBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.getPlaceManager().isBranchEnabled()) {
                    App.getPlaceManager().setBranchEnabled(false);
                    if (!App.getPlaceManager().isAtmEnabled())
                        App.getPlaceManager().setAtmEnabled(true);
                } else
                    App.getPlaceManager().setBranchEnabled(true);
                setButtonsBackground();
            }
        });

        sbRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                App.getPlaceManager().setRadius(progress);
                setRadiusText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        int radius = App.getPlaceManager().getRadius();
        sbRadius.setProgress(radius);
        setRadiusText(radius);
        return mRootView;
    }

    private void setButtonsBackground() {
        if (App.getPlaceManager().isAtmEnabled()) {
            btnAtm.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_round_dark_blue));
            btnAtm.setTextColor(ContextCompat.getColor(getActivity(), R.color.csasColorWhite));
        } else {
            btnAtm.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_round_white));
            btnAtm.setTextColor(ContextCompat.getColor(getActivity(), R.color.csasColorPrimaryDark));
        }
        if (App.getPlaceManager().isBranchEnabled()) {
            btnBranch.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_round_dark_blue));
            btnBranch.setTextColor(ContextCompat.getColor(getActivity(), R.color.csasColorWhite));
        } else {
            btnBranch.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_round_white));
            btnBranch.setTextColor(ContextCompat.getColor(getActivity(), R.color.csasColorPrimaryDark));
        }
    }

    private void setRadiusText(int radius) {
        tvRadius.setText(getString(R.string.radius, radius));
    }


}
