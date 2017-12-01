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

    private Boolean BT_SWITCH = false;//开关默认关闭
    private RadioButton rb_LightProtect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        registerReceiver();
        init();

        BT_SWITCH = SystemShare.getSettingBoolean(getActivity(), SystemShare.BRIGHTNESS_MODE_SWITCH, false); // 获取开关的状态
        rb_LightProtect.setChecked(BT_SWITCH);

    }

    private void init() {
        rb_LightProtect = (RadioButton) getView().findViewById(R.id.switch_LightProtect);
        rb_LightProtect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_LightProtect:
                rb_LightProtect.setChecked(BT_SWITCH ? false : true);
                BT_SWITCH = BT_SWITCH ? false : true;
                SystemShare.setSettingBoolean(getActivity(), SystemShare.BRIGHTNESS_MODE_SWITCH, BT_SWITCH);
                // 光线感应开关指令
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

//    private void registerReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(SystemShare.UPDATE_BLUELIGHT);
//        getActivity().registerReceiver(receiver, filter);
//    }
//
//    private final BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.e("UPDATE_BLUELIGHT", "UPDATE_BLUELIGHT");
//            String action = intent.getAction();
//            if (action.equals(SystemShare.UPDATE_BLUELIGHT)) {
//                BT_SWITCH = intent.getStringExtra(SystemShare.BULELIGHE_STATE).equals("1") ? true : false;
//                Log.e("UPDATE_BLUELIGHT", "BT_SWITCH = " + BT_SWITCH);
//                SystemShare.setSettingBoolean(getActivity(), SystemShare.BRIGHTNESS_MODE_SWITCH, BT_SWITCH);
//            }
//        }
//    };

    @Override
    public void onResume() {
        super.onResume();
        BT_SWITCH = SystemShare.getSettingBoolean(getActivity(), SystemShare.BRIGHTNESS_MODE_SWITCH, false);
        rb_LightProtect.setChecked(BT_SWITCH);
        Log.e("UPDATE_BLUELIGHT", "BT_SWITCH == " + BT_SWITCH);
    }
}
