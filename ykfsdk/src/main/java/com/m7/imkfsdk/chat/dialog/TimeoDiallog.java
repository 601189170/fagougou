package com.m7.imkfsdk.chat.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.m7.imkfsdk.R;


/**
 * Created by Hao on 2017/11/29.
 */

public class TimeoDiallog extends BaseDialog {

    Context context;

    String str="";
    TextView btn_confirm;
    TextView btn_cancle;
    ImageView close;
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
        btn_confirm=findViewById(R.id.right_btn);
        btn_cancle=findViewById(R.id.left_btn);
        close=findViewById(R.id.close);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listener!=null){
                    listener.Confirm();
                }
                cancel();
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
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

    }

    void setTimeoDialogListener(TimeoDialogListener listener){
        this.listener=listener;
    }



}
