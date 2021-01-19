package com.juttmy.chatapp;

import androidx.annotation.NonNull;

import com.b44t.messenger.DcLot;

import com.juttmy.chatapp.database.model.ThreadRecord;
import com.juttmy.chatapp.mms.GlideRequests;

import java.util.Locale;
import java.util.Set;

public interface BindableConversationListItem extends Unbindable {

  public void bind(@NonNull ThreadRecord thread,
                   int msgId,
                   @NonNull DcLot dcSummary,
                   @NonNull GlideRequests glideRequests, @NonNull Locale locale,
                   @NonNull Set<Long> selectedThreads, boolean batchMode);
}
