package app.flora.driver.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import app.flora.driver.R;
import app.flora.driver.activities.MainActivity;
import app.flora.driver.adapters.OrdersAdapter;
import app.flora.driver.adapters.ProductsAdapter;
import app.flora.driver.classes.Constants;
import app.flora.driver.classes.GlobalFunctions;
import app.flora.driver.classes.Navigator;
import app.flora.driver.classes.SessionManager;
import app.flora.driver.webservices.RetrofitConfig;
import app.flora.driver.webservices.models.Order;
import app.flora.driver.webservices.models.Product;
import app.flora.driver.webservices.responses.GetOrders;
import app.flora.driver.webservices.responses.GetProducts;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsFragment extends Fragment {
    public static FragmentActivity activity;
    public static ProductsFragment fragment;
    private View childView;
    private SessionManager sessionManager;
    private ArrayList<Product> productsList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private ProductsAdapter productsAdapter;

    @BindView(R.id.fragment_order_products_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_products_rv_products)
    RecyclerView products;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static ProductsFragment newInstance(FragmentActivity activity, String orderId, Order.ShippingAddress shippingAddress) {
        fragment = new ProductsFragment();
        ProductsFragment.activity = activity;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ORDER_ID, orderId);
        bundle.putSerializable(Constants.SHIPPING_ADDRESS,shippingAddress);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childView = inflater.inflate(R.layout.fragment_order_products, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (activity == null) {
            activity = getActivity();
        }
        sessionManager = new SessionManager(activity);
        MainActivity.setupAppbar(Constants.PRODUCT, "#" + getArguments().getString(Constants.ORDER_ID));
        initProducts();
        getProducts();
    }

    private void initProducts() {
        layoutManager = new LinearLayoutManager(activity);
        productsAdapter = new ProductsAdapter(activity, productsList, new ProductsAdapter.OnItemClickListener() {
            @Override
            public void detailsClick(int position) {
                Navigator.loadFragment(activity, ProductDetailsFragment.newInstance(activity,getArguments().getString(Constants.ORDER_ID), productsList.get(position),(Order.ShippingAddress)getArguments().getSerializable(Constants.SHIPPING_ADDRESS)), R.id.activity_main_fl_mainAppContainer, true);
            }

        });
        products.setLayoutManager(layoutManager);
        products.setAdapter(productsAdapter);
    }

    private void getProducts() {
        if (!GlobalFunctions.isNetworkConnected(activity)) {
            Snackbar.make(loading, getString(R.string.noConnection), Snackbar.LENGTH_SHORT).show();
        } else {
            productsApi(getArguments().getString(Constants.ORDER_ID));
        }
    }

    private void productsApi(String orderId) {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().GET_ORDER_ITEMS(sessionManager.getUserLanguage(), Constants.AUTHORIZATION_VALUE, orderId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int statusCode = response.code();
                        if (statusCode == 200) {
                            handleResponse(response);
                            if (productsList.size() > 0) {
                                productsAdapter.notifyDataSetChanged();
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

    private ArrayList<Product> handleResponse(Response<ResponseBody> response) {
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
            Type type = new TypeToken<GetProducts>() {
            }.getType();
            JsonReader reader = new JsonReader(new StringReader(outResponse));
            reader.setLenient(true);
            GetProducts products = new Gson().fromJson(jsonResponse, type);
            productsList.clear();
            productsList.addAll(products.getProducts());

        }
        return productsList;
    }
}
