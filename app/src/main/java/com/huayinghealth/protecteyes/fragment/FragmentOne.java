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

import com.huayinghealth.protecteyes.R;
import com.huayinghealth.protecteyes.VisionProtectionService;

import java.util.List;

/**
 * Created by ChanLin on 2017/11/15.
 */
public class FragmentOne  extends Fragment {

    private Boolean BT_SWITCH = false;//开关默认关闭

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_one,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        registerReceiver(); // 注册广播
    }

    private void registerReceiver() {
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        getActivity().registerReceiver(receiver,filter);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_TIME_TICK)) {
                Log.e("BootBroadcastTeceiver", "接收广播 " + "Intent.ACTION_TIME_TICK");
                if (!isServiceExisted(context, "com.application.protecteyesight.VisionProtectionService") && BT_SWITCH) { // 每分钟监听一次是否启动服务
                    Intent vp_service = new Intent(context, VisionProtectionService.class);
                    vp_service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startService(vp_service);
                    Log.e("BootBroadcastTeceiver", "启动服务 " + "VisionProtectionService");
                }
            }
        }
    };

    public static boolean isServiceExisted(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);

        if (!(serviceList.size() > 0)) {
            Log.e("BootBroadcastTeceiver", "isServiceExisted " + "return false");
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;

            if (serviceName.getClassName().equals(className)) {
                Log.e("BootBroadcastTeceiver", "isServiceExisted " + "return true");
                return true;
            }
        }
        Log.e("BootBroadcastTeceiver", "isServiceExisted " + "return false");
        return false;
    }
}
