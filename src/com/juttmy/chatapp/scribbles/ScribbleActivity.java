package com.juttmy.chatapp.scribbles;

import android.os.Bundle;
import androidx.annotation.RequiresApi;

import com.juttmy.chatapp.PassphraseRequiredActionBarActivity;
import com.juttmy.chatapp.R;

@RequiresApi(19)
public class ScribbleActivity extends PassphraseRequiredActionBarActivity {
  public static final int SCRIBBLE_REQUEST_CODE       = 31424;
  ImageEditorFragment imageEditorFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState, boolean ready) {
    setContentView(R.layout.scribble_activity);
    imageEditorFragment = initFragment(R.id.scribble_container, ImageEditorFragment.newInstance(getIntent().getData()));
  }

/*  @Override
  public void onBackPressed() {
    if (!imageEditorFragment.onBackPressed()) {
      super.onBackPressed();
    }
  } */
}
