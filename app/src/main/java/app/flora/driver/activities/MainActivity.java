package app.flora.driver.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.flora.driver.R;
import app.flora.driver.classes.Constants;
import app.flora.driver.classes.GlobalFunctions;
import app.flora.driver.classes.LocaleHelper;
import app.flora.driver.classes.Navigator;
import app.flora.driver.classes.SessionManager;
import app.flora.driver.fragments.LoginFragment;
import app.flora.driver.fragments.OrdersFragment;
import app.flora.driver.fragments.UpcomingOrdersFragment;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends AppCompatActivity {

    public static CardView appbarContainer;
    public static Toolbar toolbar;
    public static ImageView appbarLogo;
    public static TextView title;
    public static SearchView search;
    public static ImageView filter;
    public static ImageView back;
    public static ImageView reset;
    public static boolean isEnglish = true;
    private SessionManager sessionManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(LocaleHelper.onAttach(newBase)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        GlobalFunctions.setDefaultLanguage(this);
        GlobalFunctions.setUpFont(this);
        sessionManager = new SessionManager(this);

        appbarContainer = (CardView) findViewById(R.id.activity_main_cv_appbarContainer);
        toolbar = (Toolbar) findViewById(R.id.activity_main_tb_toolbar);
        appbarLogo = (ImageView) findViewById(R.id.activity_main_iv_appbarLogo);
        title = (TextView) findViewById(R.id.activity_main_tv_title);
        search = (SearchView) findViewById(R.id.activity_main_sv_search);
        filter = (ImageView) findViewById(R.id.activity_main_iv_filter);
        back = (ImageView) findViewById(R.id.activity_main_iv_back);
        reset = (ImageView) findViewById(R.id.activity_main_tv_reset);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (!sessionManager.isLoggedIn()) {
            Navigator.loadFragment(this, LoginFragment.newInstance(this), R.id.activity_main_fl_mainAppContainer, false);

        } else {
            Navigator.loadFragment(this, OrdersFragment.newInstance(this), R.id.activity_main_fl_mainAppContainer, false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            sessionManager.logout();
            Navigator.loadFragment(this, LoginFragment.newInstance(this), R.id.activity_main_fl_mainAppContainer, false);
        } else if (id == R.id.changeLang) {
            changeLanguage();
        }
        return super.onOptionsItemSelected(item);
    }


    //the back button action of all the app
    @OnClick(R.id.activity_main_iv_back)
    public void back() {
        onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (!search.isIconified()) {
                search.onActionViewCollapsed();
            } else {
                onBackPressed();
            }
        }
        return true;
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

        LocaleHelper.setLocale(this, sessionManager.getUserLanguage());
        finish();
        overridePendingTransition(0, 0);
        startActivity(new Intent(this, MainActivity.class));
        GlobalFunctions.setUpFont(this);
    }

    public static void setupAppbar(String flag, String titleTxt) {
        if (flag.equals(Constants.ORDERS)) {
            appbarContainer.setVisibility(View.VISIBLE);
            appbarLogo.setVisibility(View.VISIBLE);
            search.setVisibility(View.VISIBLE);
            filter.setVisibility(View.VISIBLE);
            title.setVisibility(View.INVISIBLE);
            reset.setVisibility(View.VISIBLE);
        } else if (flag.equals(Constants.PRODUCT)) {
            appbarContainer.setVisibility(View.VISIBLE);
            appbarLogo.setVisibility(View.INVISIBLE);
            search.setVisibility(View.INVISIBLE);
            filter.setVisibility(View.INVISIBLE);
            reset.setVisibility(View.INVISIBLE);
            title.setVisibility(View.VISIBLE);
            title.setText(titleTxt);
        } else {
            appbarContainer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        gotoDetails(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();

        gotoDetails(getIntent());

    }

    private void gotoDetails(Intent intent) {

        if (intent.hasExtra("Id")) {
            Log.d("gotoDetails", "1 -> " + intent.getStringExtra("type"));

            Log.d("gotoDetails", "2 -> " + intent.getStringExtra("Id"));

//            Navigator.loadFragment(this, OrderProductsFragment.newInstance(this, Integer.parseInt(intent.getStringExtra("Id"))),
//                    R.id.activity_main_fl_mainAppContainer, true);

        }
    }
}

