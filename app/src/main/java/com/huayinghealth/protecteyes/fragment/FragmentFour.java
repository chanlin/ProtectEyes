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
public class FragmentFour extends Fragment implements View.OnClickListener {

    public static Boolean BT_FANZAN_SWITCH = false;//开关默认关闭
    private RadioButton rb_Reversal;
    Context four_content;
    private static final String OnOff = "OnOff";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_four,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        four_content = getActivity().getApplicationContext();
        init();
        BT_FANZAN_SWITCH = SystemShare.getSettingBoolean(four_content,SystemShare.ReversalSwitch, false); // 获取开关的状态
        rb_Reversal.setChecked(BT_FANZAN_SWITCH);
    }

    private void init() {
        rb_Reversal = (RadioButton) getView().findViewById(R.id.switch_Reversal);
        rb_Reversal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        
        switch (v.getId()){
            case R.id.switch_Reversal:
				
                rb_Reversal.setChecked(BT_FANZAN_SWITCH ? false : true);
                BT_FANZAN_SWITCH = BT_FANZAN_SWITCH ? false : true;
                SystemShare.setSettingBoolean(four_content,SystemShare.ReversalSwitch, BT_FANZAN_SWITCH);
                
				Intent intent = new Intent(SystemShare.REVERSAL_INTENT_NAME);
				intent.putExtra(SystemShare.REVERSAL_INTENT_STATUS, BT_FANZAN_SWITCH);
                getActivity().sendBroadcast(intent);
                // 反转提醒开关指令
                if (BT_FANZAN_SWITCH) {
                    Log.e("sendbroadcast", "--luwl_apk--open fanzan func");
                    Toast.makeText(getActivity(), "打开", Toast.LENGTH_SHORT).show();
                    if (!FragmentOne.isServiceExisted(four_content, "com.huayinghealth.protecteyes.VisionProtectionService") ) {
                        Intent vp_service = new Intent(four_content, VisionProtectionService.class);
                        vp_service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        four_content.startService(vp_service);
                        Log.e("BootBroadcastTeceiver", "-BT_SWITCH-luwl_apk-启动服务 " + "start ACTION_TIME_TICK act VisionProtectionService");
                    }
                } else {
                    Log.e("sendbroadcast", "--luwl_apk--close fanzan func");
                    Toast.makeText(getActivity(), "关闭", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
