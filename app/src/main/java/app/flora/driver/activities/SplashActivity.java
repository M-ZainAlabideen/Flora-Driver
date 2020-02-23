package app.flora.driver.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import app.flora.driver.R;
import app.flora.driver.classes.GlobalFunctions;
import app.flora.driver.classes.LocaleHelper;
import app.flora.driver.classes.SessionManager;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 3000;
    private String regId = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(LocaleHelper.onAttach(newBase)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make the screen without statusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        GlobalFunctions.setDefaultLanguage(this);
        GlobalFunctions.setUpFont(this);
        //getFirebaseToken();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, SPLASH_DISPLAY_LENGTH);

    }


    private void getFirebaseToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("splash", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        regId = task.getResult().getToken();

                        Log.e("registrationId Splash ", "regId -> " + regId);

                        insertTokenApi(regId);


                    }
                });
    }
    private void insertTokenApi(final String regId) {
//        EsaalApiConfig.getCallingAPIInterface().addDeviceToken(
//                sessionManager.getUserToken(),
//                sessionManager.getUserId(),
//                regId, 2,
//                AppController.getInstance().getDeviceID(),
//                new Callback<Response>() {
//                    @Override
//                    public void success(Response response, Response response2) {
//                        if (response.getStatus() == 200) {
//                            sessionManager.setRegId(regId);
//                        }
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//
//                    }
//                }
//        );
    }
}
