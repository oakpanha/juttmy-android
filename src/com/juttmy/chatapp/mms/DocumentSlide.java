package com.juttmy.chatapp.mms;


import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.b44t.messenger.DcMsg;

import com.juttmy.chatapp.attachments.DcAttachment;
import com.juttmy.chatapp.util.StorageUtil;

public class DocumentSlide extends Slide {

  public DocumentSlide(Context context, DcMsg dcMsg) {
    super(context, new DcAttachment(dcMsg));
    dcMsgId = dcMsg.getId();
  }

  public DocumentSlide(@NonNull Context context, @NonNull Uri uri,
                       @NonNull String contentType,  long size,
                       @Nullable String fileName)
  {
    super(context, constructAttachmentFromUri(context, uri, contentType, size, 0, 0, uri, StorageUtil.getCleanFileName(fileName), false));
  }

  @Override
  public boolean hasDocument() {
    return true;
  }

}
