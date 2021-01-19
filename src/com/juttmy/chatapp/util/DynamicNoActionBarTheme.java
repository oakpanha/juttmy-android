package com.juttmy.chatapp.util;

import androidx.annotation.StyleRes;

import com.juttmy.chatapp.R;

public class DynamicNoActionBarTheme extends DynamicTheme {
  protected @StyleRes int getLightThemeStyle() {
    return R.style.TextSecure_LightNoActionBar;
  }

  protected @StyleRes int getDarkThemeStyle() {
    return R.style.TextSecure_DarkNoActionBar;
  }
}
