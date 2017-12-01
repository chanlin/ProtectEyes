package com.huayinghealth.protecteyes.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huayinghealth.protecteyes.R;
import com.huayinghealth.protecteyes.RestRemindService;
import com.huayinghealth.protecteyes.utils.SystemShare;

/**
 * Created by ChanLin on 2017/11/15.
 */
public class FragmentThree extends Fragment implements View.OnClickListener {

    private Boolean BT_SWITCH = false;//开关默认关闭
    private RadioButton rb_Resttime;
    private SeekBar seekBar_long; // 使用时间调节
    private TextView tv_learntime;

    private static final String LearnTime = "LearnTime"; // 学习时长

    private int learntime = 45;

    Intent vp_service;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_three, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vp_service = new Intent(getActivity(), RestRemindService.class);
        vp_service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        init();
        BT_SWITCH = SystemShare.getSettingBoolean(getActivity().getApplicationContext(),SystemShare.ResttimeSwitch, false);
        learntime = SystemShare.getSettingInt(getActivity().getApplicationContext(), SystemShare.LearnTime, 45);

        rb_Resttime.setChecked(BT_SWITCH);
        tv_learntime.setText(learntime + "");
        seekBar_long.setProgress(learntime);
//        seekBar_long.setEnabled(BT_SWITCH);
    }

    private void init() {
        rb_Resttime = (RadioButton) getView().findViewById(R.id.switch_Resttime);
        rb_Resttime.setOnClickListener(this);
        tv_learntime = (TextView) getView().findViewById(R.id.tv_learntime);

        seekBar_long = (SeekBar) getView().findViewById(R.id.seekBar_learn);
        seekBar_long.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { //在拖动中会调用此方法
                tv_learntime.setText(progress + "");
                learntime = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { //开始拖动时调用

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { // 停止拖动时调用
                // 保存到数据
                SystemShare.setSettingInt(getActivity().getApplicationContext(),SystemShare.LearnTime,learntime);
                if (BT_SWITCH){
                    getActivity().stopService(vp_service);
                    getActivity().startService(vp_service);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_Resttime:
                rb_Resttime.setChecked(BT_SWITCH ? false : true);
                BT_SWITCH = BT_SWITCH ? false : true;
                SystemShare.setSettingBoolean(getActivity().getApplicationContext(),SystemShare.ResttimeSwitch,BT_SWITCH);
                SystemShare.setSettingInt(getActivity().getApplicationContext(),SystemShare.LearnTime,learntime);
//                seekBar_long.setEnabled(BT_SWITCH);
                // 疲劳提醒开关指令
                if (BT_SWITCH) {
                    Toast.makeText(getActivity(), "打开", Toast.LENGTH_SHORT).show();
                    getActivity().startService(vp_service);
                } else {
                    Toast.makeText(getActivity(), "关闭", Toast.LENGTH_SHORT).show();
                    getActivity().stopService(vp_service);
                }
                break;
            default:
                break;
        }
    }
}
