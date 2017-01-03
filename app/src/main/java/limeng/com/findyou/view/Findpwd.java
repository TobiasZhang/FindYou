package limeng.com.findyou.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tt.findyou.R;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import utils.LoginUtils;
import utils.ToastUtils;
import widget.MyEditText;

public class Findpwd extends AppCompatActivity implements View.OnClickListener{
    private MyEditText inputPhoneEt;
    // 验证码输入框
    private MyEditText inputCodeEt;
    // 获取验证码按钮
    private Button requestCodeBtn;
    // 注册按钮
    private Button commitBtn;
    private TextView tx;
    int i = 30;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpwd);
    }
    private void init(){
        tx = (TextView) findViewById(R.id.back);
        inputPhoneEt = (MyEditText) findViewById(R.id.login_input_phone_et);
        inputCodeEt = (MyEditText) findViewById(R.id.login_input_code_et);
        requestCodeBtn = (Button) findViewById(R.id.login_request_code_btn);
        commitBtn = (Button) findViewById(R.id.login_commit_btn);
        SMSSDK.initSDK(this, "18526015e7758", "7dff61efb53acb3cea2f4fa13b2f69b3");
        EventHandler eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }
    @Override
    public void onClick(View v) {
        String phoneNums = inputPhoneEt.getText().toString();
        switch (v.getId()){
            case R.id.back:finish();break;
            case R.id.login_commit_btn:
                if(inputPhoneEt.getText().equals("")){
                    ToastUtils.showShortMessage( Findpwd.this,"电话号码不能为空");
                    break;
                }
                if(inputCodeEt.getText().equals("")){
                    ToastUtils.showShortMessage(Findpwd.this,"验证码不能为空");
                    break;
                }
                SMSSDK.submitVerificationCode("86", phoneNums, inputCodeEt.getText().toString());
                createProgressBar();break;
            case R.id.login_request_code_btn:
                if(!phoneNums.equals(LoginUtils.userInfo.getTel())){
                    ToastUtils.showShortMessage(Findpwd.this,"电话号码不正确，请重新输入");
                    inputPhoneEt.setText("");
                    return;
                }
                if (!judgePhoneNums(phoneNums)||phoneNums.equals("")||phoneNums==null) {
                    return;
                } // 2. 通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", phoneNums);
                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                requestCodeBtn.setClickable(false);
                requestCodeBtn.setText("重新发送(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();break;
        }
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                requestCodeBtn.setText("已发送");
            } else if (msg.what == -8) {
                requestCodeBtn.setText("获取验证码");
                requestCodeBtn.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;//
                int result = msg.arg2;//用于接受结果
                Object data = msg.obj;
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        Toast.makeText(getApplicationContext(), "提交验证码成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Findpwd.this,
                                ResetPwd.class);
                        //intent.putExtra("phoneNum",inputPhoneEt.getText().toString());
                        startActivity(intent);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "验证码已经发送",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };
    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！",Toast.LENGTH_SHORT).show();
        return false;
    }
    /**
     * 判断一个字符串的位数
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    /**
     * progressbar
     */
    private void createProgressBar() {
        FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        ProgressBar mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
