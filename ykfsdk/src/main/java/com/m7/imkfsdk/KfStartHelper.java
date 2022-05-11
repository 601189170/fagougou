package com.m7.imkfsdk;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import com.m7.imkfsdk.chat.ChatActivity;
import com.m7.imkfsdk.chat.dialog.LoadingFragmentDialog;
import com.m7.imkfsdk.utils.faceutils.FaceConversionUtil;
import com.m7.imkfsdk.utils.ToastUtils;
import com.moor.imkf.IMChatManager;
import com.moor.imkf.YKFConstants;
import com.moor.imkf.listener.GetGlobleConfigListen;
import com.moor.imkf.listener.GetPeersListener;
import com.moor.imkf.listener.InitListener;
import com.moor.imkf.model.entity.CardInfo;
import com.moor.imkf.model.entity.Peer;
import com.moor.imkf.model.entity.ScheduleConfig;
import com.moor.imkf.utils.LogUtils;
import com.moor.imkf.utils.MoorUtils;
import com.moor.imkf.utils.YKFUtils;

import java.util.List;

/**
 * Created by pangw on 2018/7/9.
 */

public class KfStartHelper {
    private LoadingFragmentDialog loadingDialog;
    private Context context;
    private String receiverAction = "com.m7.imkf.KEFU_NEW_MSG";
    private String accessId;
    private String userName;
    private String userId;
    private SharedPreferences spData;
    private SharedPreferences.Editor edit;

    public static KfStartHelper getInstance() {
        return KfStartHelper.KfStartHelperClassHolder.instance;
    }

    private static class KfStartHelperClassHolder {
        private static final KfStartHelper instance = new KfStartHelper();
    }


    private void initHelper() {
        context = YKFUtils.getInstance().getApplication();
        loadingDialog = new LoadingFragmentDialog();
        spData = context.getSharedPreferences(YKFConstants.MOOR_SP, 0);
        edit = spData.edit();
        MoorUtils.init(YKFUtils.getInstance().getApplication());
    }

    public void initSdkChat(String accessId, String userName,
                            String userId) {

        initHelper();//赋值Helper需要的资源

        if (IMChatManager.getInstance().isIniting) {
            return;
        }

        this.accessId = accessId;
        this.userName = userName;
        this.userId = userId;

        if (!MoorUtils.isNetWorkConnected(context)) {
            Toast.makeText(context, R.string.ykfsdk_notnetwork, Toast.LENGTH_SHORT).show();
            return;
        }

        startKFService();//开始连接客服
    }

    private void startKFService() {

        IMChatManager.getInstance().setOnInitListener(new InitListener() {
            @Override
            public void oninitStart() {
                loadingDialog.show(YKFUtils.getInstance().getCurrentActivity().getFragmentManager(), "");
                initFaceUtils();//创建表情数据，不可移除
            }

            @Override
            public void oninitSuccess() {
                if (!YKFUtils.getInstance().getCurrentActivity().isFinishing()) {
                    getIsGoSchedule(YKFUtils.getInstance().getCurrentActivity(), YKFConstants.FROMMAIN);
                }
                LogUtils.d("MainActivity", "sdk初始化成功");
            }

            @Override
            public void onInitFailed(int code) {
                IMChatManager.getInstance().isIniting = false;
                ToastUtils.showShort(context, context.getString(R.string.ykfsdk_sdkinitwrong) + code);
                LogUtils.d("MainActivity", "sdk初始化失败:" + code);
                IMChatManager.getInstance().quitSDk();
                loadingDialog.dismiss();
            }
        });
        IMChatManager.getInstance().init(context, receiverAction, accessId, userName, userId);
    }


    /**
     * 修改会话页面 注销按钮 文案。建议两个 文字
     */
    public void setChatActivityLeftText(String left_Text) {
        if (spData == null) {
            return;
        }
        if (!TextUtils.isEmpty(left_Text)) {
            edit.putString(YKFConstants.CHATACTIVITYLEFTTEXT, left_Text).apply();
        }
        return;

    }

    /**
     * 是否需要 emoji表情 按钮。
     */
    public void setChatActivityEmoji(boolean show) {
        if (spData == null) {
            return;
        }
        edit.putBoolean(YKFConstants.CHATACTIVITYEMOJI, show).apply();
    }

    /**
     * 初始化表情
     */
    public void initFaceUtils() {
        if (FaceConversionUtil.getInstace().emojis == null || FaceConversionUtil.getInstace().emojis.size() == 0) {
            FaceConversionUtil.getInstace().getFileText(
                    context);
        }
    }


    /**
     * 校验跳转日程还是技能组
     *
     * @param resumeAct 来自哪一个Activity 的请求
     * @param from      YKFConstants.FROMMAIN 来自初始化页面的选择
     *                  YKFConstants.FROMCHAT 来自聊天页面的选择，取消时需要同时关闭聊天页面
     */
    public void getIsGoSchedule(final Activity resumeAct, final String from) {
        IMChatManager.getInstance().getWebchatScheduleConfig(new GetGlobleConfigListen() {
            @Override
            public void getSchedule(ScheduleConfig sc) {
                loadingDialog.dismiss();
                LogUtils.aTag("MainActivity", "日程");
                if (!sc.getScheduleId().equals("") &&
                        !sc.getProcessId().equals("") &&
                        sc.getEntranceNode() != null
                        && sc.getLeavemsgNodes() != null) {
                    if (sc.getEntranceNode().getEntrances().size() > 0) {
                        if (sc.getEntranceNode().getEntrances().size() == 1) {
                            ScheduleConfig.EntranceNodeBean.EntrancesBean bean = sc.getEntranceNode().getEntrances().get(0);
                            // TODO: 2019/12/24 修改为Builder方式
                            new ChatActivity.Builder()
                                    .setType(YKFConstants.TYPE_SCHEDULE)
                                    .setScheduleId(sc.getScheduleId())
                                    .setProcessId(sc.getProcessId())
                                    .setCurrentNodeId(bean.getProcessTo())
                                    .setProcessType(bean.getProcessType())
                                    .setEntranceId(bean.get_id())
                                    .setCardInfo(IMChatManager.getInstance().getCardInfo())
                                    .setNewCardInfo(IMChatManager.getInstance().getNewCardInfo())
                                    .build(context);
                        } else {
                            startScheduleDialog(resumeAct, from, sc.getEntranceNode().getEntrances(), sc.getScheduleId(), sc.getProcessId());
                        }

                    } else {
                        ToastUtils.showShort(context, R.string.ykfsdk_sorryconfigurationiswrong);
                    }
                } else {
                    ToastUtils.showShort(context, R.string.ykfsdk_sorryconfigurationiswrong);
                }
            }

            @Override
            public void getPeers() {
                LogUtils.aTag("start", "技能组");
                startChatActivityForPeer(resumeAct, from);
            }
        });
    }

    private void startScheduleDialog(final Activity resumeAct, final String from, final List<ScheduleConfig.EntranceNodeBean.EntrancesBean> entrances, final String scheduleId, final String processId) {
        final String[] items = new String[entrances.size()];
        for (int i = 0; i < entrances.size(); i++) {
            items[i] = entrances.get(i).getName();
        }

        AlertDialog dialog = new AlertDialog.Builder(resumeAct)
                .setTitle(context.getString(R.string.ykfsdk_ykf_select_scu))
                .setCancelable(false)
                .setNegativeButton(context.getString(R.string.ykfsdk_back), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IMChatManager.getInstance().quitSDk();
                        if (YKFConstants.FROMCHAT.equals(from)) {
                            //如果为聊天页面点击的则关闭页面，重新进线
                             if (resumeAct != null) {
                                resumeAct.finish();
                            }
                        }
                    }
                })
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ScheduleConfig.EntranceNodeBean.EntrancesBean bean = entrances.get(which);
                        LogUtils.aTag("选择日程：", bean.getName());
                        new ChatActivity.Builder()
                                .setType(YKFConstants.TYPE_SCHEDULE)
                                .setScheduleId(scheduleId)
                                .setProcessId(processId)
                                .setCurrentNodeId(bean.getProcessTo())
                                .setProcessType(bean.getProcessType())
                                .setEntranceId(bean.get_id())
                                .setCardInfo(IMChatManager.getInstance().getCardInfo())
                                .setNewCardInfo(IMChatManager.getInstance().getNewCardInfo())
                                .build(context);

                    }
                }).create();
        dialog.show();
    }

    /**
     * 跳转技能组ChatActivity
     */
    private void startChatActivityForPeer(final Activity resumeAct, final String from) {
        IMChatManager.getInstance().getPeers(new GetPeersListener() {
            @Override
            public void onSuccess(List<Peer> peers) {
                loadingDialog.dismiss();
                if (peers.size() > 1) {
                    startPeersDialog(resumeAct, from, peers, IMChatManager.getInstance().getCardInfo());
                } else if (peers.size() == 1) {
                    // TODO: 2019-12-24 修改为Builder方式
                    new ChatActivity.Builder()
                            .setType(YKFConstants.TYPE_PEER)
                            .setPeerId(peers.get(0).getId())
                            .setCardInfo(IMChatManager.getInstance().getCardInfo())
                            .setNewCardInfo(IMChatManager.getInstance().getNewCardInfo())
                            .build(context);
                } else {
                    ToastUtils.showShort(context, R.string.ykfsdk_peer_no_number);
                }

            }

            @Override
            public void onFailed() {
                loadingDialog.dismiss();
                ToastUtils.showShort(context, context.getString(R.string.ykfsdk_ykf_nopeer));
            }
        });
    }

    public void startPeersDialog(final Activity resumeAct, final String from, final List<Peer> peers, final CardInfo mCardInfo) {
        final String[] items = new String[peers.size()];
        for (int i = 0; i < peers.size(); i++) {
            items[i] = peers.get(i).getName();
        }
        AlertDialog builder = new AlertDialog.Builder(resumeAct)
                .setTitle(context.getString(R.string.ykfsdk_ykf_select_peer))
                .setCancelable(false)
                .setNegativeButton(context.getString(R.string.ykfsdk_back), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IMChatManager.getInstance().quitSDk();
                        if (YKFConstants.FROMCHAT.equals(from)) {
                            //如果为聊天页面点击的则关闭页面，重新进线
                            if (resumeAct != null) {
                                resumeAct.finish();
                            }
                        }
                    }
                })
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Peer peer = peers.get(which);
                        LogUtils.aTag("选择技能组：", peer.getName());
                        // TODO: 2019-12-24 修改为Builder方式
                        new ChatActivity.Builder()
                                .setType(YKFConstants.TYPE_PEER)
                                .setPeerId(peer.getId())
                                .setCardInfo(mCardInfo)
                                .setNewCardInfo(IMChatManager.getInstance().getNewCardInfo())
                                .build(context);
                    }
                }).create();
        builder.show();
    }


    /**
     * 设置 receiverAction 和AndroidManifest中注册的广播中的action一致
     */
    public void setReceiverAction(String receiverAction) {
        this.receiverAction = receiverAction;
    }

}
