package com.m7.imkfsdk.chat.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.m7.imkfsdk.R;
import com.m7.imkfsdk.view.PointBottomView;

/**
 * Created by longwei on 2016/3/9.
 */
public class TextViewHolder extends BaseHolder {

    private TextView tv_content;

    public RelativeLayout chat_rl_robot, chat_rl_robot_result;
    public RelativeLayout chat_ll_robot_useless, chat_ll_robot_useful;
    public LinearLayout lin_content;
    public LinearLayout ll_flow;


    public LinearLayout ll_flow_multi;
    public ImageView chat_iv_robot_useless, chat_iv_robot_useful;
    public TextView chat_tv_robot_useless;
    public TextView chat_tv_robot_useful;
    public TextView chat_tv_robot_result;



    public TextView tv_multi_save;
    public TextView tv_multi_count;
    public PointBottomView pointBottomView;
    public RecyclerView mRecyclerView;


    public TextViewHolder(int type) {
        super(type);
    }

    public BaseHolder initBaseHolder(View baseView, boolean isReceive) {
        super.initBaseHolder(baseView);

        if (isReceive) {
            type = 1;
            chat_rl_robot = (RelativeLayout) baseView.findViewById(R.id.chat_rl_robot);
            chat_rl_robot_result = (RelativeLayout) baseView.findViewById(R.id.chat_rl_robot_result);
            chat_ll_robot_useless = (RelativeLayout) baseView.findViewById(R.id.chat_ll_robot_useless);
            chat_ll_robot_useful = (RelativeLayout) baseView.findViewById(R.id.chat_ll_robot_useful);
            lin_content = (LinearLayout) baseView.findViewById(R.id.chart_content_lin);
            chat_iv_robot_useless = (ImageView) baseView.findViewById(R.id.chat_iv_robot_useless);
            chat_iv_robot_useful = (ImageView) baseView.findViewById(R.id.chat_iv_robot_useful);
            chat_tv_robot_useless = (TextView) baseView.findViewById(R.id.chat_tv_robot_useless);
            chat_tv_robot_useful = (TextView) baseView.findViewById(R.id.chat_tv_robot_useful);
            chat_tv_robot_result = (TextView) baseView.findViewById(R.id.chat_tv_robot_result);
            mRecyclerView = (RecyclerView) baseView.findViewById(R.id.recycler_view);
            pointBottomView = (PointBottomView) baseView.findViewById(R.id.point);//圆点指示器
            ll_flow = (LinearLayout) baseView.findViewById(R.id.ll_flow);//圆点指示器
            ll_flow_multi=(LinearLayout) baseView.findViewById(R.id.ll_flow_multi);//多选按钮 确定布局
            tv_multi_save=(TextView)baseView.findViewById(R.id.tv_multi_save);//多选按钮确定按钮
            tv_multi_count=(TextView)baseView.findViewById(R.id.tv_multi_count);//多选个数
            return this;
        } else {
            //通过baseview找到对应组件
            tv_content = (TextView) baseView.findViewById(R.id.chat_content_tv);
        }
        progressBar = (ProgressBar) baseView.findViewById(R.id.uploading_pb);
        type = 2;
        return this;
    }

    public TextView getDescTextView() {
        if (tv_content == null) {
            tv_content = (TextView) getBaseView().findViewById(R.id.chat_content_tv);
        }
        return tv_content;
    }

    public LinearLayout getDescLinearLayout() {
        if (lin_content == null) {
            lin_content = (LinearLayout) baseView.findViewById(R.id.chart_content_lin);
        }
        return lin_content;
    }

    public LinearLayout getFlowLinearLayout() {
        if (ll_flow == null) {
            ll_flow = (LinearLayout) baseView.findViewById(R.id.ll_flow);
        }
        return ll_flow;
    }

    public RecyclerView getmRecyclerView() {
        if (mRecyclerView == null) {
            mRecyclerView = (RecyclerView) getBaseView().findViewById(R.id.recycler_view);
        }
        return mRecyclerView;
    }

    public PointBottomView getPointBottomView() {
        if (pointBottomView == null) {
            pointBottomView = (PointBottomView) getBaseView().findViewById(R.id.point);
        }
        return pointBottomView;

    }


    public LinearLayout getLl_flow_multi() {
        return ll_flow_multi;
    }
    public TextView getTv_multi_save() {
        return tv_multi_save;
    }
    public TextView getTv_multi_count() {
        return tv_multi_count;
    }
}