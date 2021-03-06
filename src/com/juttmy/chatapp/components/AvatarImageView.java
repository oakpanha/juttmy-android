package com.juttmy.chatapp.components;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.juttmy.chatapp.ProfileActivity;
import com.juttmy.chatapp.R;
import com.juttmy.chatapp.contacts.avatars.ContactPhoto;
import com.juttmy.chatapp.contacts.avatars.GeneratedContactPhoto;
import com.juttmy.chatapp.mms.GlideRequests;
import com.juttmy.chatapp.recipients.Recipient;
import com.juttmy.chatapp.util.ThemeUtil;

public class AvatarImageView extends AppCompatImageView {

  private static final String TAG = AvatarImageView.class.getSimpleName();

  private OnClickListener listener = null;

  public AvatarImageView(Context context) {
    super(context);
    setScaleType(ScaleType.CENTER_CROP);
  }

  public AvatarImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setScaleType(ScaleType.CENTER_CROP);

    if (attrs != null) {
      TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AvatarImageView, 0, 0);
      typedArray.recycle();
    }
  }

  @Override
  public void setOnClickListener(OnClickListener listener) {
    this.listener = listener;
    super.setOnClickListener(listener);
  }

  public void setAvatar(@NonNull GlideRequests requestManager, @Nullable Recipient recipient, boolean quickContactEnabled) {
    if (recipient != null) {
      ContactPhoto contactPhoto = recipient.getContactPhoto(getContext());
      requestManager.load(contactPhoto)
                    .error(recipient.getFallbackAvatarDrawable(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .circleCrop()
                    .into(this);
      if(quickContactEnabled) {
        setAvatarClickHandler(recipient, quickContactEnabled);
      }
    } else {
      setImageDrawable(new GeneratedContactPhoto("+").asDrawable(getContext(), ThemeUtil.getDummyContactColor(getContext())));
      if (listener != null) super.setOnClickListener(listener);
    }
  }

  public void clear(@NonNull GlideRequests glideRequests) {
    glideRequests.clear(this);
  }

  private void setAvatarClickHandler(final Recipient recipient, boolean quickContactEnabled) {
    if (!recipient.isGroupRecipient() && quickContactEnabled) {
      super.setOnClickListener(v -> {
        if(recipient.getAddress().isDcContact()) {
          Intent intent = new Intent(getContext(), ProfileActivity.class);
          intent.putExtra(ProfileActivity.CONTACT_ID_EXTRA, recipient.getAddress().getDcContactId());
          getContext().startActivity(intent);
        }
      });
    } else {
      super.setOnClickListener(listener);
    }
  }

}
