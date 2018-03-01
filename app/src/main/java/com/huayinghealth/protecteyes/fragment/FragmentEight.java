package com.huayinghealth.protecteyes.fragment;


import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huayinghealth.protecteyes.R;

import java.util.ArrayList;

/**
 * Created by ChanLin on 2017/11/15.
 */
public class FragmentEight extends Fragment implements View.OnClickListener {

    private ViewPager viewPager;
    private View img1, img2, img3, img4;
    private FragmentPagerAdapter adapter;
    private TextView tx_eight_title, tv_operate;
    private Button btn_player;

    private MediaPlayer mediaPlayer = null;
    private int play_position = 0;
    private int[] play_music = {R.raw.eye_exercises1, R.raw.eye_exercises2, R.raw.eye_exercises3, R.raw.eye_exercises4};

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
        btn_player = (Button) getActivity().findViewById(R.id.btn_Player);
        btn_player.setOnClickListener(this);
        btn_player.setText("播放");

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
        initdot(); // 小圆点
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            int lastPosition;
            @Override
            public void onPageSelected(int position) {
                play_position = position;
//                if (position < 1){
//                    position = list.size();
//                    viewPager.setCurrentItem(position, true);
//                } else if(position > list.size()){
//                    viewPager.setCurrentItem(1, false);
//                    position = 1;
//                }
                // 页面被选中
                // 设置当前页面选中
                lean.getChildAt(position).setBackgroundResource(R.mipmap.icon_smalldot_press);
                // 设置前一页不选中
                lean.getChildAt(lastPosition).setBackgroundResource(R.mipmap.icon_smalldot);
                // 替换位置
                lastPosition = position;

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

                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    btn_player.setText("播放");
                }
                Log.e("FragmentEight", "play_position1 = " + play_position);
//                mediaplay(play_position);
//                mediaPlayer = MediaPlayer.create(getActivity(), play_music[play_position]);
////                    mediaPlayer.setLooping(true);
//                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        Log.e("onResume", "play");
//                        Toast.makeText(getActivity(), "播放结束", Toast.LENGTH_LONG).show();
//                        btn_player.setText("播放");
//                    }
//                });
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

    private void mediaplay(int a){
        mediaPlayer = MediaPlayer.create(getActivity(), play_music[a]);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (play_position < 3){
                    play_position++;
                    viewPager.setCurrentItem(play_position);
                    mediaplay(play_position);
                    mediaPlayer.start();
                    btn_player.setText("暂停");
                } else {
                    btn_player.setText("播放");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_Player:
                if (mediaPlayer != null){
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        btn_player.setText("播放");
                    } else {
                        mediaplay(play_position);
                        mediaPlayer.start();
                        btn_player.setText("暂停");
                    }
                } else {
                    mediaplay(play_position);
                    mediaPlayer.start();
                    btn_player.setText("暂停");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        btn_player.setText("播放");
//        mediaPlayer = MediaPlayer.create(getActivity(), play_music[play_position]);
////        mediaPlayer.setLooping(true);
//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                Log.e("onResume", "play222");
//                Toast.makeText(getActivity(), "播放结束!!!!!!", Toast.LENGTH_LONG).show();
//                btn_player.setText("播放");
//            }
//        });
//        Log.e("onResume", "mediaPlayer = " + mediaPlayer + " play_position = " + play_position);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("FragmentEight", "onStop");
        if(mediaPlayer != null){
            mediaPlayer.stop();
            btn_player.setText("播放");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("FragmentEight", "onDestroy");
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("onResume", "mediaPlayer = " + mediaPlayer + " play_position = " + play_position + " hidden=" + hidden);
        if(mediaPlayer != null && mediaPlayer.isPlaying() && hidden){
            mediaPlayer.pause();
            btn_player.setText("播放");
        }
        if (!hidden){
//            mediaPlayer = MediaPlayer.create(getActivity(), play_music[play_position]);
////            mediaPlayer.setLooping(true);
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    Log.e("onResume", "play111");
//                    Toast.makeText(getActivity(), "播放结束!!!", Toast.LENGTH_LONG).show();
//                    btn_player.setText("播放");
//                }
//            });
            Log.e("FragmentEight", "play_position2 = " + play_position);
//            mediaplay(play_position);
        }
    }
}
