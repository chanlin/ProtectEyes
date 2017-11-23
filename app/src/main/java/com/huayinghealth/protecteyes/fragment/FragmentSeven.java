package com.huayinghealth.protecteyes.fragment;


import android.app.Fragment;
import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.huayinghealth.protecteyes.R;
import com.huayinghealth.protecteyes.utils.SystemShare;

/**
 * Created by ChanLin on 2017/11/15.
 */
public class FragmentSeven extends Fragment implements Preference.OnPreferenceChangeListener {


    private Boolean BT_SWITCH = false;//开关默认关闭
    //private RadioGroup rg_color;
    private RadioButton rb_blackWhiteSwitch;

    private static final String blackWhiteSwitch = "blackWhiteSwitch"; // 黑白开关

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seven,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BT_SWITCH = SystemShare.getSettingBoolean(getActivity(),blackWhiteSwitch); // 获取开关的状态

        init();
    }
    private void init(){

        rb_blackWhiteSwitch = (RadioButton) getActivity().findViewById(R.id.rb_blackWhiteSwitch);
         rb_blackWhiteSwitch.setChecked(BT_SWITCH);
        rb_blackWhiteSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_blackWhiteSwitch.setChecked(BT_SWITCH ? false : true);
                BT_SWITCH = BT_SWITCH ? false : true;
                SystemShare.setSettingBoolean(getActivity(),blackWhiteSwitch,BT_SWITCH);
                if (BT_SWITCH){
                    writeSimulateColorSpace(0);
                }else {
                    writeSimulateColorSpace(-1);
                }
            }
        });

       /* rg_color = (RadioGroup) getActivity().findViewById(R.id.rg_color);
        rg_color.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_black:
                        writeSimulateColorSpace(-1);
                        break;
                    case R.id.rb_white:
                        writeSimulateColorSpace(0);
                        break;
                    case R.id.rb_3:
                        writeSimulateColorSpace(2);
                        break;
                    case R.id.rb_4:
                        writeSimulateColorSpace(1);
                        break;
                    case R.id.rb_5:
                        writeSimulateColorSpace(3);
                        break;
                }
            }
        });*/

    }
    /*private void updateSimulateColorSpace() {
        final ContentResolver cr = getActivity().getContentResolver();
        final boolean enabled = Settings.Secure.getInt(
                cr, Settings.Secure.ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED, 0) != 0;
        if (enabled) {
            final String mode = Integer.toString(Settings.Secure.getInt(
                    cr, Settings.Secure.ACCESSIBILITY_DISPLAY_DALTONIZER,
                    AccessibilityManager.DALTONIZER_DISABLED));
            mSimulateColorSpace.setValue(mode);
            final int index = mSimulateColorSpace.findIndexOfValue(mode);
            if (index < 0) {
                // We're using a mode controlled by accessibility preferences.
                mSimulateColorSpace.setSummary(getString(R.string.daltonizer_type_overridden,
                        getString(R.string.accessibility_display_daltonizer_preference_title)));
            } else {
                mSimulateColorSpace.setSummary("%s");
            }
        } else {
            mSimulateColorSpace.setValue(
                    Integer.toString(AccessibilityManager.DALTONIZER_DISABLED));
        }
    }*/
    public static final String ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED =
            "accessibility_display_daltonizer_enabled";
    public static final String ACCESSIBILITY_DISPLAY_DALTONIZER =
            "accessibility_display_daltonizer";
    private void writeSimulateColorSpace(Object value) {
        final ContentResolver cr = getActivity().getContentResolver();
        final int newMode = Integer.parseInt(value.toString());
        if (newMode < 0) {
            //Settings.Secure.putInt(cr, Settings.Secure.ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED, 0);
            Settings.Secure.putInt(cr, ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED, 0);
        } else {
           // Settings.Secure.putInt(cr, Settings.Secure.ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED, 1);
           // Settings.Secure.putInt(cr, Settings.Secure.ACCESSIBILITY_DISPLAY_DALTONIZER, newMode);
            Settings.Secure.putInt(cr, ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED, 1);
            Settings.Secure.putInt(cr, ACCESSIBILITY_DISPLAY_DALTONIZER, newMode);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}
