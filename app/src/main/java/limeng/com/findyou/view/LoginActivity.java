package limeng.com.findyou.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoApplication;
import com.hyphenate.chatuidemo.DemoHelper;
import com.tt.findyou.R;
import com.tt.findyou.utils.Utils;

import java.io.IOException;

import limeng.com.findyou.Index;
import model.dto.DataRoot;
import model.pojo.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utils.GsonManager;
import utils.HttpUtils;
import utils.LoginUtils;
import utils.ScreenUtils;
import utils.ToastUtils;
import widget.MyEditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private MyEditText phone,password;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    LoginUtils.userInfo = (UserInfo) msg.obj;
                    editor.putInt("uid",LoginUtils.userInfo.getId());
                    editor.commit();

                    loginHuanXinServer();
                    break;
                case 2:
                    String result =  msg.obj.toString();
                    ToastUtils.showShortMessage(LoginActivity.this,result);
                    break;

                case 3:
                    Intent intent = new Intent(LoginActivity.this, Index.class);
                    startActivity(intent);
                    break;
            }
        }
    };
    private TextView tx,tx2;
    private Button btn;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

    }
    private void init(){
        phone = (MyEditText) findViewById(R.id.login_input_phone_et);
        password = (MyEditText) findViewById(R.id.login_input_code_et);
        tx = (TextView) findViewById(R.id.forget_pas);
        tx2 = (TextView) findViewById(R.id.back);
        btn = (Button) findViewById(R.id.login_commit_btn);
        sharedPreferences = getSharedPreferences("loginsp",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        tx.setOnClickListener(this);
        btn.setOnClickListener(this);
        tx2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String tel = phone.getText().toString();
        String pass = password.getText().toString();
        switch (v.getId()){
            case R.id.back:finish();break;
            case R.id.forget_pas:startActivity(new Intent(LoginActivity.this,Findpwd.class));break;
            case R.id.login_commit_btn:
                    if(tel.equals("")){
                        ToastUtils.showShortMessage(LoginActivity.this,"电话号码不能为空");
                        return;
                    }if(pass.equals("")){
                        ToastUtils.showShortMessage(LoginActivity.this,"密码不能为空");
                        return;
                    }
                HttpUtils.request("user/login?tel=" + tel + "&password=" + pass, null, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()==200){
                            DataRoot<UserInfo> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<UserInfo>>(){});
                            if(root.getResult().equals("ok")){
                                UserInfo user = root.getData();
                                Message m = Message.obtain();
                                m.obj = user;
                                m.what=1;
                                mHandler.sendMessage(m);
                            }else{
                                String result = root.getResult();
                                Message m = Message.obtain();
                                m.obj = result;
                                m.what=2;
                                mHandler.sendMessage(m);
                            }
                        }
                    }
                });
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.doStatueImmersive(this, Color.BLACK,R.color.colorStatusBar);
    }

    //登录环信服务器
    private void loginHuanXinServer(){
        EMClient.getInstance().login(LoginUtils.userInfo.getId()+"",
                LoginUtils.userInfo.getPassword(),
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


                        Utils.toast(LoginActivity.this,"登录聊天服务器成功！");
                        Log.d("main", "登录聊天服务器成功！");

                        mHandler.sendEmptyMessage(3);
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
}
