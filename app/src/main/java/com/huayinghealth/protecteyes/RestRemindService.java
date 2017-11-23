package com.huayinghealth.protecteyes;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;

import com.huayinghealth.protecteyes.dialog.RestRemindDialog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/11/22.
 */
public class RestRemindService extends Service{

    Timer timer; // 计时器
    RestRemindDialog dialog;

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CreateDialog();
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

        timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                timer.cancel();
                handler.sendEmptyMessage(0);
            }
        };
//        timer.schedule(task,longtime * 60 * 1000); //延时1000ms后执行，1000ms执行一次
        timer.schedule(task, 5000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("RestRemindService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    private void CreateDialog() {
        dialog = new RestRemindDialog(getBaseContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
        dialog.setContentView(R.layout.dialog_restremine);
        dialog.getWindow().setLayout(1260, 600);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("RestRemindService", "onDestroy");
        timer.cancel();
    }
}
