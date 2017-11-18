package com.huayinghealth.protecteyes;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Instrumentation;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//import com.android.systemui.R;
public class VisionProtectionService extends Service {

	private static final String TAG="VisionProtectionService";

	Context mContext;
    private Timer timer;
    AlertDialog dialog;
	private int num1 =2;
	private boolean mTimerIsRunning = false;
	private boolean dismiss_from_close = false;
	private boolean dismiss_when_not_fit_status = false;
	private ImageView vp_animation;
	private static SoundPool soundPoollight = null;
	private static HashMap<String, Integer> soundPoolMaplight;
	private AnimationDrawable animationDrawable;
	private int mPresentPlayId;
	private static String AUDIO_VISION_PROTECTION_OPEN="AUDIO_VISION_PROTECTION_OPEN";
	private static final String AUDIO_VISION_PROTECTION_PATH = "/system/media/audio/notifications/audio_vision_protection.mp3";
	//private static final String AUDIO_VISION_PROTECTION_PATH = "/system/media/audio/notifications/audio_vision_protection.wav";
	public IBinder onBind(Intent intent){

		return null;
	}

	public void onCreate(){

		mContext = getBaseContext();
		lightLoadingAudio();
		Log.d(TAG,"------on create VisionProtectionService--------");

	}

	 @Override
		 public void onDestroy() {
		  super.onDestroy();
		  //soundPoollight.release() ;
		 animationDrawable = (AnimationDrawable) vp_animation.getDrawable();
		 animationDrawable.stop();
		 soundPoollight.stop(mPresentPlayId);
		 dialog.dismiss();
		 mTimerIsRunning = false;
		 num1 =2;
		 dismiss_from_close = false;
		 enable_ps(false);
		 Log.e("onDestroy", "distory");
		 }
	public int onStartCommand(Intent intent, int flags, int startId){
		//wbin add
	final boolean visionProtectionEnabled = Settings.Secure.getInt(getContentResolver(),
                "vision protection", 0) != 0;
 		enable_ps(true);
	
		return Service.START_STICKY;
	}

	private SensorManager mManager;
	void enable_ps(boolean enable){
		Log.e("wzb","enable ps");
		mManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
		if(enable){
			mManager.registerListener(sensorEventListener, mManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
			Log.e("wzb","2222");
		}else{
			mManager.unregisterListener(sensorEventListener);
			Log.e("wzb","3333");
		}
	}
	
//	void rphone_open_PS()
//	{
//		Log.e("wzb"," rphone_open_PS");
//		int cmd[]={300,1};
//				MyLib ml= new MyLib();
//				ml.setGpioMode(cmd);
//
//	}
//	void rphone_close_PS()
//	{
//		Log.e("wzb"," rphone_close_PS");
//		int cmd[]={301,1};
//				MyLib ml= new MyLib();
//				ml.setGpioMode(cmd);
//	}
	float sensorProximityValue=0f;
	float sensorProximityValueOld=1f;
	int ps_orientation =0;
	int ps_orientation_old=1;
	
	public static List getLauncherPackageName(Context context) {
		List packageNames = new ArrayList();
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        if(packageNames == null || packageNames.size() == 0){
			return null;
        }else
		{
			return packageNames;
        }
    }
	
	public boolean isLauncher(Context context) {
		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		String topPackageName = rti.get(0).topActivity.getPackageName();
		//List launcherName = getLauncherPackageName(context);
		Log.i("weibin", "topPackageName =" + topPackageName);
		//Log.i("weibin", "launcherName =" + launcherName);
		//if (launcherName != null && launcherName.size() != 0) {
		//	for (int i = 0; i < launcherName.size(); i ++) {
				if ("com.android.launcher3".equals(topPackageName) || "com.hy.launcher3".equals(topPackageName)) {
					return true;
				}
		//	}
		//}
		return false;
	}
	private SensorEventListener sensorEventListener = new SensorEventListener() {
		   @Override
		   public void onSensorChanged(SensorEvent sensorEvent) {
		    
		       if(sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY){
		           sensorProximityValue = sensorEvent.values[0];
		       }
					//Log.e("luwl"," --luwl--sensorProximityValue 1="+sensorProximityValue);
			  PowerManager powerManager = (PowerManager) getSystemService(Service.POWER_SERVICE);
			  if (powerManager.isScreenOn()&&(getResources().getConfiguration().orientation ==
					Configuration.ORIENTATION_LANDSCAPE)&&!isLauncher(mContext))
			  {
				  if ((sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY)) {

						//Log.e("luwl"," --luwl--sensorProximityValue="+sensorProximityValue);					
						if(sensorProximityValue==0.0) {                          
							//Log.e("luwl"," --luwl-close-dialog="+dialog + " ps=" + sensorProximityValue);	
							if(dialog==null)
							{
								//Log.e("luwl", "-11-luwl--dialog is null dialogThread will be exce now");  
								//mTimerIsRunning = false;	
								dialogThread(); 
								dismiss_when_not_fit_status = false;
								dismiss_from_close = true;
							}
							if(dialog!=null)
							{
								if(dialog.isShowing())
								{
									//Log.e("luwl", "-close-luwl--dialog isShowing now"); 
									dismiss_from_close = true;
									return;
								}
								else
								{
									//Log.e("luwl", "-22-luwl--dialog is not null dialogThread will be exce now");  
									//mTimerIsRunning = false;
									dismiss_when_not_fit_status = false;
									dialogThread();
								}
								dismiss_from_close = true;
							}
						} else {  
							//Log.e("luwl"," --luwl-away-dialog="+dialog + " ps=" + sensorProximityValue);	
							try {  
								if (timer != null){  
									//Log.e("luwl", "--luwl--timer is not null,exce timer.cancel");
									timer.cancel();
								}
								
								if(dialog!=null)
								{
									if (dialog.isShowing())
									{
										Log.e("luwl", "-away-luwl--dialog isShowing now");
									}
									 //Log.e("luwl", "-away-luwl--cancel the vp_animation and dialog"); 
	
									dismiss_animationDrawable_dialog();
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block  
								e.printStackTrace();  
							}  
						}  
					} 
			  }
			  else
			  {
			  	//Log.e("luwl"," --luwl-condision is not fit -dialog="+dialog);	
				if(dialog!=null)
					{

						//Log.e("luwl","-condision-luwl--cancel the vp_animation and dialog dismiss_when_not_fit_status=" + dismiss_when_not_fit_status);	
						if(dismiss_when_not_fit_status == false){
							dismiss_when_not_fit_status = true;
							dismiss_from_close = true;
							dismiss_animationDrawable_dialog();
							if (timer != null){  
								Log.e("luwl", "--luwl--timer is not null,exce timer.cancel");
								timer.cancel();
							}
						}
						 
						
					}
			  }
				
				//add by luwl start
		       if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
				{
					ps_orientation =1;   // S P
				}
				else
				{
					ps_orientation =0;   // H P
				}
				//Log.e("wzb"," orientation ="+ps_orientation + " orientation_old =" + ps_orientation_old);
				if(ps_orientation != ps_orientation_old){
					if(ps_orientation == 0){
//						rphone_open_PS();
					}
					else{
//						rphone_close_PS();
					}
				}
				ps_orientation_old = ps_orientation;
			   sensorProximityValueOld=sensorProximityValue;
		   }

		   @Override
		   public void onAccuracyChanged(Sensor sensor, int i) {

		   }
		};

	private void dismiss_animationDrawable_dialog(){
		Log.e("luwl","--luwl--dismiss_animationDrawable_dialog dismiss_from_close" + dismiss_from_close);
		if(dismiss_from_close){
			Log.e("luwl","--luwl--dismiss_animationDrawable_dialog");
			animationDrawable = (AnimationDrawable) vp_animation.getDrawable();
			animationDrawable.stop();
			soundPoollight.stop(mPresentPlayId);
			dialog.dismiss();
			mTimerIsRunning = false;	
			num1 =2;
			dismiss_from_close = false;
		}
	}

   private void dialogThread() { 
	//Log.e(TAG, "--luwl--mTimerIsRunning "+mTimerIsRunning);
   if(!mTimerIsRunning)
   {
        timer = new Timer();
        //Log.e(TAG, "--luwl--dialogThread num "+num1);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {  
                if (num1 == 0) {  
					//Log.e(TAG, "-000-luwl--sendEmptyMessage --AUDIO_VISION_PROTECTION_OPEN");		
                    timer.cancel(); 			
					handler.sendEmptyMessage(0);
					lightplayAuio(AUDIO_VISION_PROTECTION_OPEN);
                } else {						
                    num1--; 
					Log.e(TAG, "--luwl--dialogThread num --"+num1);
                }  
            }  
        }; 	 
		Log.d(TAG, "dialogThread num reset"+num1);
		mTimerIsRunning = true;	
        timer.schedule(timerTask, 0, 1000); 				
   }		
}  
  
    private void CreateDialog()
    {
    	/** 
         * 系统默认的alertDialog 
         */  
        Builder builder = new Builder(mContext);
        dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setType(  
                (WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
					Log.e("onKyeDown", "KEYCODE_BACK");
					return true;
				} else {
					return false;
				}
			}
		});
		dialog.setCancelable(false);
    	
    }
    private Handler handler = new Handler() {
  
        @Override
        public void handleMessage(Message msg) {
            CreateDialog();  
            dialog.show(); 
			Window window = dialog.getWindow();
			window.setContentView(R.layout.vp_dialog);
			dialog.getWindow().setLayout(1260, 600);
			vp_animation = (ImageView) dialog.findViewById(R.id.vp_animation);
			vp_animation.setImageResource(R.drawable.vision_protection_ani);
			animationDrawable = (AnimationDrawable) vp_animation.getDrawable();
			animationDrawable.start();	
			//Log.e(TAG, "--luwl--CreateDialog animationDrawable.start");	
        }  
  
    }; 


	private void sendKeyCode(final int keyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(keyCode);
				} catch (Exception e) {
//					Log.e("Exception when sendPointerSync", e.toString());
					Log.e("sendPointerSync", e.toString());
				}
			}
		}.start();
	}

	private void lightLoadingAudio() {
		soundPoollight = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
		soundPoolMaplight = new HashMap<String, Integer>();
		soundPoolMaplight.put(AUDIO_VISION_PROTECTION_OPEN,soundPoollight.load(AUDIO_VISION_PROTECTION_PATH, 1));

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
		mPresentPlayId=soundPoollight.play(soundPoolMaplight.get(audioFile), 1.0f, 1.0f, 1, -1, 1.0f);
	}
}

