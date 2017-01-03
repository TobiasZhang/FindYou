package com.tt.findyou;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.ui.ContactListFragment;
import com.hyphenate.chatuidemo.ui.ConversationListFragment;
import com.hyphenate.easeui.domain.EaseUser;
import com.tt.findyou.ui.adapter.MyFragmentPagerAdapter;
import com.tt.findyou.ui.fragment.TongZhiFragment;
import com.tt.findyou.utils.HttpUtils;
import com.tt.findyou.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.dto.DataRoot;
import model.pojo.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    List<Fragment> fragmentList = new ArrayList<>();
    ConversationListFragment conversationListFragment;
    ContactListFragment contactListFragment;
    List<UserInfo> friends;
    Map<String,EaseUser> easeUserMap = new HashMap<>();

    Handler handler = new Handler(){
        @Override
        public void handleMessage(final Message msg) {

            switch (msg.what){
                case 1:
                    //会话列表 fragment
                    conversationListFragment = new ConversationListFragment();
                    /*conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

                        @Override
                        public void onListItemClicked(EMConversation conversation) {
                            Intent intent = new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName());
                            intent.putExtra("username",conversation.getUserName());
                            int charType = 1;
                            if(conversation.isGroup()){
                                charType = EaseConstant.CHATTYPE_GROUP;
                            }
                            intent.putExtra("chatType",charType);
                            startActivity(intent);

                        }
                    });*/

                    //联系人列表fragment
                    contactListFragment= new ContactListFragment();
                    handler.sendEmptyMessage(3);
                    //需要设置联系人列表才能启动fragment

                    /*HttpUtils.request("user/getFriends",
                            new FormBody.Builder()
                                    .add("uid", Utils.loginUser.getId()+"")
                                    .build(), new okhttp3.Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                    Utils.toast(MainActivity.this,"获取好友列表失败");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if(response.code()==200){
                                        DataRoot<List<UserInfo>> dataRoot = HttpUtils.parseJson(response.body().string(),new TypeToken<DataRoot<List<UserInfo>>>(){});
                                        if(dataRoot.getResult().equals("ok")){
                                            friends = dataRoot.getData();

                                            for(UserInfo user:friends){
                                                EaseUser easeUser = new EaseUser(user.getId()+"");
                                                easeUser.setAvatar(user.getHeadImage());
                                                easeUser.setNickname(user.getNickname());
                                                easeUserMap.put(user.getId()+"",easeUser);
                                            }

                                            UserInfo loginUser = Utils.loginUser;
                                            EaseUser easeUserMe = new EaseUser(loginUser.getId()+"");
                                            easeUserMe.setAvatar(loginUser.getHeadImage());
                                            easeUserMe.setNickname(loginUser.getNickname());
                                            easeUserMap.put(Utils.loginUser.getId()+"",easeUserMe);

                                            contactListFragment.setContactsMap(easeUserMap);
                                            //设置item点击事件
                                            contactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {
                                                @Override
                                                public void onListItemClicked(EaseUser user) {
                                                    Intent intent = new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername());
                                                    intent.putExtra("username",user.getUsername());
                                                    intent.putExtra("chatType",EaseConstant.CHATTYPE_SINGLE);
                                                    startActivity(intent);
                                                }
                                            });
                                        //设置用户信息提供者
                                            EaseUI easeUI = EaseUI.getInstance();
                                            //需要EaseUI库显示用户头像和昵称设置此provider
                                            easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
                                                @Override
                                                public EaseUser getUser(String id) {
                                                    EaseUser easeUser = easeUserMap.get(id);
                                                    return easeUser;
                                                }
                                            });

                                            handler.sendEmptyMessage(3);
                                        }else{
                                            Utils.toast(MainActivity.this,dataRoot.getResult());
                                        }
                                    }else{
                                        Utils.toast(MainActivity.this,"获取好友列表失败---"+response.code());
                                    }

                                }
                            });*/


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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager.setOffscreenPageLimit(3);

        if(Utils.loginUser==null){
            login();
        }else
            handler.sendEmptyMessage(1);



        /*binding.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file1 = new File(Utils.DIR_ROOT,"1.jpg");
                RequestBody uploadFile = RequestBody.create(MediaType.parse("application/octet-stream"),file1);

                MultipartBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file","123",uploadFile)
                        .build();
                HttpUtils.request("user/uploadTest", body, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        Utils.toast(MainActivity.this,"---onFailure------");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        System.out.println(str);
                        Utils.toast(MainActivity.this,str);
                    }
                });
            }
        });


        try {
            InputStream is = new FileInputStream(new File(""));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
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
                        Utils.toast(MainActivity.this,"登录失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()==200){
                            DataRoot<UserInfo> dataRoot = HttpUtils.parseJson(response.body().string(),new TypeToken<DataRoot<UserInfo>>(){});
                            if(dataRoot.getResult().equals("ok")){
                                UserInfo loginUser = dataRoot.getData();
                                Utils.loginUser = loginUser;

                                Utils.toast(MainActivity.this,"登录成功");


                                //登录环信服务器
                    EMClient.getInstance().login(Utils.loginUser.getId()+"",
                            Utils.loginUser.getPassword(),
                            new EMCallBack() {//回调
                        @Override
                        public void onSuccess() {
                            EMClient.getInstance().groupManager().loadAllGroups();
                            EMClient.getInstance().chatManager().loadAllConversations();
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


                            }else{
                                Utils.toast(MainActivity.this,dataRoot.getResult());
                                handler.sendEmptyMessage(2);
                            }
                        }else{
                            Utils.toast(MainActivity.this,"登录失败---"+response.code());
                        }

                    }
                });
    }
}
