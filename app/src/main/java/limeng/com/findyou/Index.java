package limeng.com.findyou;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.bumptech.glide.Glide;
import com.easemob.redpacketsdk.constant.RPConstant;
import com.easemob.redpacketui.utils.RedPacketUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.adapter.IntentAdapter;
import com.hyphenate.chatuidemo.adapter.OnUserExceptionAdapter;
import com.hyphenate.chatuidemo.db.InviteMessgeDao;
import com.hyphenate.chatuidemo.db.UserDao;
import com.hyphenate.chatuidemo.runtimepermissions.PermissionsManager;
import com.hyphenate.chatuidemo.runtimepermissions.PermissionsResultAction;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.chatuidemo.ui.ChatFragment;
import com.hyphenate.chatuidemo.ui.ContactListFragment;
import com.hyphenate.chatuidemo.ui.ConversationListFragment;
import com.hyphenate.chatuidemo.ui.GroupsActivity;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.tt.findyou.R;
import com.tt.findyou.SettingActivity;
import com.tt.findyou.ui.fragment.TongZhiFragment;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;
import java.util.List;

import limeng.com.findyou.view.Chat;
import limeng.com.findyou.view.Concacts;
import limeng.com.findyou.view.EditActivity;
import limeng.com.findyou.view.FindActivity;
import limeng.com.findyou.view.FindOldActivity;
import limeng.com.findyou.view.FindPerson;
import limeng.com.findyou.view.FindSameHobby;
import limeng.com.findyou.view.LoginActivity;
import limeng.com.findyou.view.MyThemeActivity;
import limeng.com.findyou.view.MyselfinformationActivity;
import limeng.com.findyou.view.Notification;
import limeng.com.findyou.view.Noviceguidance;
import limeng.com.findyou.view.OtherMainActivity;
import limeng.com.findyou.view.Recommend;
import limeng.com.findyou.view.SearchActivity;
import ui.PagerAdapter;
import utils.GlideCircleTransform;
import utils.LocationUtils;
import utils.LoginUtils;
import utils.ScreenUtils;

public class Index extends AppCompatActivity implements View.OnClickListener , com.hyphenate.chatuidemo.adapter.MyAdapter{
    //drawerLayout 里面的控件
    private ImageView userImg,add;
    private TextView userName,editor,maintheme,tip,setup,guide,share,location;
    private List<Fragment> flist = new ArrayList<>();
    private Toolbar toolbar;
    private TextView search;
    private DrawerLayout drawerLayout;
    private TabLayout tabLayout;
    private ViewPager pager;
    private PopupWindow popupWindow;
    private LinearLayout contentLayout;
    private AMapLocationClient aMapLocationClient;
    private String [] title = {"推荐","找人","聊天","通讯录","通知"};




    // textview for unread message count
    private TextView unreadLabel;
    // textview for unread event message
    private TextView unreadAddressLable;


    protected static final String TAG = "MainActivity";

    private ContactListFragment contactListFragment;
    // user logged into another device
    public boolean isConflict = false;
    // user account was removed
    private boolean isCurrentAccountRemoved = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        aMapLocationClient = new AMapLocationClient(this);

        DemoHelper.getInstance().onUserExceptionAdapter = new OnUserExceptionAdapter() {
            @Override
            public Intent onUserExceptionIntent(Context context) {
                return new Intent(context,Index.class);
            }
        };
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }

        //make sure activity will not in background if user is logged into another device or removed
        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            DemoHelper.getInstance().logout(false,null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        setContentView(R.layout.activity_index);

        // runtime permission for android 6.0, just require all permissions here for simple
        requestPermissions();



        init();
        Glide.with(Index.this).load(LoginUtils.userInfo.getHeadImage()).transform(new GlideCircleTransform(Index.this)).into(userImg);
        LocationUtils.doPosition(this,aMapLocationClient,location);
        userName.setText(LoginUtils.userInfo.getTruename());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.app_name,R.string.close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(),title,flist));
        pager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(pager);



        //umeng api
        MobclickAgent.updateOnlineConfig(this);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);

        showExceptionDialogFromIntent(getIntent());

        inviteMessgeDao = new InviteMessgeDao(this);
        UserDao userDao = new UserDao(this);


        //register broadcast receiver to receive the change of group from DemoHelper
        registerBroadcastReceiver();

        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        //debug purpose only
        registerInternalDebugReceiver();
    }
    private void init(){
        toolbar = (Toolbar) findViewById(R.id.tools);
        add = (ImageView) findViewById(R.id.tool_img);
        add.setOnClickListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager) findViewById(R.id.pager);
        contentLayout = (LinearLayout) findViewById(R.id.content);
        userImg = (ImageView) findViewById(R.id.img);
        userName = (TextView) findViewById(R.id.name);
        editor = (TextView) findViewById(R.id.tips);
        editor.setOnClickListener(this);
        maintheme = (TextView) findViewById(R.id.maintheme);
        maintheme.setOnClickListener(this);
        location = (TextView) findViewById(R.id.location);
        location.setOnClickListener(this);
        tip = (TextView) findViewById(R.id.mytips);
        tip.setOnClickListener(this);
        setup = (TextView) findViewById(R.id.setup);
        setup.setOnClickListener(this);
        guide = (TextView) findViewById(R.id.guide);
        guide.setOnClickListener(this);
        search = (TextView) findViewById(R.id.search);
        search.setOnClickListener(this);
        share = (TextView) findViewById(R.id.share);
        share.setOnClickListener(this);


        conversationListFragment = new ConversationListFragment();
        contactListFragment = new ContactListFragment();
        conversationListFragment.hideTitleBar();
        contactListFragment.hideTitleBar();

        flist.add(new Recommend());
        flist.add(new FindPerson());
        flist.add(conversationListFragment);
        flist.add(contactListFragment);
        flist.add(new TongZhiFragment());


        unreadLabel = (TextView) findViewById(R.id.my_unread_msg_number);
        unreadAddressLable = (TextView) findViewById(R.id.my_unread_address_number);

        IntentAdapter intentAdapter = new IntentAdapter() {
            @Override
            public Intent makeIntent(Context context,int targetUid) {
                Intent intent = null;
                if(targetUid==LoginUtils.userInfo.getId()){
                    intent = new Intent(context,MyselfinformationActivity.class);
                }else{
                    intent = new Intent(context,OtherMainActivity.class);
                    intent.putExtra("uid", targetUid);
                }

                return intent;
            }
        };
        ContactListFragment.intentAdapter = intentAdapter;
        ChatFragment.intentAdapter = intentAdapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img:
            case R.id.name:
            case R.id.tips:startIntent(MyselfinformationActivity.class);break;//编辑个人资料
            case R.id.maintheme:startIntent(MyThemeActivity.class);break;//查看主题帖
            case R.id.setup:
                SettingActivity.activity = Index.this;
                startActivity(new Intent(Index.this,SettingActivity.class));
                break;
            case R.id.mytips:startIntent(MyTipsActivity.class);break;
            case R.id.guide:startIntent(Noviceguidance.class);break;
            case R.id.share://分享
                 break;
            case R.id.search:startIntent(SearchActivity.class);break;
            case R.id.tool_img:
                View rootView = getLayoutInflater().inflate(R.layout.pop_item_layout,null);
                TextView findNew = (TextView) rootView.findViewById(R.id.findnew);
                TextView old = (TextView) rootView.findViewById(R.id.old);
                findNew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startIntent(FindSameHobby.class);
                    }
                });
                old.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startIntent(FindOldActivity.class);
                    }
                });
                popupWindow = new PopupWindow(rootView,130,200,true);
                popupWindow.setAnimationStyle(R.style.mystyle);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.colorStatusBar));
                popupWindow.setFocusable(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    popupWindow.showAsDropDown(add,0,0,Gravity.LEFT);
                }
                darkenBackground(0.8f);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        darkenBackground(1f);
                    }
                });
                break;
            case R.id.location:LocationUtils.doPosition(this,aMapLocationClient,location);break;
        }
    }
    public void startIntent(Class clazz){
        Intent intent = new Intent(this,clazz);
        startActivity(intent);
    }
    private void darkenBackground(Float bgcolor){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgcolor;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.doStatueImmersive(this, Color.BLACK,R.color.colorStatusBar);



        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
            updateUnreadAddressLable();
        }
        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationUtils.stopLocation(aMapLocationClient);
        LocationUtils.destory(aMapLocationClient);


        if (exceptionBuilder != null) {
            exceptionBuilder.create().dismiss();
            exceptionBuilder = null;
            isExceptionDialogShow = false;
        }
        unregisterBroadcastReceiver();

        try {
            unregisterReceiver(internalDebugReceiver);
        } catch (Exception e) {
        }

    }




    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * check if current user account was remove
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //red packet code : 处理红包回执透传消息
            for (EMMessage message : messages) {
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                final String action = cmdMsgBody.action();//获取自定义action
                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
                    RedPacketUtil.receiveRedPacketAckMessage(message);
                }
            }
            //end of red packet code
            refreshUIWithMessage();
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {}
    };


    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        intentFilter.addAction(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLable();

                // refresh conversation list
                if (conversationListFragment != null) {
                    conversationListFragment.refresh();
                }
                if(contactListFragment != null) {
                    contactListFragment.refresh();
                }
                String action = intent.getAction();
                if(action.equals(Constant.ACTION_GROUP_CHANAGED)){
                    if (EaseCommonUtils.getTopActivity(Index.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
                //red packet code : 处理红包回执透传消息
                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
                    if (conversationListFragment != null){
                        conversationListFragment.refresh();
                    }
                }
                //end of red packet code
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {}
        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (com.hyphenate.chatuidemo.ui.ChatActivity.activityInstance != null && com.hyphenate.chatuidemo.ui.ChatActivity.activityInstance.getToChatUsername() != null &&
                            username.equals(com.hyphenate.chatuidemo.ui.ChatActivity.activityInstance.getToChatUsername())) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(Index.this, com.hyphenate.chatuidemo.ui.ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
        }
        @Override
        public void onContactInvited(String username, String reason) {}
        @Override
        public void onContactAgreed(String username) {}
        @Override
        public void onContactRefused(String username) {}
    }

    private void unregisterBroadcastReceiver(){
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }


    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            unreadLabel.setText(String.valueOf(count));
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * update the total unread count
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
                if (count > 0) {
                    unreadAddressLable.setVisibility(View.VISIBLE);
                } else {
                    unreadAddressLable.setVisibility(View.INVISIBLE);
                }
            }
        });

    }


    /**
     * get unread event notification count, including application, accepted, etc
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for(EMConversation conversation:EMClient.getInstance().chatManager().getAllConversations().values()){
            if(conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount=chatroomUnreadMsgCount+conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal-chatroomUnreadMsgCount;
    }

    private InviteMessgeDao inviteMessgeDao;



    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private android.app.AlertDialog.Builder exceptionBuilder;
    private boolean isExceptionDialogShow =  false;
    private BroadcastReceiver internalDebugReceiver;
    private ConversationListFragment conversationListFragment;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    private int getExceptionMessageId(String exceptionType) {
        if(exceptionType.equals(Constant.ACCOUNT_CONFLICT)) {
            return R.string.connect_conflict;
        } else if (exceptionType.equals(Constant.ACCOUNT_REMOVED)) {
            return R.string.em_user_remove;
        } else if (exceptionType.equals(Constant.ACCOUNT_FORBIDDEN)) {
            return R.string.user_forbidden;
        }
        return R.string.Network_error;
    }
    /**
     * show the dialog when user met some exception: such as login on another device, user removed or user forbidden
     */
    private void showExceptionDialog(String exceptionType) {
        isExceptionDialogShow = true;
        DemoHelper.getInstance().logout(false,null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!Index.this.isFinishing()) {
            // clear up global variables
            try {
                if (exceptionBuilder == null)
                    exceptionBuilder = new android.app.AlertDialog.Builder(Index.this);
                exceptionBuilder.setTitle(st);
                exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));
                exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exceptionBuilder = null;
                        isExceptionDialogShow = false;
                        finish();

                        // TODO: 2016/12/22  异地登陆返回login

                        Intent intent = new Intent(Index.this, limeng.com.findyou.view.LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                exceptionBuilder.setCancelable(false);
                exceptionBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }
        }
    }

    private void showExceptionDialogFromIntent(Intent intent) {
        EMLog.e(TAG, "showExceptionDialogFromIntent");
        if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
            showExceptionDialog(Constant.ACCOUNT_CONFLICT);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
            showExceptionDialog(Constant.ACCOUNT_REMOVED);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false)) {
            showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showExceptionDialogFromIntent(intent);
    }

    /**
     * debug purpose only, you can ignore this
     */
    private void registerInternalDebugReceiver() {
        internalDebugReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                DemoHelper.getInstance().logout(false,new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                finish();
                                startActivity(new Intent(Index.this, limeng.com.findyou.view.LoginActivity.class));
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {}

                    @Override
                    public void onError(int code, String message) {}
                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);

    }


    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();
                // refresh conversation list
                if (conversationListFragment != null) {
                    conversationListFragment.refresh();
                }
            }
        });
    }
}
