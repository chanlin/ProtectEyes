package com.huayinghealth.protecteyes.fragment;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
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

    private Boolean BT_SWITCH = false;//开关默认关闭
    private RadioButton rb_LightProtect;

    private static final String OnOff = "OnOff";
    private static final String brightness_mode_switch  = "BRIGHTNESS_MODE_Switch"; // 光线开关


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();

        BT_SWITCH = SystemShare.getSettingBoolean(getActivity(),brightness_mode_switch, false); // 获取开关的状态
        rb_LightProtect.setChecked(BT_SWITCH);
    }

    private void init() {
        rb_LightProtect = (RadioButton) getView().findViewById(R.id.switch_LightProtect);
        rb_LightProtect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switch_LightProtect:
                rb_LightProtect.setChecked(BT_SWITCH ? false : true);
                BT_SWITCH = BT_SWITCH ? false : true;
                SystemShare.setSettingBoolean(getActivity(),brightness_mode_switch, BT_SWITCH);
                // 疲劳提醒开关指令
                if (BT_SWITCH) {
                    Toast.makeText(getActivity(), "打开", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "关闭", Toast.LENGTH_SHORT).show();
                }
                Settings.System.putInt(getActivity().getContentResolver(), SCREEN_BRIGHTNESS_MODE,
                    BT_SWITCH ? SCREEN_BRIGHTNESS_MODE_AUTOMATIC : SCREEN_BRIGHTNESS_MODE_MANUAL);
                break;
            default:
                break;
        }
    }
}
