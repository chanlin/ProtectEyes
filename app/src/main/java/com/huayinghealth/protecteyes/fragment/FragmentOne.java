package com.huayinghealth.protecteyes.fragment;


import android.app.ActivityManager;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
public class FragmentOne  extends Fragment implements View.OnClickListener {

    public static Boolean BT_SWITCH = SystemShare.PSENSOR_DEFAULT_STATUS;//开关默认关闭
    private RadioButton rb_EyeProtect;
	Intent vp_service;
    private static final String OnOff = "OnOff";
    Context ont_content;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_one,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vp_service = new Intent(getActivity(), VisionProtectionService.class);
        vp_service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ont_content = getActivity().getApplicationContext();
        init();

        BT_SWITCH = SystemShare.getSettingBoolean(ont_content,SystemShare.EyeProtectSwitch, SystemShare.PSENSOR_DEFAULT_STATUS); // 获取开关的状态
        rb_EyeProtect.setChecked(BT_SWITCH);
    }

    private void init() {
        rb_EyeProtect = (RadioButton) getView().findViewById(R.id.switch_EyeProtect);
        rb_EyeProtect.setOnClickListener(this);
        registerReceiver(); // 注册广播
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switch_EyeProtect:
                rb_EyeProtect.setChecked(BT_SWITCH ? false : true);
                BT_SWITCH = BT_SWITCH ? false : true;
                SystemShare.setSettingBoolean(ont_content,SystemShare.EyeProtectSwitch, BT_SWITCH);
                // 眼距保护开关指令
                Intent intent = new Intent(SystemShare.PSENSOR_INTENT_NAME);
				intent.putExtra(SystemShare.PSENSOR_INTENT_STATUS, BT_SWITCH);
                getActivity().sendBroadcast(intent);
				if (BT_SWITCH) {
                    Log.e("sendbroadcast", "luwl_test-打开眼距保护");
                    Toast.makeText(getActivity(), "打开", Toast.LENGTH_SHORT).show();
                    if (!isServiceExisted(ont_content, "com.huayinghealth.protecteyes.VisionProtectionService") ) {
                        Intent vp_service = new Intent(ont_content, VisionProtectionService.class);
                        vp_service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ont_content.startService(vp_service);
                        Log.e("BootBroadcastTeceiver", "-BT_SWITCH-luwl_apk-启动服务 " + "start ACTION_TIME_TICK act VisionProtectionService");
                    }
                } else {
                    Log.e("sendbroadcast", "luwl_test 关闭眼距保护");
                    Toast.makeText(getActivity(), "关闭", Toast.LENGTH_SHORT).show();
                   
                }

                break;
            default:
                break;
        }
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
            if(action.equals(Intent.ACTION_TIME_TICK)) { // 监听ACTION_TIME_TICK  每分钟接收一次
                Log.e("BootBroadcastTeceiver", "luwl_apk-接收广播 " + "Intent.ACTION_TIME_TICK" + "BT_ps=" + BT_SWITCH
                        + " Fanzan=" + FragmentFour.BT_FANZAN_SWITCH + " doudo=" + FragmentFive.BT_DOUDO_SWITCH);
                if (!isServiceExisted(context, "com.huayinghealth.protecteyes.VisionProtectionService") && (BT_SWITCH || FragmentFour.BT_FANZAN_SWITCH || FragmentFive.BT_DOUDO_SWITCH)) { // 每分钟监听一次是否启动服务
                    Intent vp_service = new Intent(context, VisionProtectionService.class);
                    vp_service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startService(vp_service);
                    Log.e("BootBroadcastTeceiver", "luwl_apk-启动服务 " + "start ACTION_TIME_TICK act VisionProtectionService");
                }else {
                    if (!(BT_SWITCH || FragmentFour.BT_FANZAN_SWITCH || FragmentFive.BT_DOUDO_SWITCH) && isServiceExisted(context, "com.huayinghealth.protecteyes.VisionProtectionService")) {
                        Log.e("BootBroadcastTeceiver", "luwl_apk-关闭 " + "stop ACTION_TIME_TICK act VisionProtectionService");
                        Toast.makeText(getActivity(), "关闭", Toast.LENGTH_SHORT).show();
                        Intent vp_service = new Intent(getActivity(), VisionProtectionService.class);
                        vp_service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().stopService(vp_service);
                    }
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
    public void onDestroy() {
        super.onDestroy();
//        getActivity().unregisterReceiver(receiver);
    }
}
