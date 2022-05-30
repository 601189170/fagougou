package com.fagougou.government.consult;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.m7.imkfsdk.R;
import com.m7.imkfsdk.chat.dialog.BaseDialog;

/**
 * Created by Hao on 2017/11/29.
 */

public class WechatDiallog extends BaseDialog {

    Context context;
    String str="";
    ImageView close;
    ImageView code;
    Bitmap bitmap;

    public WechatDiallog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    public WechatDiallog(@NonNull Context context, Bitmap bitmap) {
        this(context, R.style.shareDialog);
        this.context = context;
        this.bitmap=bitmap;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.fagougou.government.R.layout.wechat_layout);

        close=findViewById(R.id.close);
        code=findViewById(com.fagougou.government.R.id.code);

        code.setImageBitmap(bitmap);
        close.setOnClickListener(view -> cancel());

    }

}
