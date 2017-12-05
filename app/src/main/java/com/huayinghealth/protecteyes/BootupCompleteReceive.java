package com.huayinghealth.protecteyes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.huayinghealth.protecteyes.utils.SystemShare;

/**
 * Created by Administrator on 2017/11/29.
 */

public class BootupCompleteReceive extends BroadcastReceiver {
    Context mContext;
    private boolean Psensor_switch = false;
    private boolean Reversal_switch = false;
    private boolean Doudo_switch = false;
    private boolean ResttimeSwitch = false;//休息时间开关
	private boolean ColorSwitch = false;
    private boolean AUTOBACKLIGHT_SWITCH = false; // 光线感应开关
    private int ColorValue = 50;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        mContext=context;
        Log.e("wzb","--luwl--shutdownandboottimeReceiver: aciton="+action);
        if(action.equals("android.intent.action.BOOT_COMPLETED")) {

            Psensor_switch = SystemShare.getSettingBoolean(mContext,SystemShare.EyeProtectSwitch,false);
            Reversal_switch = SystemShare.getSettingBoolean(mContext,SystemShare.ReversalSwitch,false);
            Doudo_switch = SystemShare.getSettingBoolean(mContext,SystemShare.ShakeRemindSwitch,false);
            ResttimeSwitch = SystemShare.getSettingBoolean(mContext,SystemShare.ResttimeSwitch,false);
            ColorSwitch = SystemShare.getSettingBoolean(mContext,SystemShare.colorOnOff,false);
            ColorValue = SystemShare.getSettingInt(mContext,SystemShare.colorValue,50);
            Log.e("luwl"," --luwl_test-shutdownandboottimeReceiver psensor=" + Psensor_switch + " reversal=" + Reversal_switch + " Doudo=" + Doudo_switch
             + " ResttimeSwitch=" + ResttimeSwitch);
            //if(Psensor_switch || Reversal_switch || Doudo_switch) {
                Intent vp_service = new Intent(mContext, VisionProtectionService.class);
                vp_service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startService(vp_service);
            //}
            if(ResttimeSwitch){
                Intent rs_service = new Intent(mContext, RestRemindService.class);
                rs_service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startService(rs_service);
            }
        }
		if(ColorSwitch){
                Intent intent_color = new Intent("com.huaying.protecteyes.update_bluelight");
                intent_color.putExtra("protect.eyes.update_bluelight_state","1");
                intent_color.putExtra("protect.eyes.update_bluelight_progress",ColorValue);
                mContext.sendBroadcast(intent_color);
        }

        /**
         * 接收光线感应开关状态
         * 20171205 by yangcl
         * */
        if (action.equals(SystemShare.UPDATE_AUTOBACKLIGHT)) {
            AUTOBACKLIGHT_SWITCH = intent.getStringExtra(SystemShare.SYSTEM_AUTOBACKLIGHT_STAUTS).equals("1") ? true : false;
            Log.e("AUTOBACKLIGHT_SWITCH", "AUTOBACKLIGHT_SWITCH = " + AUTOBACKLIGHT_SWITCH);
            SystemShare.setSettingBoolean(mContext, SystemShare.BRIGHTNESS_MODE_SWITCH, AUTOBACKLIGHT_SWITCH);
        }
    }
}
