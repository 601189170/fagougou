package com.m7.imkfsdk.chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.m7.imkfsdk.R;
import com.moor.imkf.YKFConstants;

public class KFBaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences spData = this.getSharedPreferences(YKFConstants.MOOR_SP, 0);
        int themeType = spData.getInt(YKFConstants.SYSTHEME, 0);
        if (themeType == 0) {
            setTheme(R.style.ykfsdk_KFSdkAppTheme);
        } else if (themeType == 1) {
            setTheme(R.style.ykfsdk_KFSdkAppTheme1);
        }
    }
}
