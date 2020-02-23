package app.flora.driver.classes;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SessionManager {
    Context context;
    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor editor;
    public static final String USER_PREF = "userPreference";
    private static final String IS_LOGGED = "isLogged";
    private static final String USER_ID = "id";
    private static final String LANGUAGE_CODE = "languageCode";
    private static String IS_NOTIFICATION_ON = "is_Notification_on";
    private static final String REG_ID = "reg_id";

    public SessionManager(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences(USER_PREF, MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void loginSession() {
        editor.putBoolean(IS_LOGGED, true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPref.getBoolean(IS_LOGGED, false);
    }

    public void logout() {
        setUserId(null);
        editor.putBoolean(IS_LOGGED, false);
        editor.apply();
    }

    public void setUserId(String id) {
        editor.putString(USER_ID, id);
        editor.apply();
    }

    public String getUserId() {
        return sharedPref.getString(USER_ID, null);
    }

    public void setUserLanguage(String languageCode) {
        editor.putString(LANGUAGE_CODE, languageCode);
        editor.apply();
    }

    public String getUserLanguage() {
        return sharedPref.getString(LANGUAGE_CODE, "");
    }

    public boolean isNotificationOn(){
        return  sharedPref.getBoolean(IS_NOTIFICATION_ON,true);
    }

    public String getRegId() {
        return sharedPref.getString(REG_ID, "");
    }

    public void setRegId(String id) {
        editor.putString(REG_ID, id);
        editor.commit();
    }
}
