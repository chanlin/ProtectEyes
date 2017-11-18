package com.huayinghealth.protecteyes.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huayinghealth.protecteyes.R;

import java.util.ArrayList;

/**
 * Created by ChanLin on 2017/11/15.
 */
public class FragmentEight extends Fragment {

    private ViewPager viewPager;
    private View img1,img2;
    private FragmentPagerAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eight,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setView();
    }
    private void setView(){
        viewPager = (ViewPager)getActivity().findViewById(R.id.vp_eight_imgs);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        img1 = inflater.inflate(R.layout.eight_img1,null);
        img2 = inflater.inflate(R.layout.eight_img2,null);
        final ArrayList<View> list = new ArrayList();
        list.add(img1);
        list.add(img2);

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return  list.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(list.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(list.get(position));
                return  list.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
    }
}
