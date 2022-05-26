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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.m7.imkfsdk.MessageConstans;
import com.m7.imkfsdk.R;
import com.m7.imkfsdk.chat.MessageEvent;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Hao on 2017/11/29.
 */

public class TaskTimeBaseDialog extends Dialog implements DialogInterface.OnDismissListener {
    Activity activity;
    int endTime = 1 * 30;
    public boolean isTimerClost;
    TextView content;
    String str="";
    TimeoDialogListener listener;
    TextView btn_confirm;
    TextView btn_cancle;
    ImageView close;
    Handler claseHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(isTimerClost) return;

            if(msg.what == 1){
                if(endTime <= 0){
                    EventBus.getDefault().post(new MessageEvent(MessageConstans.CloseToMain));
                    claseHandler.removeCallbacks(thread);
                    if(!activity.isFinishing())
                    dismiss();


                }else{
                    if(!activity.isFinishing()) {
                        endTime--;
                        content.setText(endTime+"s");
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


    public TaskTimeBaseDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }


    public TaskTimeBaseDialog(@NonNull Context context, String str, TimeoDialogListener listener) {
        this(context, R.style.shareDialog);
        activity = (Activity) context;
        this.str = str;
        this.listener = listener;

    }
    public TaskTimeBaseDialog(@NonNull Context context) {
        this(context, R.style.shareDialog);
        activity = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_task_layout);
        content=findViewById(R.id.content);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        setOnDismissListener(this);
        btn_confirm=findViewById(R.id.right_btn);
        btn_cancle=findViewById(R.id.left_btn);
        close=findViewById(R.id.close);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MessageEvent(MessageConstans.CloseToMain));
                cancel();
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EventBus.getDefault().post(new MessageEvent(MessageConstans.RefreshTime));
                cancel();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
//        claseHandler.post(thread);
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

    public void RefreshShow(){
        if (!isShowing()&& !activity.isFinishing()){
            endTime = 1 * 30;
            claseHandler.post(thread);
            show();
        }
    }
}
