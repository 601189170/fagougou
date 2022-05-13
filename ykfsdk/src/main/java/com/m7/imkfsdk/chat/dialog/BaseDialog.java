package com.m7.imkfsdk.chat.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.m7.imkfsdk.R;


/**
 * Created by Hao on 2017/11/29.
 */

public class BaseDialog extends Dialog implements DialogInterface.OnDismissListener {
    Activity activity;
    int endTime = 1 * 60;
    public boolean isTimerClost;
    Handler claseHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(isTimerClost) return;

            if(msg.what == 1){
                if(endTime <= 0){
                    claseHandler.removeCallbacks(thread);
                    if(!activity.isFinishing())
                    dismiss();
                }else{
                    if(!activity.isFinishing()) {
                        endTime--;
                        claseHandler.postDelayed(thread, 1000);
                    }
                }
            }
        }
    };

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {

            Log.i("close","dialog -- " + endTime);
                Message message = new Message();
                message.what = 1;
                if(claseHandler!=null)
                    claseHandler.sendMessage(message);
            }
    });


    public BaseDialog(@NonNull Context context, int theme) {
        super(context, theme);
        activity = (Activity) context;
    }


    public BaseDialog(@NonNull Context context, String str) {
        this(context, R.style.shareDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.msg_layout);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        setOnDismissListener(this);


        claseHandler.post(thread);
    }


    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            endTime = 1 * 60;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
        Log.i("close","setOnDismissListener -- " + endTime);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(claseHandler!=null)
            claseHandler.removeCallbacks(thread);
        Log.i("close","onDismiss -- " + endTime);
    }
}
