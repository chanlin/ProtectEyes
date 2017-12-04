package com.huayinghealth.protecteyes.fragment;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.huayinghealth.protecteyes.R;
import com.huayinghealth.protecteyes.utils.SystemShare;

import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;

/**
 * Created by ChanLin on 2017/11/15.
 */
public class FragmentTwo extends Fragment implements View.OnClickListener {

    private Boolean AUTOBACKLIGHT_SWITCH = false;//开关默认关闭
    private RadioButton rb_LightProtect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerReceiver();
        init();

        AUTOBACKLIGHT_SWITCH = SystemShare.getSettingBoolean(getActivity(), SystemShare.BRIGHTNESS_MODE_SWITCH, false); // 获取开关的状态
        rb_LightProtect.setChecked(AUTOBACKLIGHT_SWITCH);

    }

    private void init() {
        rb_LightProtect = (RadioButton) getView().findViewById(R.id.switch_LightProtect);
        rb_LightProtect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_LightProtect:
                rb_LightProtect.setChecked(AUTOBACKLIGHT_SWITCH ? false : true);
                AUTOBACKLIGHT_SWITCH = AUTOBACKLIGHT_SWITCH ? false : true;
                SystemShare.setSettingBoolean(getActivity(), SystemShare.BRIGHTNESS_MODE_SWITCH, AUTOBACKLIGHT_SWITCH);
                // 光线感应开关指令
                if (AUTOBACKLIGHT_SWITCH) {
                    Toast.makeText(getActivity(), "打开", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "关闭", Toast.LENGTH_SHORT).show();
                }
                Settings.System.putInt(getActivity().getContentResolver(), SCREEN_BRIGHTNESS_MODE,
                        AUTOBACKLIGHT_SWITCH ? SCREEN_BRIGHTNESS_MODE_AUTOMATIC : SCREEN_BRIGHTNESS_MODE_MANUAL);
                break;
            default:
                break;
        }
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SystemShare.UPDATE_AUTOBACKLIGHT);
        getActivity().registerReceiver(receiver, filter);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("UPDATE_AUTOBACKLIGHT", "UPDATE_AUTOBACKLIGHT");
            String action = intent.getAction();
             if (action.equals(SystemShare.UPDATE_AUTOBACKLIGHT)) {
                 AUTOBACKLIGHT_SWITCH = intent.getStringExtra(SystemShare.SYSTEM_AUTOBACKLIGHT_STAUTS).equals("1") ? true : false;
                 Log.e("UPDATE_BLUELIGHT", "AUTOBACKLIGHT_SWITCH = " + AUTOBACKLIGHT_SWITCH);
                 SystemShare.setSettingBoolean(getActivity(), SystemShare.BRIGHTNESS_MODE_SWITCH, AUTOBACKLIGHT_SWITCH);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        AUTOBACKLIGHT_SWITCH = SystemShare.getSettingBoolean(getActivity(), SystemShare.BRIGHTNESS_MODE_SWITCH, false);
        rb_LightProtect.setChecked(AUTOBACKLIGHT_SWITCH);
        Log.e("UPDATE_BLUELIGHT", "AUTOBACKLIGHT_SWITCH == " + AUTOBACKLIGHT_SWITCH);
    }
}
