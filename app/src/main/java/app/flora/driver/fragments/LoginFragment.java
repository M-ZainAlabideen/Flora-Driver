package app.flora.driver.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;

import app.flora.driver.R;
import app.flora.driver.activities.MainActivity;
import app.flora.driver.classes.Constants;
import app.flora.driver.classes.FixControl;
import app.flora.driver.classes.GlobalFunctions;
import app.flora.driver.classes.LocaleHelper;
import app.flora.driver.classes.Navigator;
import app.flora.driver.classes.SessionManager;
import app.flora.driver.webservices.RetrofitConfig;
import app.flora.driver.webservices.models.Customer;
import app.flora.driver.webservices.responses.GetCustomer;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {
    public static FragmentActivity activity;
    public static LoginFragment fragment;
    private View childView;
    private SessionManager sessionManager;
    private GetCustomer customer;
    private String regId = "";
    String emailTxt;
    String passwordTxt;

    @BindView(R.id.fragment_login_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_login_til_emailContainer)
    TextInputLayout emailContainer;
    @BindView(R.id.fragment_login_et_email)
    TextInputEditText email;
    @BindView(R.id.fragment_login_til_passwordContainer)
    TextInputLayout passwordContainer;
    @BindView(R.id.fragment_login_et_password)
    TextInputEditText password;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static LoginFragment newInstance(FragmentActivity activity) {
        fragment = new LoginFragment();
        LoginFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (activity == null) {
            activity = getActivity();
        }
        MainActivity.setupAppbar(Constants.NO_FLAG, null);
        FixControl.setupUI(activity, childView);
        sessionManager = new SessionManager(activity);
        loading.setVisibility(View.GONE);
        emailAndPassListeners();
    }

    private void emailAndPassListeners() {
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailTxt = s.toString();
                if (emailTxt == null || emailTxt.isEmpty()) {
                    emailContainer.setError(getString(R.string.enterEmail));
                } else if (!FixControl.isValidEmail(emailTxt)) {
                    emailContainer.setError(getString(R.string.invalidEmail));
                } else {
                    emailContainer.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordTxt = s.toString();
                if (passwordTxt == null || passwordTxt.isEmpty()) {
                    passwordContainer.setError(getString(R.string.enterPassword));
                } else if (!FixControl.isValidPassword(passwordTxt)) {
                    passwordContainer.setError(getString(R.string.invalidPassword));
                } else {
                    passwordContainer.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.fragment_login_tv_forgetPassword)
    public void forgetPasswordClick() {
        Navigator.loadFragment(activity, ForgetPasswordFragment.newInstance(activity), R.id.activity_main_fl_mainAppContainer, true);
    }

    @OnClick(R.id.fragment_login_tv_login)
    public void loginClick() {
        emailTxt = email.getText().toString();
        passwordTxt = password.getText().toString();
        if (emailTxt == null || emailTxt.isEmpty()) {
            emailContainer.setError(getString(R.string.enterEmail));
        } else if (!FixControl.isValidEmail(emailTxt)) {
            emailContainer.setError(getString(R.string.invalidEmail));
        } else {
            emailContainer.setError(null);
        }
        if (passwordTxt == null || passwordTxt.isEmpty()) {
            passwordContainer.setError(getString(R.string.enterPassword));
        } else {
            passwordContainer.setError(null);
        }
        if (emailTxt != null && passwordTxt != null &&
                !emailTxt.isEmpty() && !passwordTxt.isEmpty()
                && FixControl.isValidEmail(emailTxt)) {
            loginApi(emailTxt, passwordTxt);
        }
    }

    @OnClick(R.id.fragment_login_tv_changeLang)
    public void changeLangClick() {
        changeLanguage();
    }

    private void changeLanguage() {
         /*for changing the language of App
        1- check the value of currentLanguage  and Reflects it
         2- set the new value of Language in local and change the value of languageSharedPreference to new value
         3- restart the mainActivity with noAnimation
        * */

        if (sessionManager.getUserLanguage().equals(Constants.EN)) {
            sessionManager.setUserLanguage(Constants.AR);
            MainActivity.isEnglish = false;
        } else {
            sessionManager.setUserLanguage(Constants.EN);
            MainActivity.isEnglish = true;
        }

        LocaleHelper.setLocale(activity, sessionManager.getUserLanguage());
        activity.finish();
        activity.overridePendingTransition(0, 0);
        startActivity(new Intent(activity, MainActivity.class));
        GlobalFunctions.setUpFont(activity);
    }

    private void loginApi(String userNameOrMail, String password) {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().LOGIN_CALL(sessionManager.getUserLanguage(), Constants.AUTHORIZATION_VALUE, userNameOrMail, password)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int statusCode = response.code();
                        if (statusCode == 200) {
                            handleResponse(response);
                            if (customer != null && customer.getCustomers() != null) {
                                Customer currentCustomer = customer.getCustomers().get(0);
                                sessionManager.setUserId(currentCustomer.getId());
                                sessionManager.loginSession();
                               // getFirebaseToken();
                                Navigator.loadFragment(activity, OrdersFragment.newInstance(activity), R.id.activity_main_fl_mainAppContainer, true);
                            }
                        } else {
                            GlobalFunctions.handleError(response, childView);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        GlobalFunctions.EnableLayout(container);
                        GlobalFunctions.generalErrorMessage(activity, childView, loading);
                    }
                });
    }

    private GetCustomer handleResponse(Response<ResponseBody> response) {
        ResponseBody body = response.body();
        String outResponse = "";
        String jsonResponse = "";
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
            jsonResponse = out.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (outResponse != null) {
            outResponse = outResponse.replace("\"", "");
            outResponse = outResponse.replace("\n", "");
            Type type = new TypeToken<GetCustomer>() {
            }.getType();
            JsonReader reader = new JsonReader(new StringReader(outResponse));
            reader.setLenient(true);
            customer = new Gson().fromJson(jsonResponse, type);
        }
        return customer;
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


