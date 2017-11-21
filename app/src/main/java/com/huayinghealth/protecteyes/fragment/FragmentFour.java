package com.huayinghealth.protecteyes.fragment;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.huayinghealth.protecteyes.R;

/**
 * Created by ChanLin on 2017/11/15.
 */
public class FragmentFour extends Fragment implements View.OnClickListener {

    private Boolean BT_SWITCH = false;//开关默认关闭
    private RadioButton rb_Reversal;

    private static final String OnOff = "OnOff";
    private static final String ReversalSwitch = "ReversalSwitch"; // 视力开关

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_four,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        sharedPreferences = getActivity().getSharedPreferences(OnOff, getActivity().MODE_PRIVATE);
        editor = sharedPreferences.edit();
        BT_SWITCH = sharedPreferences.getBoolean(ReversalSwitch, false); // 获取开关的状态
        rb_Reversal.setChecked(BT_SWITCH);
    }

    private void init() {
        rb_Reversal = (RadioButton) getView().findViewById(R.id.switch_Reversal);
        rb_Reversal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switch_Reversal:
                rb_Reversal.setChecked(BT_SWITCH ? false : true);
                BT_SWITCH = BT_SWITCH ? false : true;
                editor.putBoolean(ReversalSwitch, BT_SWITCH);
                editor.commit();
                // 反转提醒开关指令
                if (BT_SWITCH) {
                    Toast.makeText(getActivity(), "打开", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "关闭", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
