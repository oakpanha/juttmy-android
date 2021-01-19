package com.juttmy.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class JuttmySignUpActivity extends Activity {
  String RegUrl = "https://juttmy.com:4433/6266b0fac605517df5d83a7e00fb5157";

  private void startRegistrationActivity(String username) {
    Intent intent = new Intent(this, RegistrationActivity.class);
    if(username != "") intent.putExtra("putusr", username);
    startActivity(intent);
    // no finish() here, the back key should take the user back from RegistrationActivity to WelcomeActivity
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.signup_juttmy);

    WebView webView = (WebView) findViewById(R.id.web_register);
    webView.setWebViewClient(new WebViewClient() {
      @Override
      public void onPageFinished(WebView view, String url) {
        super.onPageFinished(webView, url);
        String uurl = RegUrl + "?juttmyid=";
        if(url.contains(uurl)){
          String usrnam = url.substring(uurl.length());
          startRegistrationActivity(usrnam);  //--register is successful then redirect to login form
        }
      }
      @Override
      public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
      }
    });

    WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setDomStorageEnabled(true);
    webSettings.setSupportZoom(false);
    webSettings.setBuiltInZoomControls(false);

    webView.loadUrl(RegUrl);
  }
}
