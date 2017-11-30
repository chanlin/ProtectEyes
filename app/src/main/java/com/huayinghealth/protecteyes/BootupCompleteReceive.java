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
            Log.e("luwl"," --luwl_test-shutdownandboottimeReceiver psensor=" + Psensor_switch + " reversal=" + Reversal_switch + " Doudo=" + Doudo_switch
             + " ResttimeSwitch=" + ResttimeSwitch);
            if(Psensor_switch || Reversal_switch || Doudo_switch) {
                Intent vp_service = new Intent(mContext, VisionProtectionService.class);
                vp_service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startService(vp_service);
            }
            if(ResttimeSwitch){
                Intent rs_service = new Intent(mContext, RestRemindService.class);
                rs_service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startService(rs_service);
            }

        }
    }
}
