package com.juttmy.chatapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDexApplication;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.juttmy.chatapp.connect.ApplicationDcContext;
import com.juttmy.chatapp.connect.FetchWorker;
import com.juttmy.chatapp.connect.ForegroundDetector;
import com.juttmy.chatapp.connect.KeepAliveService;
import com.juttmy.chatapp.connect.NetworkStateReceiver;
import com.juttmy.chatapp.crypto.PRNGFixes;
import com.juttmy.chatapp.jobmanager.JobManager;
import com.juttmy.chatapp.notifications.InChatSounds;
import com.juttmy.chatapp.util.AndroidSignalProtocolLogger;
import com.juttmy.chatapp.util.ScreenLockUtil;
import com.juttmy.chatapp.util.SignalProtocolLoggerProvider;

import java.util.concurrent.TimeUnit;
//import com.squareup.leakcanary.LeakCanary;

public class ApplicationContext extends MultiDexApplication implements DefaultLifecycleObserver {

  public ApplicationDcContext   dcContext;
  private JobManager            jobManager;
  private volatile boolean      isAppVisible;

  public static ApplicationContext getInstance(Context context) {
    return (ApplicationContext)context.getApplicationContext();
  }

  @Override
  public void onCreate() {
    super.onCreate();

    // if (LeakCanary.isInAnalyzerProcess(this)) {
    //   // This process is dedicated to LeakCanary for heap analysis.
    //   // You should not init your app in this process.
    //   return;
    // }
    // LeakCanary.install(this);

    Log.i("MyJuttmy", "++++++++++++++++++ ApplicationContext.onCreate() ++++++++++++++++++");

    System.loadLibrary("native-utils");
    dcContext = new ApplicationDcContext(this);

    new ForegroundDetector(ApplicationContext.getInstance(this));

    BroadcastReceiver networkStateReceiver = new NetworkStateReceiver();
    registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

    KeepAliveService.maybeStartSelf(this);

    initializeRandomNumberFix();
    initializeLogging();
    initializeJobManager();
    ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    InChatSounds.getInstance(this);

    dcContext.setStockTranslations();

    IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
    registerReceiver(new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dcContext.setStockTranslations();
        }
    }, filter);

    // MAYBE TODO: i think the ApplicationContext is also created
    // when the app is stated by FetchWorker timeouts.
    // in this case, the normal threads shall not be started.
    Constraints constraints = new Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build();
    PeriodicWorkRequest fetchWorkRequest = new PeriodicWorkRequest.Builder(
            FetchWorker.class,
            PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, // usually 15 minutes
            TimeUnit.MILLISECONDS,
            PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS, // the start may be preferred by up to 5 minutes, so we run every 10-15 minutes
            TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build();
    WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "FetchWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            fetchWorkRequest);
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
  }

  @Override
  public void onStart(@NonNull LifecycleOwner owner) {
    isAppVisible = true;
  }

  @Override
  public void onStop(@NonNull LifecycleOwner owner) {
    isAppVisible = false;
    ScreenLockUtil.setShouldLockApp(true);
  }

  public JobManager getJobManager() {
    return jobManager;
  }

  public boolean isAppVisible() {
    return isAppVisible;
  }

  private void initializeRandomNumberFix() {
    PRNGFixes.apply();
  }

  private void initializeLogging() {
    SignalProtocolLoggerProvider.setProvider(new AndroidSignalProtocolLogger());
  }

  private void initializeJobManager() {
    this.jobManager = new JobManager(this, 5);
  }
}
