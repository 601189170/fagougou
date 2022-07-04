package com.fagougou.government.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JS {
    public static JS js;

    public Context context;

    Pattern pattern;
    Matcher matcher;
    ClipboardManager cm;
    ClipData clipData;
    public String data;
    public JS(Context context) {
        this.context = context;
        cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }
    /**
     * 单例
     */
    public static JS getInstance(Context context){
        if (null == js){
            synchronized (JS.class){
                if (null == js){
                    js = new JS(context);
                }
            }
        }
        return js;
    }
    @JavascriptInterface
    @SuppressWarnings("unused")
    public void showSource(String html){

        data=html;
        getMesg(html);
    }
    public void getMesg(String html){

        String patterns = "<pre style=\"word-wrap: break-word; white-space: pre-wrap;\">(.*?)</pre>";
        pattern = Pattern.compile(patterns);
        matcher = pattern.matcher(html);
        if (matcher.find()){
            clipData = ClipData.newPlainText("Label",matcher.group(1));
            cm.setPrimaryClip(clipData);
        }
    }

}