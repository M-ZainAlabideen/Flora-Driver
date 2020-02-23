package app.flora.driver.pushnotifications;

import android.os.AsyncTask;

import com.google.firebase.messaging.FirebaseMessagingService;

import app.flora.driver.classes.SessionManager;


public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    SessionManager sessionManager;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        sessionManager = new SessionManager(this);
        sendRegistrationToServer(s);
    }

    private void sendRegistrationToServer(final String regId) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                msg = "Device registered, registration ID=" + regId;
                return msg;
            }
            @Override
            protected void onPostExecute(String msg) {
                insertTokenApi(regId);
            }
        }.execute(null, null, null);
    }

    private void insertTokenApi(final String regId) {
//        EsaalApiConfig.getCallingAPIInterface().addDeviceToken(
//                sessionManager.getUserToken(),
//                sessionManager.getUserId(),
//                regId,2,
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
