package app.flora.driver.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import app.flora.driver.R;
import app.flora.driver.activities.MainActivity;
import app.flora.driver.classes.Constants;
import app.flora.driver.classes.Navigator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OrdersFragment extends Fragment {
    public static FragmentActivity activity;
    public static OrdersFragment fragment;
    private View childView;

    @BindView(R.id.fragment_orders_v_upcomingSelector)
    View upcomingSelector;
    @BindView(R.id.fragment_orders_v_currentSelector)
    View currentSelector;


    public static OrdersFragment newInstance(FragmentActivity activity) {
        fragment = new OrdersFragment();
        OrdersFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childView = inflater.inflate(R.layout.fragment_orders, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (activity == null) {
            activity = getActivity();
        }
        MainActivity.setupAppbar(Constants.ORDERS, null);
        initTabs();
    }

    private void initTabs() {
        upcomingSelector.setVisibility(View.VISIBLE);
        currentSelector.setVisibility(View.INVISIBLE);
        Navigator.loadFragment(activity,UpcomingOrdersFragment.newInstance(activity),R.id.fragment_orders_fl_ordersContainer,false);
    }

    @OnClick(R.id.fragment_orders_ll_upcoming)
    public void upcomingClick() {
        upcomingSelector.setVisibility(View.VISIBLE);
        currentSelector.setVisibility(View.INVISIBLE);
        Navigator.loadFragment(activity,UpcomingOrdersFragment.newInstance(activity),R.id.fragment_orders_fl_ordersContainer,false);
    }

    @OnClick(R.id.fragment_orders_ll_current)
    public void currentClick() {
        upcomingSelector.setVisibility(View.INVISIBLE);
        currentSelector.setVisibility(View.VISIBLE);
        Navigator.loadFragment(activity,CurrentOrdersFragment.newInstance(activity),R.id.fragment_orders_fl_ordersContainer,false);
    }


}
