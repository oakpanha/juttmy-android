/** 
 * Copyright (C) 2011 Whisper Systems
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.juttmy.chatapp.mms;

import android.content.Context;
import android.net.Uri;

import com.b44t.messenger.DcMsg;

import com.juttmy.chatapp.attachments.Attachment;
import com.juttmy.chatapp.attachments.DcAttachment;
import com.juttmy.chatapp.connect.DcHelper;
import com.juttmy.chatapp.util.MediaUtil;

import java.io.File;

public class VideoSlide extends Slide {

  private static Attachment constructVideoAttachment(Context context, Uri uri, long dataSize)
  {
    Uri thumbnailUri = Uri.fromFile(new File(DcHelper.getContext(context).getBlobdirFile("temp-preview.jpg")));
    MediaUtil.ThumbnailSize retWh = new MediaUtil.ThumbnailSize(0, 0);
    MediaUtil.createVideoThumbnailIfNeeded(context, uri, thumbnailUri, retWh);
    return constructAttachmentFromUri(context, uri, MediaUtil.VIDEO_UNSPECIFIED, dataSize, retWh.width, retWh.height, thumbnailUri, null, false);
  }

  public VideoSlide(Context context, Uri uri, long dataSize) {
    super(context, constructVideoAttachment(context, uri, dataSize));
  }

  public VideoSlide(Context context, DcMsg dcMsg) {
    super(context, new DcAttachment(dcMsg));
    dcMsgId = dcMsg.getId();
  }

  @Override
  public boolean hasPlayOverlay() {
    return true;
  }

  @Override
  public boolean hasImage() {
    return true;
  }

  @Override
  public boolean hasVideo() {
    return true;
  }
}