package com.huayinghealth.protecteyes;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.huayinghealth.protecteyes.dialog.RestRemindDialog;
import com.huayinghealth.protecteyes.utils.SystemShare;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/11/22.
 */
public class RestRemindService extends Service {

//    RestRemindDialog dialog;
    AlertDialog dialog;

    private int learntime = 0;
    private Button btn_back;


    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (dialog == null) {
                CreateDialog();
            } else if (dialog != null) {
                if (!dialog.isShowing()){
                    CreateDialog();
                }
            }
        }
    };

    Timer timer = new Timer(true);
    TimerTask task = new TimerTask() {
        public void run() {
//                timer.cancel();
            handler.sendEmptyMessage(0);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("RestRemindService", "onCreat");
        learntime = SystemShare.getSettingInt(getBaseContext(),SystemShare.LearnTime,45);

        Log.e("learntime=", learntime + "");
//        timer.schedule(task,longtime * 60 * 1000); //延时1000ms后执行，1000ms执行一次
        if (learntime != 0) {
            timer.schedule(task, learntime * 1000 * 60, learntime * 1000 * 60);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("RestRemindService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    private void CreateDialog() {
//        dialog = new RestRemindDialog(getBaseContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setCancelable(false);
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
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_restremine);
        dialog.getWindow().setLayout(840, 767);
        window.setBackgroundDrawableResource(R.mipmap.icon_remind2);
        btn_back = (Button) dialog.findViewById(R.id.btn_Back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("RestRemindService", "onDestroy");
        timer.cancel();
    }
}
