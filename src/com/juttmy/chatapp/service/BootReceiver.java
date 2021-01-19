package com.juttmy.chatapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.i("MyJuttmy", "*** BootReceiver.onReceive()");
    // there's nothing more to do here as all initialisation stuff is already done in
    // ApplicationDcContext() which is called before this broadcast is sent.
  }

}
