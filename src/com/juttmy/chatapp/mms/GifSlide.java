package com.juttmy.chatapp.mms;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.Nullable;

import com.b44t.messenger.DcMsg;

import com.juttmy.chatapp.util.MediaUtil;

public class GifSlide extends ImageSlide {

  public GifSlide(Context context, DcMsg dcMsg) {
    super(context, dcMsg);
  }

  public GifSlide(Context context, Uri uri, long size, int width, int height) {
    super(context, constructAttachmentFromUri(context, uri, MediaUtil.IMAGE_GIF, size, width, height, uri, null, false));
  }

  @Override
  @Nullable
  public Uri getThumbnailUri() {
    return getUri();
  }
}
