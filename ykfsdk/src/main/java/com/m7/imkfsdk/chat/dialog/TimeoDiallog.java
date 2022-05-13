package com.m7.imkfsdk.chat.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.m7.imkfsdk.R;


/**
 * Created by Hao on 2017/11/29.
 */

public class TimeoDiallog extends BaseDialog {

    Context context;

    String str="";
    Button btn_confirm;
    Button btn_cancle;
    TimeoDialogListener listener;
    public TimeoDiallog(@NonNull Context context, int theme) {
        super(context, theme);
    }


    public TimeoDiallog(@NonNull Context context, String str) {
        this(context, R.style.shareDialog);
        this.context = context;
        this.str = str;

    }
    public TimeoDiallog(@NonNull Context context, String str,TimeoDialogListener listener) {
        this(context, R.style.shareDialog);
        this.context = context;
        this.str = str;
        this.listener = listener;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_msg_layout);
        btn_confirm=findViewById(R.id.btn_confirm);
        btn_cancle=findViewById(R.id.btn_cancle);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listener!=null){
                    listener.Confirm();
                }
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listener!=null){
                    listener.Cancle();
                }
                cancel();
            }
        });


    }

    void setTimeoDialogListener(TimeoDialogListener listener){
        this.listener=listener;
    }



}
