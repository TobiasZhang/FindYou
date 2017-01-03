package com.hyphenate.chatuidemo.adapter;

import android.content.Context;
import android.content.Intent;

/**
 * Created by TT on 2016/12/23.
 */
public interface IntentAdapter {
    Intent makeIntent(Context context,int targetUid);
}
