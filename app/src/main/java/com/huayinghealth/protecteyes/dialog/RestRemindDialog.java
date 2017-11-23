package com.huayinghealth.protecteyes.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

import com.huayinghealth.protecteyes.R;

/**
 * Created by Administrator on 2017/11/10.
 */
public class RestRemindDialog extends AlertDialog {

    Context mContext;

    public RestRemindDialog(Context context) {
        super(context, R.style.RestRemindDialog); // 自定义全屏style
        this.mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity= Gravity.CENTER;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }
}
