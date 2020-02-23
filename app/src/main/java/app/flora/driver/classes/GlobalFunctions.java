package app.flora.driver.classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import app.flora.driver.R;
import app.flora.driver.activities.MainActivity;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class GlobalFunctions {
    public static SessionManager sessionManager;

    public static void setUpFont(Context context) {
        sessionManager = new SessionManager(context);
        if (sessionManager.getUserLanguage().equals(Constants.EN)) {
            ViewPump.init(ViewPump.builder()
                    .addInterceptor(new CalligraphyInterceptor(
                            new CalligraphyConfig.Builder()
                                    .setDefaultFontPath(Constants.POPPINS_REGULAR)
                                    .setFontAttrId(R.attr.fontPath)
                                    .build()))
                    .build());
        } else {
            ViewPump.init(ViewPump.builder()
                    .addInterceptor(new CalligraphyInterceptor(
                            new CalligraphyConfig.Builder()
                                    .setDefaultFontPath(Constants.CAIRO_REGULAR)
                                    .setFontAttrId(R.attr.fontPath)
                                    .build()))
                    .build());
        }
    }

    public static void DisableLayout(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                DisableLayout((ViewGroup) child);
            } else {
                child.setEnabled(false);
            }
        }
    }

    public static void EnableLayout(ViewGroup layout) {
        layout.setEnabled(true);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                EnableLayout((ViewGroup) child);
            } else {
                child.setEnabled(true);
            }
        }
    }

    public static String formatDate(String date) {
        String dateResult = "";
        Locale locale = new Locale(Constants.EN);


        SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", locale);
        SimpleDateFormat dateFormatter2 = new SimpleDateFormat("MM/dd/yyyy", locale);

        //SimpleDateFormat dateFormatter2 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aaa", locale);

        int index = date.lastIndexOf('/');

        try {

            dateResult = dateFormatter2.format(dateFormatter1.parse(date.substring(index + 1)));

        } catch (ParseException e) {

            e.printStackTrace();

        }

        return dateResult;
    }

    public static String formatDateAndTime(String dateAndTime) {
        String dateResult = "";
        Locale locale = new Locale(Constants.EN);
        SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", locale);
        SimpleDateFormat dateFormatter2 = new SimpleDateFormat("MM/dd/yyyy - hh:mm aaa", locale);
        int index = dateAndTime.lastIndexOf('/');

        try {

            dateResult = dateFormatter2.format(dateFormatter1.parse(dateAndTime.substring(index + 1)));

        } catch (ParseException e) {

            e.printStackTrace();

        }

        dateFormatter2.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = dateFormatter2.parse(dateResult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateFormatter2.setTimeZone(TimeZone.getDefault());
        String formattedDate = dateFormatter2.format(date);
        return formattedDate;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected() && netInfo.isAvailable()) {
            return true;
        } else {
            return false;
        }

    }

    public static void handleError(Response<ResponseBody> response,View childView) {

        ResponseBody body = response.errorBody();
        String outResponse = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(body.byteStream()));
            StringBuilder out = new StringBuilder();
            String newLine = System.getProperty("line.separator");
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
                out.append(newLine);
            }
            outResponse = out.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (outResponse != null) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(outResponse);
                if (jsonObject.has("errors")) {
                    outResponse = jsonObject.getString("errors").replaceAll("\"", "");
                    if (outResponse.split("\"").length > 0) {
                        if (outResponse.split(",")[0].split(":").length > 1) {
                            outResponse = outResponse.split(":")[1].replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\}", "");
                            if (outResponse.contains("Please select")) ;
                            else {
                                Snackbar.make(childView, outResponse, Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void generalErrorMessage(Context context,View childView,ProgressBar loading) {
        loading.setVisibility(View.GONE);
        Snackbar.make(childView, context.getString(R.string.generalError), Snackbar.LENGTH_SHORT).show();
    }

    public static void setDefaultLanguage(Context context) {
        SessionManager sessionManager = new SessionManager(context);
        String language = sessionManager.getUserLanguage();
        if (language.equals(Constants.AR)) {
            MainActivity.isEnglish = false;
            LocaleHelper.setLocale(context,Constants.AR);
        } else {
            MainActivity.isEnglish = true;
            LocaleHelper.setLocale(context, Constants.EN);
            if (language == null || language.isEmpty()) {
                sessionManager.setUserLanguage(Constants.EN);
            }
        }
    }


}

