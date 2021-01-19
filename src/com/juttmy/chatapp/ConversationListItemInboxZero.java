package com.juttmy.chatapp;


import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.b44t.messenger.DcLot;

import com.juttmy.chatapp.database.model.ThreadRecord;
import com.juttmy.chatapp.mms.GlideRequests;

import java.util.Locale;
import java.util.Set;

public class ConversationListItemInboxZero extends LinearLayout implements BindableConversationListItem{
  public ConversationListItemInboxZero(Context context) {
    super(context);
  }

  public ConversationListItemInboxZero(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public ConversationListItemInboxZero(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public ConversationListItemInboxZero(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  public void unbind() {

  }

  @Override
  public void bind(@NonNull ThreadRecord thread, int msgId, @NonNull DcLot dcSummary, @NonNull GlideRequests glideRequests, @NonNull Locale locale, @NonNull Set<Long> selectedThreads, boolean batchMode) {

  }
}
