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
public class FragmentThree extends Fragment implements View.OnClickListener {

    private RadioButton btn_switch;

    private Boolean BT_SWITCH = false;//开关默认关闭
    private static final String OnOff = "OnOff";
    private static final String eyesightOnOff = "EyesightOnOff"; // 视力开关
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_three, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
        sharedPreferences = getActivity().getSharedPreferences(OnOff, getActivity().MODE_PRIVATE);
        editor = sharedPreferences.edit();
        BT_SWITCH = sharedPreferences.getBoolean(eyesightOnOff, false); // 获取开关的状态
        btn_switch.setChecked(BT_SWITCH);
    }

    private void init() {
        btn_switch = (RadioButton) getView().findViewById(R.id.btn_switch);
        btn_switch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_switch:
                btn_switch.setChecked(BT_SWITCH ? false : true);
                BT_SWITCH = BT_SWITCH ? false : true;
                editor.putBoolean(eyesightOnOff, BT_SWITCH);
                editor.commit();
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
