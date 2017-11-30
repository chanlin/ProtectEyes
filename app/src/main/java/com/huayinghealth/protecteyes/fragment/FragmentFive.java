package com.huayinghealth.protecteyes.fragment;


import android.app.Fragment;
import android.content.Intent;
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

/**
 * Created by ChanLin on 2017/11/15.
 */
public class FragmentFive extends Fragment implements View.OnClickListener {

    private Boolean BT_SWITCH = false;//开关默认关闭
    private RadioButton rb_ShakeRemind;

    private static final String OnOff = "OnOff";
    private static final String ShakeRemindSwitch = "ShakeRemindSwitch"; // 视力开关

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_five,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        BT_SWITCH = SystemShare.getSettingBoolean(getActivity().getApplicationContext(), ShakeRemindSwitch, false); // 获取开关的状态
        rb_ShakeRemind.setChecked(BT_SWITCH);
    }

    private void init() {
        rb_ShakeRemind = (RadioButton) getView().findViewById(R.id.switch_ShakeRemind);
        rb_ShakeRemind.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switch_ShakeRemind:
                rb_ShakeRemind.setChecked(BT_SWITCH ? false : true);
                BT_SWITCH = BT_SWITCH ? false : true;
                SystemShare.setSettingBoolean(getActivity().getApplicationContext(),ShakeRemindSwitch, BT_SWITCH);
                // 抖动提醒开关指令
                if (BT_SWITCH) {
//                    Toast.makeText(getActivity(), "打开", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getActivity(), "关闭", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
