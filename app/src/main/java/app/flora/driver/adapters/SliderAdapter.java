package app.flora.driver.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import app.flora.driver.R;
import app.flora.driver.classes.FixControl;
import app.flora.driver.classes.Navigator;
import app.flora.driver.fragments.GestureImageFragment;

public class SliderAdapter extends PagerAdapter {
    Context context;
    ArrayList<String> sliderList;

    public SliderAdapter(Context context, ArrayList<String> sliderList) {
        this.context = context;
        this.sliderList = sliderList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View childView = LayoutInflater.from(context).inflate(R.layout.item_slider, container, false);
        assert childView != null;
        ImageView sliderImage = (ImageView) childView.findViewById(R.id.item_slider_iv_sliderImg);

        int Width = FixControl.getImageWidth(context, R.drawable.product_details_noimg);
        int Height = FixControl.getImageHeight(context, R.drawable.product_details_noimg);
        sliderImage.getLayoutParams().height = Height;
            if (sliderList.get(position) != null
                    && !sliderList.get(position).matches("")) {
                Glide.with(context.getApplicationContext())
                        .load(sliderList.get(position))
                        .apply(new RequestOptions().centerCrop()
                                .placeholder(R.drawable.product_details_noimg))
                        .into(sliderImage);

                sliderImage.getLayoutParams().height = Height;
            }

        sliderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigator.loadFragment((FragmentActivity) context, GestureImageFragment.newInstance((FragmentActivity) context,sliderList, position), R.id.activity_main_fl_mainAppContainer, true);
            }
        });

        container.addView(childView, 0);
        return childView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return sliderList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public int getItemPosition(Object object) {
        // refresh all fragments when data set changed
        return PagerAdapter.POSITION_NONE;
    }

}

