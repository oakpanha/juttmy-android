// called when the user click the "clear" or "mark read" button in the system notification

package com.juttmy.chatapp.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.juttmy.chatapp.connect.ApplicationDcContext;
import com.juttmy.chatapp.connect.DcHelper;
import com.juttmy.chatapp.util.Util;

public class MarkReadReceiver extends BroadcastReceiver {
  public static final  String MARK_NOTICED_ACTION   = "com.juttmy.chatapp.notifications.MARK_NOTICED";
  public static final  String CANCEL_ACTION         = "com.juttmy.chatapp.notifications.CANCEL";
  public static final  String CHAT_ID_EXTRA         = "chat_id";

  @Override
  public void onReceive(final Context context, Intent intent) {
    boolean markNoticed = MARK_NOTICED_ACTION.equals(intent.getAction());
    if (!markNoticed && !CANCEL_ACTION.equals(intent.getAction())) {
      return;
    }

    final int chatId = intent.getIntExtra(CHAT_ID_EXTRA, 0);
    if (chatId==0) {
      return;
    }

    final ApplicationDcContext dcContext = DcHelper.getContext(context);

    Util.runOnAnyBackgroundThread(() -> {
      dcContext.notificationCenter.removeNotifications(chatId);
      if (markNoticed) {
        dcContext.marknoticedChat(chatId);
      }
    });
  }
}
