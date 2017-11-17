package com.huayinghealth.protecteyes.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
        ArrayList list = new ArrayList();
        list.add();
    }
}
