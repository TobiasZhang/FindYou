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
package com.hyphenate.chatuidemo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chatuidemo.R;
import com.hyphenate.chatuidemo.adapter.NewFriendsMsgAdapter;
import com.hyphenate.chatuidemo.db.InviteMessgeDao;
import com.hyphenate.chatuidemo.domain.InviteMessage;
import com.hyphenate.chatuidemo.parse.MyParseManager;
import com.hyphenate.easeui.domain.EaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * Application and notification
 *
 */
//好友申请与通知
public class NewFriendsMsgActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_new_friends_msg);

		final ListView listView = (ListView) findViewById(R.id.list);
		final InviteMessgeDao dao = new InviteMessgeDao(this);


		final List<InviteMessage> msgs = dao.getMessagesList();
		final NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(NewFriendsMsgActivity.this, 1, msgs);
		dao.saveUnreadMessageCount(0);



		if(msgs.size()==0){

		}else{
			List<String> usernames = new ArrayList<>();
			for(InviteMessage inviteMessage:msgs){
				if(inviteMessage.getGroupId()==null){
					usernames.add(inviteMessage.getFrom());
				}else{
					usernames.add(inviteMessage.getGroupInviter());
				}
			}
			MyParseManager.getInstance().getContactInfos(usernames, new EMValueCallBack<List<EaseUser>>() {
				@Override
				public void onSuccess(List<EaseUser> easeUsers) {
					for(int i = 0; i < easeUsers.size(); i ++){
						EaseUser easeUser = easeUsers.get(i);
						InviteMessage inviteMessage = msgs.get(i);
						if(inviteMessage.getGroupId()==null){//用户信息
							inviteMessage.setTruename(easeUser.getNick());
							inviteMessage.setUserAvantar(easeUser.getAvatarUrl());
						}else{//群聊信息
							inviteMessage.setGroupInviterTruename(easeUser.getNick());
							inviteMessage.setGroupInviterAvantar(easeUser.getAvatarUrl());
						}
					}
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							listView.setAdapter(adapter);
						}
					});
				}

				@Override
				public void onError(int i, String s) {
					System.out.println("NewFriendsMsgActivity-----onError");
				}
			});
		}
	}

	public void back(View view) {
		finish();
	}
}
