/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tt.findyou;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.redpacketsdk.constant.RPConstant;
import com.easemob.redpacketui.utils.RedPacketUtil;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.DemoApplication;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.adapter.OnUserExceptionAdapter;
import com.hyphenate.chatuidemo.db.InviteMessgeDao;
import com.hyphenate.chatuidemo.db.UserDao;
import com.hyphenate.chatuidemo.runtimepermissions.PermissionsManager;
import com.hyphenate.chatuidemo.runtimepermissions.PermissionsResultAction;
import com.hyphenate.chatuidemo.ui.*;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.tt.findyou.ui.adapter.MyFragmentPagerAdapter;
import com.tt.findyou.ui.fragment.*;
import com.tt.findyou.utils.HttpUtils;
import com.tt.findyou.utils.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.dto.DataRoot;
import model.pojo.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class MainActivity2 extends BaseActivity implements com.hyphenate.chatuidemo.adapter.MyAdapter {
	// textview for unread message count
	private TextView unreadLabel;
	// textview for unread event message
	private TextView unreadAddressLable;

	Button setting;
	ViewPager viewPager;
	TabLayout tabLayout;
	List<Fragment> fragmentList = new ArrayList<>();

	protected static final String TAG = "MainActivity";

	private ContactListFragment contactListFragment;
	// user logged into another device
	public boolean isConflict = false;
	// user account was removed
	private boolean isCurrentAccountRemoved = false;




	Handler handler = new Handler(){
		@Override
		public void handleMessage(final Message msg) {
			switch (msg.what){
				case 1:
					handler.sendEmptyMessage(3);
					break;
				case 3:
					fragmentList.add(conversationListFragment);
					fragmentList.add(contactListFragment);
					fragmentList.add(new TongZhiFragment());
					List<String> titleList = new ArrayList<>();
					titleList.add("会话");
					titleList.add("通讯录");
					titleList.add("通知");
					viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList,titleList));

					conversationListFragment.hideTitleBar();
					contactListFragment.hideTitleBar();

					tabLayout.setupWithViewPager(viewPager);


					break;
			}
		}
	};
	/**
	 * check if current user account was remove
	 */
	public boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DemoHelper.getInstance().onUserExceptionAdapter = new OnUserExceptionAdapter() {
			@Override
			public Intent onUserExceptionIntent(Context context) {
				return new Intent(context,MainActivity2.class);
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

		setContentView(R.layout.activity_main);
		// runtime permission for android 6.0, just require all permissions here for simple
		requestPermissions();

		initView();

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
	 * init views
	 */
	private void initView() {

		viewPager = (ViewPager) findViewById(com.tt.findyou.R.id.viewPager);
		tabLayout = (TabLayout) findViewById(com.tt.findyou.R.id.tabLayout);
		viewPager.setOffscreenPageLimit(3);
		setting = (Button) findViewById(R.id.setting);
		setting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SettingActivity.activity = MainActivity2.this;
				startActivity(new Intent(MainActivity2.this,SettingActivity.class));
			}
		});

		unreadLabel = (TextView) findViewById(R.id.my_unread_msg_number);
		unreadAddressLable = (TextView) findViewById(R.id.my_unread_address_number);

		conversationListFragment = new ConversationListFragment();
		contactListFragment = new ContactListFragment();

		if(Utils.loginUser==null){
			String lastLoginJson = getSharedPreferences("config.xml",MODE_PRIVATE).getString("last_login_data",null);
			if(lastLoginJson != null){
				DataRoot<UserInfo> dataRoot = HttpUtils.parseJson(lastLoginJson,new TypeToken<DataRoot<UserInfo>>(){});
				Utils.loginUser = dataRoot.getData();
				loginHuanXinServer();
				Utils.toast(this,"读取sp登陆成功");
			}else{
				login();
			}
		}else
			handler.sendEmptyMessage(1);
	}

	private void login(){
		HttpUtils.request("user/login",
				new FormBody.Builder()
						.add("tel", "13718113926")
						.add("password", "tt")
						.build(), new Callback() {
					@Override
					public void onFailure(Call call, IOException e) {
						e.printStackTrace();
						Utils.toast(MainActivity2.this,"登录失败");
					}

					@Override
					public void onResponse(Call call, Response response) throws IOException {
						if(response.code()==200){
							String responseContent = response.body().string();
							DataRoot<UserInfo> dataRoot = HttpUtils.parseJson(responseContent,new TypeToken<DataRoot<UserInfo>>(){});
							if(dataRoot.getResult().equals("ok")){
								UserInfo loginUser = dataRoot.getData();
								Utils.loginUser = loginUser;

								Utils.toast(MainActivity2.this,"登录成功");
								//存SP
								getSharedPreferences("config.xml",MODE_PRIVATE).edit().putString("last_login_data",responseContent).commit();
								//登录环信服务器
								loginHuanXinServer();


							}else{
								Utils.toast(MainActivity2.this,dataRoot.getResult());
								handler.sendEmptyMessage(2);
							}
						}else{
							Utils.toast(MainActivity2.this,"登录失败---"+response.code());
						}

					}
				});
	}

	private void loginHuanXinServer(){
		//登录环信服务器
		EMClient.getInstance().login(Utils.loginUser.getId()+"",
				Utils.loginUser.getPassword(),
				new EMCallBack() {//回调
					@Override
					public void onSuccess() {

						// ** manually load all local groups and conversation
						EMClient.getInstance().groupManager().loadAllGroups();
						EMClient.getInstance().chatManager().loadAllConversations();

						// update current user's display name for APNs
						boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
								DemoApplication.currentUserNick.trim());
						if (!updatenick) {
							Log.e("LoginActivity", "update current user nick fail");
						}

												/*if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
													pd.dismiss();
												}*/
						// get user's info (this should be get from App's server or 3rd party service)
						DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();


						Utils.toast(MainActivity2.this,"登录聊天服务器成功！");
						Log.d("main", "登录聊天服务器成功！");

						handler.sendEmptyMessage(1);
					}

					@Override
					public void onProgress(int progress, String status) {

					}

					@Override
					public void onError(int code, String message) {
						Log.d("main", "登录聊天服务器失败！"+message);
					}
				});
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
                    if (EaseCommonUtils.getTopActivity(MainActivity2.this).equals(GroupsActivity.class.getName())) {
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
					    Toast.makeText(MainActivity2.this, com.hyphenate.chatuidemo.ui.ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
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

	@Override
	protected void onDestroy() {
		super.onDestroy();		
		
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
			if(conversation.getType() == EMConversationType.ChatRoom)
			chatroomUnreadMsgCount=chatroomUnreadMsgCount+conversation.getUnreadMsgCount();
		}
		return unreadMsgCountTotal-chatroomUnreadMsgCount;
	}

	private InviteMessgeDao inviteMessgeDao;

	@Override
	protected void onResume() {
		super.onResume();

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
		if (!MainActivity2.this.isFinishing()) {
			// clear up global variables
			try {
				if (exceptionBuilder == null)
				    exceptionBuilder = new android.app.AlertDialog.Builder(MainActivity2.this);
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

						Intent intent = new Intent(MainActivity2.this, LoginActivity.class);
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
                                startActivity(new Intent(MainActivity2.this, LoginActivity.class));
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
