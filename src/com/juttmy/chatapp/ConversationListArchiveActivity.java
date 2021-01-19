package com.juttmy.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.juttmy.chatapp.util.DynamicLanguage;
import com.juttmy.chatapp.util.DynamicTheme;

import static com.juttmy.chatapp.ConversationActivity.CHAT_ID_EXTRA;
import static com.juttmy.chatapp.util.RelayUtil.REQUEST_RELAY;
import static com.juttmy.chatapp.util.RelayUtil.acquireRelayMessageContent;
import static com.juttmy.chatapp.util.RelayUtil.isRelayingMessageContent;
import static com.juttmy.chatapp.util.RelayUtil.isSharing;

public class ConversationListArchiveActivity extends PassphraseRequiredActionBarActivity
    implements ConversationListFragment.ConversationSelectedListener
{

  private final DynamicTheme    dynamicTheme    = new DynamicTheme();
  private final DynamicLanguage dynamicLanguage = new DynamicLanguage();

  @Override
  protected void onPreCreate() {
    dynamicTheme.onCreate(this);
    dynamicLanguage.onCreate(this);
  }

  @Override
  protected void onCreate(Bundle icicle, boolean ready) {
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    if (isRelayingMessageContent(this)) {
      getSupportActionBar().setTitle(isSharing(this) ? R.string.chat_share_with_title : R.string.forward_to);
      getSupportActionBar().setSubtitle(R.string.chat_archived_chats_title);
    } else {
      getSupportActionBar().setTitle(R.string.chat_archived_chats_title);
    }

    Bundle bundle = new Bundle();
    bundle.putBoolean(ConversationListFragment.ARCHIVE, true);

    initFragment(android.R.id.content, new ConversationListFragment(), dynamicLanguage.getCurrentLocale(), bundle);
  }

  @Override
  public void onResume() {
    super.onResume();
    dynamicTheme.onResume(this);
    dynamicLanguage.onResume(this);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    switch (item.getItemId()) {
      case android.R.id.home: super.onBackPressed(); return true;
    }

    return false;
  }

  @Override
  public void onCreateConversation(int chatId) {
    Intent intent = new Intent(this, ConversationActivity.class);
    intent.putExtra(CHAT_ID_EXTRA, chatId);
    if (isRelayingMessageContent(this)) {
      acquireRelayMessageContent(this, intent);
      startActivityForResult(intent, REQUEST_RELAY);
    } else {
      startActivity(intent);
    }

    overridePendingTransition(R.anim.slide_from_right, R.anim.fade_scale_out);
  }

  @Override
  public void onSwitchToArchive() {
    throw new AssertionError();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_RELAY && resultCode == RESULT_OK) {
      setResult(RESULT_OK);
      finish();
    }
  }
}