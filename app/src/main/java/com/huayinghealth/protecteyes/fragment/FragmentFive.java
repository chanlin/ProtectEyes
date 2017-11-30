package com.huayinghealth.protecteyes.fragment;


import android.app.ActivityManager;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.huayinghealth.protecteyes.R;
import com.huayinghealth.protecteyes.VisionProtectionService;
import com.huayinghealth.protecteyes.utils.SystemShare;

import java.util.List;

/**
 * Created by ChanLin on 2017/11/15.
 */
public class FragmentFive extends Fragment implements View.OnClickListener {

    public static Boolean BT_DOUDO_SWITCH = false;//开关默认关闭
    private RadioButton rb_ShakeRemind;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_five,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        BT_DOUDO_SWITCH = SystemShare.getSettingBoolean(getActivity().getApplicationContext(),SystemShare.ShakeRemindSwitch, false); // 获取开关的状态
        rb_ShakeRemind.setChecked(BT_DOUDO_SWITCH);
    }

    private void init() {
        rb_ShakeRemind = (RadioButton) getView().findViewById(R.id.switch_ShakeRemind);
        rb_ShakeRemind.setOnClickListener(this);
    }
    public static boolean isServiceExisted_5(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);

        if (!(serviceList.size() > 0)) {
//            Log.e("BootBroadcastTeceiver", "isServiceExisted " + "return false");
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;

            if (serviceName.getClassName().equals(className)) {
//                Log.e("BootBroadcastTeceiver", "isServiceExisted " + "return true");
                return true;
            }
        }
//        Log.e("BootBroadcastTeceiver", "isServiceExisted " + "return false");
        return false;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switch_ShakeRemind:
				
                rb_ShakeRemind.setChecked(BT_DOUDO_SWITCH ? false : true);
                BT_DOUDO_SWITCH = BT_DOUDO_SWITCH ? false : true;
                SystemShare.setSettingBoolean(getActivity().getApplicationContext(),SystemShare.ShakeRemindSwitch, BT_DOUDO_SWITCH);

                Intent intent = new Intent(SystemShare.DOUDO_INTENT_NAME);
				intent.putExtra(SystemShare.DOUDO_INTENT_STATUS, BT_DOUDO_SWITCH);
                getActivity().sendBroadcast(intent);
                // 抖动提醒开关指令
                if (BT_DOUDO_SWITCH) {
                    Log.e("sendbroadcast", "--luwl_apk--open doudo func");
                    Toast.makeText(getActivity(), "打开", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("sendbroadcast", "--luwl_apk--close doudo func");
                    Toast.makeText(getActivity(), "关闭", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
