package com.huayinghealth.protecteyes;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Instrumentation;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huayinghealth.protecteyes.fragment.FragmentFour;
import com.huayinghealth.protecteyes.fragment.FragmentOne;
import com.huayinghealth.protecteyes.fragment.FragmentFive;
import com.huayinghealth.protecteyes.utils.SystemShare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//import com.android.systemui.R;
public class VisionProtectionService extends Service {

    private static final String TAG = "VisionProtectionService";

    Context mContext;
    private Timer timer;
    Dialog dialog = null;
    private int num1 = 2;
    private boolean mTimerIsRunning = false;
    private boolean dismiss_when_not_fit_status = false;
    private boolean dismiss_Acc_Fanzan_when_not_fit_status = false;
    private ImageView vp_animation = null;
    private TextView text_remind;
    private static SoundPool soundPoollight = null;
    private static HashMap<String, Integer> soundPoolMaplight;
    private AnimationDrawable animationDrawable;
    private int mPresentPlayId;
    private static String AUDIO_VISION_PROTECTION_OPEN = "AUDIO_VISION_PROTECTION_OPEN";
    private static String AUDIO_FANZAN_PROTECTION_OPEN = "AUDIO_FANZAN_PROTECTION_OPEN";
    private static String AUDIO_DOUDO_PROTECTION_OPEN = "AUDIO_DOUDO_PROTECTION_OPEN";
    private static final String AUDIO_VISION_PROTECTION_PATH = "/system/media/audio/notifications/audio_vision_protection.mp3";
    private static final String AUDIO_FANZAN_PROTECTION_PATH = "/system/media/audio/notifications/audio_fanzan_wainning.mp3";
    private static final String AUDIO_DOUDO_PROTECTION_PATH = "/system/media/audio/notifications/audio_doudo_wainning.mp3";

    private boolean Psensor_switch = SystemShare.PSENSOR_DEFAULT_STATUS;
    private boolean Reversal_switch = false;
    private boolean Doudo_switch = false;

    private int remind_title; // 提醒状态1、眼距提醒 2、反转提醒 3、抖动提醒

    float sensorProximityValue = 0f;
    float sensorProximityValueOld = 10.0f;
    float sensorProximityValueOld_default = 1f;
    int sensorAccFanzanValue = 0;
    int sensorAccFanzanValueOld = 1;
    int ps_orientation = 0;
    int ps_orientation_old = 1;
    int eye_protect_sound_select = 0;  // 1 : ps protect   2: fanzan proteck
    private boolean b = false;

    boolean isDirectionUp = false;
    int continueUpCount = 0;
    int continueUpFormerCount = 0;
    boolean lastStatus = false;
    float peakOfWave = 0;
    float valleyOfWave = 0;
    float gravityOld = 0;
    int peak_num = 0;
    int phone_in_doudo_status = 0;
    int phone_in_doudo_stop_counter = 0;
	private boolean psensor_idle_status = true;
	private boolean fanzan_idle_status = true;
	private boolean doudo_idle_status = true;
	int doudo_active_set_time = 0;
	int doudo_value_level = 0;

    public IBinder onBind(Intent intent) {

        return null;
    }

    public void onCreate() {

        mContext = getBaseContext();
        lightLoadingAudio();
        Log.d(TAG, "------on create VisionProtectionService--------" + "build_android_ver=" + Build.VERSION.SDK_INT);
		if(Build.VERSION.SDK_INT == 23){   //android6.0
            doudo_active_set_time = 8;
            doudo_value_level = 120;
            sensorProximityValueOld_default = 1f;
		}else{    //android7.0
            doudo_active_set_time = 5;
            doudo_value_level = 160;
            sensorProximityValueOld_default = 10.0f;
		}
        getState();
        registerReceiver();
    }

    private void getState() {
        Psensor_switch = SystemShare.getSettingBoolean(mContext, SystemShare.EyeProtectSwitch, SystemShare.PSENSOR_DEFAULT_STATUS);
        Reversal_switch = SystemShare.getSettingBoolean(mContext, SystemShare.ReversalSwitch, false);
        Doudo_switch = SystemShare.getSettingBoolean(mContext, SystemShare.ShakeRemindSwitch, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //soundPoollight.release() ;
        Log.e("onDestroy", "vp_animation = " + vp_animation);
        Log.e("onDestroy", "soundPoollight = " + soundPoollight);
        Log.e("onDestroy", "dialog = " + dialog);
        if (vp_animation != null) {
//            animationDrawable = (AnimationDrawable) vp_animation.getDrawable();
//            animationDrawable.stop();
        }
        if (soundPoollight != null) {
            soundPoollight.stop(mPresentPlayId);
            soundPoollight.release();
        }

        if (dialog != null) {
            dialog.dismiss();
        }
        mTimerIsRunning = false;
        num1 = 2;
        enable_ps(false);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        enable_ps(true);
        return Service.START_STICKY;
    }

    private SensorManager mManager;

    void enable_ps(boolean enable) {
        Log.e("wzb", "enable ps");
        mManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (enable) {
            mManager.registerListener(sensorEventListener, mManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
            mManager.registerListener(sensorEventListener, mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            Log.e("wzb", "2222");
        } else {
            mManager.unregisterListener(sensorEventListener);
            Log.e("wzb", "3333");
        }
    }

    public static List getLauncherPackageName(Context context) {
        List packageNames = new ArrayList();
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        if (packageNames == null || packageNames.size() == 0) {
            return null;
        } else {
            return packageNames;
        }
    }

    public boolean isLauncher(Context context) {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        String topPackageName = null;
        if (rti.size() != 0) {
            topPackageName = rti.get(0).topActivity.getPackageName();
        }
        Log.i("weibin", "topPackageName =" + topPackageName);
        if (topPackageName != null) {
            if ("com.android.launcher3".equals(topPackageName) || "com.hy.launcher3".equals(topPackageName)) {
                return true;
            }
        }
        return false;
    }

    Handler mHandler_doudo = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 9999:
                    Log.e("wzb", "--luwl_doudo--999 peak num=" + peak_num + " phone_in_doudo_status" + phone_in_doudo_status);
                    if ((peak_num >= doudo_active_set_time) && (phone_in_doudo_status == 0)) {
                        //Toast.makeText(mContext, "检测到颠簸!!!", Toast.LENGTH_SHORT).show();
                        remind_title = 3;
						doudo_idle_status = false;
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        handler.sendEmptyMessage(0);
                        lightplayAuio(AUDIO_DOUDO_PROTECTION_OPEN);
                        phone_in_doudo_status = 1;
                        Log.e(TAG, "----------" + "build_android_ver=" + Build.VERSION.SDK_INT);
                        //这里触发检测到抖动.可以发送一个广播,然后app接收到广播后做相应的界面提示
                    }
                    peak_num = 0;
                    break;
                default:
                    break;
            }
        }
    };

    private void parse_acc_data(float f) {
        if (gravityOld == 0) {
            gravityOld = f;
        } else {
            if (detectorPeak(f, gravityOld)) {
                Log.e("wzb", "peak f=" + gravityOld);
                phone_in_doudo_stop_counter = 0;
                peak_num++;
                if (peak_num == 1) {
                    mHandler_doudo.sendEmptyMessageDelayed(9999, 2000);
                }
                Log.e("wzb", "--luwl_doudo--peak_num=" + peak_num);

            } else {
                if (phone_in_doudo_status == 1) {
                    phone_in_doudo_stop_counter++;
                    Log.e("wzb", "peak phone_in_doudo_stop_counter=" + phone_in_doudo_stop_counter);
                    if (phone_in_doudo_stop_counter > 20) {
                        Log.e("wzb", "--luwl_doudo--peak dismiss dialog");
                        phone_in_doudo_stop_counter = 0;
                        phone_in_doudo_status = 0;
						doudo_idle_status = true;
                        dismiss_animationDrawable_dialog();
                    }
                }
            }
        }
        gravityOld = f;

    }

    public boolean detectorPeak(float newValue, float oldValue) {
        lastStatus = isDirectionUp;
        if (newValue >= oldValue) {
            isDirectionUp = true;
            continueUpCount++;
        } else {
            continueUpFormerCount = continueUpCount;
            continueUpCount = 0;
            isDirectionUp = false;
        }
        //Log.e("wzb", "--luwl_doudo--detectorPeak=" + newValue + " " + oldValue + " " + isDirectionUp + " " + lastStatus + " " + continueUpFormerCount + " " + continueUpCount);
        if (!isDirectionUp && lastStatus && (continueUpFormerCount >= 1 && oldValue >= doudo_value_level)) {
            peakOfWave = oldValue;
            return true;
        } else if (!lastStatus && isDirectionUp) {
            valleyOfWave = oldValue;
            return false;
        } else {
            return false;
        }
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            PowerManager powerManager = (PowerManager) getSystemService(Service.POWER_SERVICE);
            //Log.e("luwl", " --luwl_test- psensor=" + Psensor_switch + " reversal=" + Reversal_switch + " Doudo=" + Doudo_switch);


            if ((sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) && Psensor_switch ) {
                sensorProximityValue = sensorEvent.values[0];
                Log.e("luwl", " --luwl_test-11-sensorProximityValue=" + sensorProximityValue);
                if (powerManager.isScreenOn() && (getResources().getConfiguration().orientation ==
                        Configuration.ORIENTATION_LANDSCAPE)) {
                    Log.e("luwl", " --luwl_test-22-sensorProximityValue=" + sensorProximityValue + " old=" + sensorProximityValueOld);
                    if (sensorProximityValue != sensorProximityValueOld) {
                        if (sensorProximityValue == 0.0 && sensorAccFanzanValue == 0) {
                            dismiss_when_not_fit_status = false;
							psensor_idle_status = false;
                            eye_protect_sound_select = 1;
                            remind_title = 1;
                            Log.e("luwl", " --luwl_test-TYPE_PROXIMITY-dialogThread start mTimerIsRunning=!!!" + mTimerIsRunning);
                            dialogThread();
                        } else if (sensorProximityValue != 0){
						    psensor_idle_status = true;
                            dismiss_animationDrawable_dialog();
                            Log.e("luwl", " --luwl_test-TYPE_PROXIMITY-from close-dismiss_animationDrawable_dialog=");
                        }
                        sensorProximityValueOld = sensorProximityValue;
                    }

                } else {
                    if (dialog != null) {
                        if (dismiss_when_not_fit_status == false) {
                            dismiss_when_not_fit_status = true;
                            sensorProximityValueOld = sensorProximityValueOld_default;
							psensor_idle_status = true;
                            dismiss_animationDrawable_dialog();
                            Log.e("luwl", " --luwl_test-TYPE_PROXIMITY-from not fit-dismiss_animationDrawable_dialog");
                        }
                    }
                }
            }

            if ((sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) && (Reversal_switch || Doudo_switch)) {
                if (sensorEvent.values[2] < -4) {
                    sensorAccFanzanValue = 1;
                } else {
                    sensorAccFanzanValue = 0;
                }

                if (powerManager.isScreenOn()) {
                    Log.e("luwl", " --luwl_test--sensorAccFanzanValue=" + sensorAccFanzanValue + " old=" + sensorAccFanzanValueOld);
                    if (Reversal_switch) {
                        if (sensorAccFanzanValue != sensorAccFanzanValueOld) {
                            if (sensorAccFanzanValue == 1) {
                                dismiss_Acc_Fanzan_when_not_fit_status = false;
                                eye_protect_sound_select = 2;
                                remind_title = 2;
                                fanzan_idle_status = false;
                                Log.e("luwl", " --luwl_test-TYPE_ACCELEROMETER-dialogThread start mTimerIsRunning=!!!" + mTimerIsRunning);
                                dialogThread();
                            } else {
                                fanzan_idle_status = true;
                                dismiss_animationDrawable_dialog();
                                Log.e("luwl", " --luwl_test-TYPE_ACCELEROMETER from normal-dismiss_animationDrawable_dialog !!!");
                            }
                            sensorAccFanzanValueOld = sensorAccFanzanValue;
                        }
                    }
                    if (Doudo_switch) {
                        float xyz = sensorEvent.values[0] * sensorEvent.values[0]
                                + sensorEvent.values[1] * sensorEvent.values[1]
                                + sensorEvent.values[2] * sensorEvent.values[2];
                        Log.e("wzb", "peak xyz=" + xyz + " " + sensorEvent.values[0] + " " + sensorEvent.values[1] + " " + sensorEvent.values[2]);
                        parse_acc_data(xyz);
                    }
                } else {
                    if ((dialog != null) && Reversal_switch) {
                        if (dismiss_Acc_Fanzan_when_not_fit_status == false) {
                            dismiss_Acc_Fanzan_when_not_fit_status = true;
                            fanzan_idle_status = true;
                            dismiss_animationDrawable_dialog();
                            sensorAccFanzanValueOld = 0;
                            Log.e("luwl", " --luwl_test-TYPE_ACCELEROMETER from screen off-dismiss_animationDrawable_dialog !!!");
                        }
                    }
                }
            }

           // Log.e("luwl", " --luwl_idle- psensor=" + psensor_idle_status + " reversal=" + fanzan_idle_status + " Doudo=" + doudo_idle_status + " " + sensorProximityValue);
           if(psensor_idle_status == true && fanzan_idle_status == true && doudo_idle_status == true) {
               if(dialog != null){
                   Log.e("luwl", "luwl--dialog dismiss for all idle");
                   if(dialog.isShowing())
                       dialog.dismiss();
               }
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private void dismiss_animationDrawable_dialog() {

        Log.e("luwl", "--release sound close dualog mPresentPlayId " + mPresentPlayId + " vp_animation " + vp_animation + " dialog " + dialog + " soundPoollight " + soundPoollight);
        if (vp_animation != null) {
//            animationDrawable = (AnimationDrawable) vp_animation.getDrawable();
//            animationDrawable.stop();
        }
        if (soundPoollight != null) {
            soundPoollight.stop(mPresentPlayId);
        }
        if (dialog != null) {
            dialog.dismiss();
        }
        if (timer != null) {
            timer.cancel();
        }
        mTimerIsRunning = false;
        num1 = 2;
        sensorAccFanzanValueOld = 0;
        sensorProximityValueOld = sensorProximityValueOld_default;
    }

    private void dialogThread() {
        if (!mTimerIsRunning) {
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (num1 == 0) {
                        timer.cancel();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        handler.sendEmptyMessage(0);
                        if (eye_protect_sound_select == 1) {
                            lightplayAuio(AUDIO_VISION_PROTECTION_OPEN);
                        } else if (eye_protect_sound_select == 2) {
                            lightplayAuio(AUDIO_FANZAN_PROTECTION_OPEN);
                        } else {
                            lightplayAuio(AUDIO_VISION_PROTECTION_OPEN);
                        }
                    } else {
                        num1--;
                    }
                }
            };
            mTimerIsRunning = true;
            timer.schedule(timerTask, 0, 1000);
        }
    }

    private void CreateDialog() {
        Builder builder = new Builder(mContext);
//        dialog = builder.create();
        dialog = new Dialog(mContext, R.style.mydialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setType(
                (WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            CreateDialog();
            dialog.show();
            Window window = dialog.getWindow();
            window.setContentView(R.layout.vp_dialog);
//            dialog.getWindow().setLayout(840, 767);
            text_remind = (TextView) dialog.findViewById(R.id.text_remind);
            vp_animation = (ImageView) dialog.findViewById(R.id.vp_animation);
            if (remind_title == 1) {
//                vp_animation.setImageResource(R.drawable.vision_protection_ani);
//                animationDrawable = (AnimationDrawable) vp_animation.getDrawable();
//                animationDrawable.start();
//                window.setBackgroundDrawableResource(R.mipmap.icon_remind1);
                vp_animation.setImageResource(R.mipmap.icon_dialog1);
                text_remind.setText("平板与眼睛距离过近对眼睛不好！");
            } else if (remind_title == 2) {
//                window.setBackgroundDrawableResource(R.mipmap.icon_remind3);
                vp_animation.setImageResource(R.mipmap.icon_dialog3);
                text_remind.setText("躺着看对眼睛不好,请不要躺着看屏幕！");
            } else if (remind_title == 3) {
//                window.setBackgroundDrawableResource(R.mipmap.icon_remind4);
                vp_animation.setImageResource(R.mipmap.icon_dialog4);
//                vp_animation.setImageResource(R.mipmap.icon_remind4);
                text_remind.setText("在晃动平板时看屏幕对眼睛不好!");
            }
        }
    };


    private void sendKeyCode(final int keyCode) {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(keyCode);
                } catch (Exception e) {
                    Log.e("Exception ", e.toString());
                }
            }
        }.start();
    }

    private void lightLoadingAudio() {
        soundPoollight = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
        soundPoolMaplight = new HashMap<String, Integer>();
        soundPoolMaplight.put(AUDIO_VISION_PROTECTION_OPEN, soundPoollight.load(getBaseContext(), R.raw.audio_vision_protection, 2));
        soundPoolMaplight.put(AUDIO_FANZAN_PROTECTION_OPEN, soundPoollight.load(getBaseContext(), R.raw.audio_fanzan_wainning, 1));
        soundPoolMaplight.put(AUDIO_DOUDO_PROTECTION_OPEN, soundPoollight.load(getBaseContext(), R.raw.audio_doudo_wainning, 1));
    }

    private void lightplayAuio(String audioFile) {

        AudioManager mgr = (AudioManager) mContext
                .getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (streamVolumeCurrent - 500 > 0) {
            streamVolumeCurrent = streamVolumeCurrent - 500;
        }
        float volume = streamVolumeCurrent / streamVolumeMax;
        mPresentPlayId = soundPoollight.play(soundPoolMaplight.get(audioFile), 1.0f, 1.0f, 1, 0, 1.0f);//loop -1
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SystemShare.PSENSOR_INTENT_NAME);
        filter.addAction(SystemShare.REVERSAL_INTENT_NAME);
        filter.addAction(SystemShare.DOUDO_INTENT_NAME);
        registerReceiver(receiver, filter);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            mContext = context;
            if (action.equals(SystemShare.PSENSOR_INTENT_NAME)) {
                Psensor_switch = intent.getBooleanExtra(SystemShare.PSENSOR_INTENT_STATUS, SystemShare.PSENSOR_DEFAULT_STATUS);
            }
            if (action.equals(SystemShare.REVERSAL_INTENT_NAME)) {
                Reversal_switch = intent.getBooleanExtra(SystemShare.REVERSAL_INTENT_STATUS, false);
            }
            if (action.equals(SystemShare.DOUDO_INTENT_NAME)) {
                Doudo_switch = intent.getBooleanExtra(SystemShare.DOUDO_INTENT_STATUS, false);
            }
            Log.e("luwl", " --luwl_test-receiver psensor=" + Psensor_switch + " reversal=" + Reversal_switch + " Doudo=" + Doudo_switch);
        }
    };
}

