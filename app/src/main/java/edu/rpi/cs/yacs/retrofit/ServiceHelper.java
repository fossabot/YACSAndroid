package edu.rpi.cs.yacs.retrofit;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.lang.annotation.Retention;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Mark Robinson on 9/24/16.
 */

class ServiceHelper {
    @Retention(RUNTIME)
    @interface Json {
    }

    private YACSService service;
    private Activity activity;

    private ServiceHelper(Activity activity) {
        this.activity = activity;

        invalidateService();
    }

    public YACSService getService() {
        return service;
    }

    /**
     * This method is used when we need to invalidate the YACSService
     * @return new YACSService that uses the latest "House of YACS" setting
     */
    public YACSService invalidateService() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);

        // If exists, return String value
        // Otherwise, set to "unknown"
        String houseOfYACS = preferences.getString("House of YACS", "unknown");

        String baseURL = convertHouseToURL(houseOfYACS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(new JsonStringConverterFactory(GsonConverterFactory.create()))
                .build();

        service = retrofit.create(YACSService.class);

        return service;
    }

    /**
     * This method returns the YACS URL of the input college name.
     * @param house College name
     * @return College YACS URL if College supports YACS. Otherwise, returns "not supported yet"
     */
    private String convertHouseToURL(String house) {
        switch (house) {
            case "Rensselaer Polytechnic Institute":
                return "https://yacs.cs.rpi.edu/api/v5/";
            default :
                return "not supported yet";
        }
    }
}