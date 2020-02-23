package app.flora.driver.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.duolingo.open.rtlviewpager.RtlViewPager;

import java.util.ArrayList;

import app.flora.driver.R;
import app.flora.driver.activities.MainActivity;
import app.flora.driver.adapters.GestureImageAdapter;
import app.flora.driver.classes.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GestureImageFragment extends Fragment {
    static FragmentActivity activity;
    static GestureImageFragment fragment;
    private View childView;

    @BindView(R.id.fragment_gesture_image_rvp_pager)
    RtlViewPager pager;
    public static GestureImageFragment newInstance(FragmentActivity activity, ArrayList<String> imagesList, int position) {
        fragment = new GestureImageFragment();
        GestureImageFragment.activity = activity;
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(Constants.IMAGES,imagesList);
        bundle.putInt(Constants.POSITION,position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childView = inflater.inflate(R.layout.fragment_gesture_image, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //hidden MainAppBar and Ads for making the image fullScreen
        MainActivity.appbarContainer.setVisibility(View.VISIBLE);
        setupPager();
    }
    private void setupPager(){
        pager.setAdapter(new GestureImageAdapter(activity, getArguments().getStringArrayList(Constants.IMAGES)));
        pager.setCurrentItem(getArguments().getInt(Constants.POSITION));
    }
}
