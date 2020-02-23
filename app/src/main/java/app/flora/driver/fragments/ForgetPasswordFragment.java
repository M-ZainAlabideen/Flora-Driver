package app.flora.driver.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import app.flora.driver.classes.Navigator;
import app.flora.driver.classes.SessionManager;
import app.flora.driver.webservices.RetrofitConfig;
import app.flora.driver.webservices.responses.GetCustomer;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordFragment extends Fragment {
    public static FragmentActivity activity;
    public static ForgetPasswordFragment fragment;
    private SessionManager sessionManager;
    private View childView;
    private GetCustomer customer;
    String emailTxt;

    @BindView(R.id.fragment_forget_password_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_forget_password_til_emailContainer)
    TextInputLayout emailContainer;
    @BindView(R.id.fragment_forget_password_et_email)
    TextInputEditText email;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static ForgetPasswordFragment newInstance(FragmentActivity activity) {
        fragment = new ForgetPasswordFragment();
        ForgetPasswordFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childView = inflater.inflate(R.layout.fragment_forget_password, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(activity);
        if (activity == null) {
            activity = getActivity();
        }
        MainActivity.setupAppbar(Constants.NO_FLAG, null);
        FixControl.setupUI(activity, childView);
        loading.setVisibility(View.GONE);
        emailListeners();
    }

    private void emailListeners() {
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

    }


    @OnClick(R.id.fragment_forget_password_tv_send)
    public void sendClick() {
        emailTxt = email.getText().toString();
        if (emailTxt == null || emailTxt.isEmpty()) {
            emailContainer.setError(getString(R.string.enterEmail));
        } else if (!FixControl.isValidEmail(emailTxt)) {
            emailContainer.setError(getString(R.string.invalidEmail));
        } else {
            emailContainer.setError(null);
        }
        if (emailTxt != null && FixControl.isValidEmail(emailTxt)) {
            forgetPasswordApi(emailTxt);
        }
    }

    private void forgetPasswordApi(String email) {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().FORGET_PASSWORD_CALL(sessionManager.getUserLanguage(),Constants.AUTHORIZATION_VALUE,email)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int statusCode = response.code();
                        if (statusCode == 200) {
                            Navigator.loadFragment(activity,LoginFragment.newInstance(activity),R.id.activity_main_fl_mainAppContainer,false);
                            Snackbar.make(childView,getString(R.string.checkEmail),Snackbar.LENGTH_SHORT).show();
                        }else {
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
}



