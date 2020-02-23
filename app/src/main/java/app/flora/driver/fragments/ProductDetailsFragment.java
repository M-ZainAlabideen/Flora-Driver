package app.flora.driver.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.duolingo.open.rtlviewpager.RtlViewPager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import app.flora.driver.R;
import app.flora.driver.activities.MainActivity;
import app.flora.driver.adapters.SliderAdapter;
import app.flora.driver.classes.Constants;
import app.flora.driver.classes.FixControl;
import app.flora.driver.classes.GlobalFunctions;
import app.flora.driver.classes.Navigator;
import app.flora.driver.webservices.models.Order;
import app.flora.driver.webservices.models.Product;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class ProductDetailsFragment extends Fragment {
    public static FragmentActivity activity;
    public static ProductDetailsFragment fragment;
    private View childView;
    private Product product;
    private ArrayList<String> sliderList = new ArrayList<>();
    private int currentPage = 0;
    private int NUM_PAGES = 0;

    @BindView(R.id.fragment_product_details_iv_sliderPlaceholder)
    ImageView sliderPlaceholder;
    @BindView(R.id.fragment_product_details_vp_slider)
    RtlViewPager slider;
    @BindView(R.id.fragment_product_details_ci_sliderCircles)
    CircleIndicator sliderCircles;
    @BindView(R.id.fragment_product_details_tv_productName)
    TextView productName;
    @BindView(R.id.fragment_product_details_tv_productDescription)
    TextView productDescription;
    @BindView(R.id.fragment_product_details_tv_clientAddress)
    TextView clientAddress;
    @BindView(R.id.fragment_product_details_tv_vendorAddress)
    TextView vendorAddress;
    @BindView(R.id.fragment_product_details_tv_pieces)
    TextView pieces;
    @BindView(R.id.fragment_product_details_tv_totalPrice)
    TextView totalPrice;


    public static ProductDetailsFragment newInstance(FragmentActivity activity, String orderId, Product product, Order.ShippingAddress shippingAddress) {
        fragment = new ProductDetailsFragment();
        ProductDetailsFragment.activity = activity;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ORDER_ID, orderId);
        bundle.putSerializable(Constants.PRODUCT, product);
        bundle.putSerializable(Constants.SHIPPING_ADDRESS, shippingAddress);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childView = inflater.inflate(R.layout.fragment_product_details, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (activity == null) {
            activity = getActivity();
        }
        MainActivity.setupAppbar(Constants.PRODUCT, "#" + getArguments().getString(Constants.ORDER_ID));
        product = (Product) getArguments().getSerializable(Constants.PRODUCT);
        setData();
    }

    private void setData() {
        int Height = FixControl.getImageHeight(activity, R.drawable.product_details_noimg);
        sliderPlaceholder.getLayoutParams().height = Height;
        for (Product.Image item : product.productDetails.images) {
            sliderList.add(item.src);
        }
        setupSlider();
        productName.setText(product.productDetails.localizedNames.get(0).localizedName);
        if (product.productDetails.Descriptions.get(0).description == null ||
                product.productDetails.Descriptions.get(0).description.isEmpty()) {
            productDescription.setVisibility(View.GONE);
        } else {
            productDescription.setText(product.productDetails.Descriptions.get(0).description);
        }
        Order.ShippingAddress shippingAddress = (Order.ShippingAddress) getArguments().getSerializable(Constants.SHIPPING_ADDRESS);
        if (shippingAddress != null) {
            clientAddress.setText(setAddress(shippingAddress.getBlock(),
                    shippingAddress.getAddress1(),
                    shippingAddress.getProvince(),
                    shippingAddress.getCountry(),
                    shippingAddress.getAddress2()));
        } else {
            clientAddress.setText("");
        }
        if (product.productDetails.vendor != null && product.productDetails.vendor.vendorAddress != null) {
            vendorAddress.setText(setAddress(product.productDetails.vendor.vendorAddress.block,
                    product.productDetails.vendor.vendorAddress.address1,
                    product.productDetails.vendor.vendorAddress.province,
                    product.productDetails.vendor.vendorAddress.country,
                    product.productDetails.vendor.vendorAddress.address2));
        } else {
            vendorAddress.setText("");
        }
        pieces.setText(product.quantity + " " + getString(R.string.pieces));

        totalPrice.setText(product.productDetails.formattedPrice);
    }

    private String setAddress(String block, String street, String area, String country, String extraDetails) {
        String address = block + ", " + street + ", " + area + ", " + country + ", " + extraDetails;
        if (address != null)
            return address.replace("null, ", "").replace("null", "").replace(" ,null", "");
        else
            return "";
    }

    private void setupSlider() {
        SliderAdapter sliderAdapter = new SliderAdapter(activity, sliderList);
        slider.setAdapter(sliderAdapter);
        sliderCircles.setViewPager(slider);
        NUM_PAGES = sliderList.size();
        slider.setCurrentItem(0, true);
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                slider.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        sliderCircles.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

    @OnClick(R.id.fragment_product_details_tv_vendorAddress)
    public void vendorAddressClick() {
        String map = "http://maps.google.co.in/maps?q=" + vendorAddress.getText().toString();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(i);
    }

    @OnClick(R.id.fragment_product_details_tv_clientAddress)
    public void clientAddressClick() {
        String map = "http://maps.google.co.in/maps?q=" + clientAddress.getText().toString();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(i);
    }
}





