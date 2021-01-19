package com.juttmy.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.b44t.messenger.DcChat;
import com.b44t.messenger.DcMsg;

import com.juttmy.chatapp.mms.GlideRequests;
import com.juttmy.chatapp.recipients.Recipient;

import java.util.Locale;
import java.util.Set;

public interface BindableConversationItem extends Unbindable {
  void bind(@NonNull DcMsg                   messageRecord,
            @NonNull DcChat                  dcChat,
            @NonNull GlideRequests           glideRequests,
            @NonNull Locale                  locale,
            @NonNull Set<DcMsg>              batchSelected,
            @NonNull Recipient               recipients,
                     boolean                 pulseHighlight);

  DcMsg getMessageRecord();

  void setEventListener(@Nullable EventListener listener);

  interface EventListener {
    void onQuoteClicked(DcMsg messageRecord);
  }
}
