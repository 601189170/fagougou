package com.m7.imkfsdk.chat.chatrow;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.m7.imkfsdk.R;
import com.m7.imkfsdk.chat.ChatActivity;
import com.m7.imkfsdk.chat.holder.BaseHolder;
import com.m7.imkfsdk.chat.holder.SendCardInfoTxHolder;
import com.m7.imkfsdk.chat.holder.ViewHolderTag;
import com.m7.imkfsdk.utils.GlideUtil;
import com.moor.imkf.model.entity.FromToMessage;
import com.moor.imkf.model.entity.NewCardInfo;

import java.lang.reflect.Type;


/**
 * Description:点击发给坐席的卡片类型
 *
 * @Author R-D
 * @Modification Time:2019/12/20
 */
public class SendCardInfoTxChatRow extends BaseChatRow {

    public SendCardInfoTxChatRow(int type) {
        super(type);
    }

    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu, View targetView, FromToMessage detail) {
        return false;
    }

    @Override
    protected void buildChattingData(final Context context, BaseHolder baseHolder, final FromToMessage message, int position) {
        SendCardInfoTxHolder holder = (SendCardInfoTxHolder) baseHolder;
        if (message != null && message.newCardInfo != null) {
            Type token = new TypeToken<NewCardInfo>() {
            }.getType();
            final NewCardInfo newCardInfo = new Gson().fromJson(message.newCardInfo, token);
            holder.tv_child_title.setText(newCardInfo.getTitle());
            holder.tv_child_.setText(newCardInfo.getSub_title());

            String img = newCardInfo.getImg();
            GlideUtil.loadImage(context,img,8f,holder.iv_child_img);

            View.OnClickListener listener = ((ChatActivity) context).getChatAdapter().getOnClickListener();
            ViewHolderTag clickTag = ViewHolderTag.createTag(newCardInfo.getTarget(), ViewHolderTag.TagType.TAG_CLICK_NEW_CARD);
            holder.kf_chat_rich_lin.setTag(clickTag);
            holder.kf_chat_rich_lin.setOnClickListener(listener);
            getMsgStateResId(position, holder, message, listener);
        }
    }

    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ykfsdk_kf_chat_row_newcard_info_tx, null);
            SendCardInfoTxHolder holder = new SendCardInfoTxHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, false));
        }
        return convertView;
    }

    @Override
    public int getChatViewType() {
        return ChatRowType.SEND_ORDER_INFO_ROW_TRANSMIT.ordinal();
    }
}
