package app.flora.driver.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cuneytayyildiz.gestureimageview.GestureImageView;

import java.util.ArrayList;

import app.flora.driver.R;

public class GestureImageAdapter extends PagerAdapter {
    public final ArrayList<String> imagesList;
    public Context context;

    public GestureImageAdapter(Context context, ArrayList<String> imagesList) {
        this.context=context;
        this.imagesList = imagesList;
    }


    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = LayoutInflater.from(context).inflate(R.layout.item_gesture_image, view, false);
        assert imageLayout != null;
        final GestureImageView gestureImageItem = (GestureImageView) imageLayout.findViewById(R.id.gesture_image);
        if (imagesList.get(position) != null
                && !imagesList.get(position).matches("")
                && !imagesList.get(position).isEmpty()) {

            Glide.with(context).load(imagesList.get(position)).apply(new RequestOptions().placeholder(R.drawable.product_details_noimg)).into(gestureImageItem);
        }
        view.addView(imageLayout, 0);
        return imageLayout;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}

