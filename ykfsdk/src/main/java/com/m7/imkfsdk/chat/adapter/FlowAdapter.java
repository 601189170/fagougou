package com.m7.imkfsdk.chat.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.m7.imkfsdk.R;
import com.moor.imkf.IMChatManager;
import com.moor.imkf.model.entity.FlowBean;
import com.moor.imkf.model.entity.FromToMessage;
import java.util.List;

public class FlowAdapter extends RecyclerView.Adapter<FlowAdapter.FlowViewHolder> {
    private OnItemClickListener onItemClickListener;
    public List<FlowBean> data;
    private FromToMessage detail;

    public FlowAdapter(List<FlowBean> strings, FromToMessage detail, OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.data = strings;
        this.detail = detail;
    }

    @Override
    public FlowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.ykfsdk_layout_item, parent, false);
        return new FlowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FlowViewHolder holder, final int position) {
        final FlowBean flowBean = data.get(position);
        holder.tv_flowItem.setText(flowBean.getButton());

        if(flowBean.isChoose()){
            holder.iv_choose_flow.setVisibility(View.VISIBLE);
            holder.tv_flowItem.setBackgroundResource(R.drawable.ykfsdk_ykf_bg_flow_btn);
        }else{
            holder.iv_choose_flow.setVisibility(View.GONE);
            holder.tv_flowItem.setBackgroundResource(R.drawable.ykfsdk_bg_flow_item);
        }

        holder.itemView.setOnClickListener(v -> {
            if (IMChatManager.getInstance().isManual) return;
            if(detail.isFlowSelect) return;
            flowBean.setChoose(!flowBean.isChoose());
            notifyDataSetChanged();
            onItemClickListener.setOnButtonClickListener(position,flowBean.isChoose(),flowBean.getText());
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class FlowViewHolder extends RecyclerView.ViewHolder {
        TextView tv_flowItem;
        ImageView iv_choose_flow;
        public FlowViewHolder(View itemView) {
            super(itemView);
            tv_flowItem = itemView.findViewById(R.id.tv_flowItem);
            iv_choose_flow= itemView.findViewById(R.id.iv_choose_flow);
        }
    }

    public interface OnItemClickListener {
        void setOnButtonClickListener(int position , boolean is, String msg);
    }
}
