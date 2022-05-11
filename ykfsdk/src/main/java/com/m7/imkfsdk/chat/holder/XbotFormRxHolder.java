package com.m7.imkfsdk.chat.holder;

import android.view.View;
import android.widget.TextView;

import com.m7.imkfsdk.R;

/**
 * Created by longwei on 2017/12/11.
 */

public class XbotFormRxHolder extends BaseHolder{

    private TextView tv_formPrompt,tv_formName;

    public XbotFormRxHolder(int type) {
        super(type);
    }

    public BaseHolder initBaseHolder(View baseView, boolean isReceive) {
        super.initBaseHolder(baseView);

        //通过baseview找到对应组件
        tv_formPrompt = (TextView) baseView.findViewById(R.id.tv_formPrompt);
        tv_formName=(TextView) baseView.findViewById(R.id.tv_formName);
        if(isReceive) {
            type = 25;
        }
        return this;
    }

    public TextView getformPromptView() {
        if(tv_formPrompt == null) {
            tv_formPrompt = (TextView) getBaseView().findViewById(R.id.tv_formPrompt);
        }
        return tv_formPrompt;
    }
    public TextView getformNameView() {
        if(tv_formName == null) {
            tv_formName = (TextView) getBaseView().findViewById(R.id.tv_formName);
        }
        return tv_formName;
    }
}
