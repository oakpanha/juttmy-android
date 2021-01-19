package com.juttmy.chatapp.profiles;


import android.content.Context;

import com.juttmy.chatapp.mms.MediaConstraints;

public class ProfileMediaConstraints extends MediaConstraints {
  @Override
  public int getImageMaxWidth(Context context) {
    return 640;
  }

  @Override
  public int getImageMaxHeight(Context context) {
    return 640;
  }

  @Override
  public int getImageMaxSize(Context context) {
    return 5 * 1024 * 1024;
  }
}
