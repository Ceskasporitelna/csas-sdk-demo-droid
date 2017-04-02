package cz.csas.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import cz.csas.demo.places.LocationTrackerFallback;
import cz.csas.demo.places.PlacesManager;
import cz.csas.demo.places.PlacesManagetImpl;

/**
 * The type App.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 29 /04/16.
 */
public class App extends MultiDexApplication {

    private static final String FAKE_VERSION_KEY = "fake_version";
    private static final String PREFS = "demo_csas_prefs";

    private static LocationTrackerFallback sLocationManager;
    private static PlacesManager sPlaceManager;
    private static SharedPreferences demoPreferences;
    private static SharedPreferences.Editor demoEditor;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    /**
     * Init.
     */
    public void init() {
        sLocationManager = new LocationTrackerFallback(this);
        sPlaceManager = new PlacesManagetImpl(this);
        demoPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        demoEditor = demoPreferences.edit();
        context = this;
    }

    /**
     * Gets location manager.
     *
     * @return the location manager
     */
    public static LocationTrackerFallback getLocationManager() {
        return sLocationManager;
    }

    /**
     * Gets place manager.
     *
     * @return the place manager
     */
    public static PlacesManager getPlaceManager() {
        return sPlaceManager;
    }

    /**
     * Sets fake version.
     *
     * @param fakeVersion the fake version
     */
    public static void setFakeVersion(boolean fakeVersion) {
        demoEditor.putBoolean(FAKE_VERSION_KEY, fakeVersion);
        demoEditor.commit();
    }

    /**
     * Gets fake version.
     *
     * @return the fake version
     */
    public static boolean getFakeVersion() {
        return demoPreferences.getBoolean(FAKE_VERSION_KEY, false);
    }

    public static Context get() {
        return context;
    }
}
