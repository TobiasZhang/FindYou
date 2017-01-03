package com.tt.findyou.application;

import android.app.Application;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TT on 2016/12/15.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(true);
        EaseUI.getInstance().init(this,options);




    }
}
