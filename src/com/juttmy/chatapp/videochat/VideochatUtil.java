package com.juttmy.chatapp.videochat;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import com.b44t.messenger.DcChat;
import com.b44t.messenger.DcMsg;

import com.juttmy.chatapp.R;
import com.juttmy.chatapp.connect.ApplicationDcContext;
import com.juttmy.chatapp.connect.DcHelper;
import com.juttmy.chatapp.util.IntentUtils;

public class VideochatUtil {

  public void invite(Activity activity, int chatId) {
    ApplicationDcContext dcContext = DcHelper.getContext(activity);
    DcChat dcChat = dcContext.getChat(chatId);

    new AlertDialog.Builder(activity)
            .setTitle(activity.getString(R.string.videochat_invite_user_to_videochat, dcChat.getName()))
            .setMessage(R.string.videochat_invite_user_hint)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.ok, (dialog, which) -> {
                int msgId = dcContext.sendVideochatInvitation(dcChat.getId());
                if (msgId != 0) {
                  join(activity, msgId);
                }
            })
            .show();
  }

  public void join(Activity activity, int msgId) {
    ApplicationDcContext dcContext = DcHelper.getContext(activity);
    DcMsg dcMsg = dcContext.getMsg(msgId);
    String videochatUrl = dcMsg.getVideochatUrl();
    IntentUtils.showBrowserIntent(activity, videochatUrl);
  }

}
