package com.huayinghealth.protecteyes.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Toast;
//import android.os.SystemProperties;
import com.huayinghealth.protecteyes.R;
import com.huayinghealth.protecteyes.utils.SystemShare;

/**
 * Created by ChanLin on 2017/11/15.
 */
public class FragmentSix extends Fragment implements View.OnClickListener{

    private RadioButton btn_switch;
    private Boolean BT_SWITCH = false;//开关默认关闭
    private static final String OnOff = "OnOff";
    private int progress_value = 50;


    private SeekBar seekBar;
    public FragmentSix(){}
    /*public FragmentSix (boolean enable){
        this.cb_enable = enable;
    }*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_six,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init(){
        btn_switch = (RadioButton) getView().findViewById(R.id.btn_switch);
        btn_switch.setOnClickListener(this);
        BT_SWITCH = SystemShare.getSettingBoolean(getActivity(),SystemShare.colorOnOff);
        progress_value = SystemShare.getSettingInt(getActivity(),SystemShare.colorValue);
        btn_switch.setChecked(BT_SWITCH);


        seekBar = (SeekBar) getActivity().findViewById(R.id.sb_colortemperature);
        seekBar.setProgress(progress_value);
        seekBar.setEnabled(BT_SWITCH);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                //Toast.makeText(getActivity(),"当前进度："+i,Toast.LENGTH_LONG).show();
               // SystemProperties.set("persist.sys.custom_bl_level",""+progress);
                if(BT_SWITCH){
                    Log.e("bluelight",String.valueOf(progress));
                    //Intent intent = new Intent("com.android.custom.update_bluelight");
                    Intent intent = new Intent("com.huaying.protecteyes.update_bluelight");
                    intent.putExtra("protect.eyes.update_bluelight_state","1");

                    intent.putExtra("protect.eyes.update_bluelight_progress",progress);
                    //intent.getIntExtra("protect.eyes.update_bluelight_progress",progress);
                    getActivity().sendBroadcast(intent);
                    SystemShare.setSettingInt(getActivity(),SystemShare.colorValue,progress);
                }
              /*  else if(action.equals("com.huaying.protecteyes.update_bluelight")){
                    String b_state = intent.getStringExtra("protect.eyes.update_bluelight_state");

                    Intent bluelight_service = new Intent(mContext,BlueLightFilterService.class);
                    if("1".equals(b_state)){
                        int progress = intent.getIntExtra("protect.eyes.update_bluelight_progress",50);
                        bluelight_service.putExtra("level",progress);
                        mContext.startService(bluelight_service);
                    }else{
                        mContext.stopService(bluelight_service);
                    }
                }*/
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
       /* if("1".equals(SystemProperties.get("persist.sys.custom_bl_state","0"))){
            cb_enable.setChecked(true);
        }else{
            cb_enable.setChecked(false);
        }

        int level=SystemProperties.getInt("persist.sys.custom_bl_level",50);
        seekBar.setProgress(level);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_switch:
                btn_switch.setChecked(BT_SWITCH ? false : true);
                BT_SWITCH = BT_SWITCH ? false : true;
                SystemShare.setSettingBoolean(getActivity(),SystemShare.colorOnOff,BT_SWITCH);
                Intent intent = new Intent("com.huaying.protecteyes.update_bluelight");
                if(BT_SWITCH){
                    progress_value = SystemShare.getSettingInt(getActivity(),SystemShare.colorValue);
                    intent.putExtra("protect.eyes.update_bluelight_state","1");
                    intent.putExtra("protect.eyes.update_bluelight_progress",progress_value);


                   // SystemProperties.set("persist.sys.custom_bl_state","1");
                    //sendBroadcast(new Intent("com.android.custom.update_bluelight"));
                }else{
                    //SystemProperties.set("persist.sys.custom_bl_state","0");
                    intent.putExtra("protect.eyes.update_bluelight_state","0");

                    //sendBroadcast(new Intent("com.android.custom.update_bluelight"));
                }
                seekBar.setEnabled(BT_SWITCH);
                getActivity().sendBroadcast(intent);
                break;
            default:
                break;
        }
    }
}
