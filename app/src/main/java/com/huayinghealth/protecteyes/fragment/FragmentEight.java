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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huayinghealth.protecteyes.R;

import java.util.ArrayList;

/**
 * Created by ChanLin on 2017/11/15.
 */
public class FragmentEight extends Fragment {

    private ViewPager viewPager;
    private View img1, img2, img3, img4;
    private FragmentPagerAdapter adapter;
    private TextView tx_eight_title, tv_operate;

    final ArrayList<View> list = new ArrayList();
    private LinearLayout lean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eight, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setView();
    }

    private void setView() {
        tx_eight_title = (TextView) getActivity().findViewById(R.id.tx_eight_title);
        tv_operate = (TextView) getActivity().findViewById(R.id.tv_operate);

        viewPager = (ViewPager) getActivity().findViewById(R.id.vp_eight_imgs);
        lean = (LinearLayout) getActivity().findViewById(R.id.ll_dot);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        img1 = inflater.inflate(R.layout.eight_img1, null);
        img2 = inflater.inflate(R.layout.eight_img2, null);
        img3 = inflater.inflate(R.layout.eight_img3, null);
        img4 = inflater.inflate(R.layout.eight_img4, null);

        list.add(img1);
        list.add(img2);
        list.add(img3);
        list.add(img4);

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
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
                return list.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
        initdot();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int count = lean.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = lean.getChildAt(i);
//                    view.setEnabled(i == position ? false : true);
                    if(i == position){
                        view.setBackgroundResource(R.mipmap.icon_smalldot_press);
                    }else {
                        view.setBackgroundResource(R.mipmap.icon_smalldot);
                    }
                }
                switch (position) {
                    case 0:
                        tx_eight_title.setText("第一节  揉天应穴");
                        tv_operate.setText("        以左右大拇指罗纹面接左右眉头下面的上眶角处。其他四指散开弯曲如弓状，支在前额上，按探面不要大。");
                        break;
                    case 1:
                        tx_eight_title.setText("第二节  挤按晴明穴");
                        tv_operate.setText("        以左手或右手大拇指按鼻根部，先向下按、然后向上挤。");
                        break;
                    case 2:
                        tx_eight_title.setText("第三节  按揉四白穴");
                        tv_operate.setText("        先以左右食指与中指并拢，放在靠近鼻翼两侧，大拇指支撑在下腭骨凹陷处，然后放下中指，在面颊中央按揉。注意穴位不需移动，按揉面不要太大。");
                        break;
                    case 3:
                        tx_eight_title.setText("第四节  按太阳穴、轮刮眼眶");
                        tv_operate.setText("        拳起四指，以左右大拇指罗纹面按住太阳穴，以左右食指第二节内侧面轮刮眼眶上下一圈，上侧从眉头开始，到眉梢为止，下面从内眼角起至外眼角止，先上后下，轮刮上下一圈。");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initdot() {
        for (int i = 0; i < list.size(); i++) {
            View view = new View(getActivity());
            view.setBackgroundResource(R.mipmap.icon_smalldot);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);
            params.rightMargin = 10;
            lean.addView(view, params);
            view.setTag(i);
        }
        View view = lean.getChildAt(0);
//        view.setEnabled(false);
        view.setBackgroundResource(R.mipmap.icon_smalldot_press);
    }
}
