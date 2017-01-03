package limeng.com.findyou.view;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.easeui.domain.EaseUser;
import com.tt.findyou.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.dto.DataRoot;
import model.pojo.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.GsonManager;
import utils.HttpUtils;
import utils.LoginUtils;
import utils.ScreenUtils;
import utils.ToastUtils;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener{
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0: ToastUtils.showShortMessage(UpdateActivity.this,"性别修改成功");
                        LoginUtils.userInfo.setSex(sexs);break;
                case 1: ToastUtils.showShortMessage(UpdateActivity.this,"修改姓名成功");
                        LoginUtils.userInfo.setTruename(userName);break;
                case 2: ToastUtils.showShortMessage(UpdateActivity.this,"修改生日成功");
                        LoginUtils.userInfo.setBirth(userbirth);break;
                case 3: ToastUtils.showShortMessage(UpdateActivity.this,"修改电话号码成功");
                        LoginUtils.userInfo.setBirth((String) msg.obj);break;
                case 4: ToastUtils.showShortMessage(UpdateActivity.this,"修改密码成功");
                        LoginUtils.userInfo.setBirth((String) msg.obj);break;
                case 6: ToastUtils.showShortMessage(UpdateActivity.this,"保存成功");
                        UserInfo u = (UserInfo) msg.obj;
                        LoginUtils.userInfo = u;
                    Intent intent = new Intent(UpdateActivity.this,EditActivity.class);
                    startActivity(intent);
                        break;
            }
        }
    };
    private TextView back,sex,name,birth,phone,pass;
    private LinearLayout sex_layout,name_layout,birth_layout,phone_layout,pass_layout;
    String sexbutton[] = {"男","女"};
    Button btn;
    String userSex;
    String userName;
    int sexs;
    String userTel;
    String userPwd;
    String userbirth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        init();
    }
    void init(){
        back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(this);
        sex = (TextView) findViewById(R.id.sex_tx);
        name = (TextView) findViewById(R.id.name_tx);
        birth = (TextView) findViewById(R.id.birth_tx);
        phone = (TextView) findViewById(R.id.telephone_tx);
        pass = (TextView) findViewById(R.id.pass_tx);
        btn = (Button) findViewById(R.id.delete);
        btn.setOnClickListener(this);
        sex_layout = (LinearLayout) findViewById(R.id.sex);
        sex_layout.setOnClickListener(this);
        name_layout = (LinearLayout) findViewById(R.id.name);
        name_layout.setOnClickListener(this);
        birth_layout = (LinearLayout) findViewById(R.id.birth);
        birth_layout.setOnClickListener(this);
        phone_layout = (LinearLayout) findViewById(R.id.telephone);
        phone_layout.setOnClickListener(this);
        pass_layout = (LinearLayout) findViewById(R.id.updatepass);
        pass_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sex:
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                builder.setTitle("更改性别").setSingleChoiceItems(sexbutton, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userSex = sexbutton[which];
                    }
                }).setNegativeButton("取消",null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sex.setText(userSex);
                        sexs = userSex.equals("男")?0:1;
                        //请求服务器更改用户信息
                        HttpUtils.request("user/update?id=" + LoginUtils.userInfo.getId() + "&sex=" + sexs, null, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showShortMessage(UpdateActivity.this,"当前网络不给力");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if(response.code()==200){
                                    mHandler.sendEmptyMessage(0);
                                }
                            }
                        });

                    }
                }).show();
                break;
            case R.id.name:
                final EditText et = new EditText(UpdateActivity.this);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(UpdateActivity.this);
                builder1.setTitle("更改姓名").setView(et).setNegativeButton("取消",null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if("".equals(et.getText().toString().trim())){
                            ToastUtils.showShortMessage(UpdateActivity.this,"修改失败,姓名不能为空");
                            return;
                        }
                        name.setText(et.getText().toString().trim());
                        userName = et.getText().toString().trim();
                        RequestBody body = new FormBody.Builder().add("id",LoginUtils.userInfo.getId()+"")
                                .add("truename",userName).build();
                        //请求服务器更改用户信息
                        HttpUtils.request("user/update", body, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showShortMessage(UpdateActivity.this,"当前网络不给力");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if(response.code()==200){
                                    mHandler.sendEmptyMessage(1);

                                    EaseUser me = DemoHelper.getInstance().getUserProfileManager().getCurrentUserInfo();
                                    me.setNickname(userName);
                                    //DemoHelper.getInstance().saveContact(me);
                                    DemoHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(userName);
                                }
                            }
                        });
                    }
                }).show();
                break;
            case R.id.birth:
                new DatePickerDialog(UpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String y = year+"";
                        String month = monthOfYear+1>=10?(monthOfYear+1)+"":"0"+(monthOfYear+1);
                        String day = dayOfMonth>=10?dayOfMonth+"":"0"+dayOfMonth;
                        birth.setText(y+"/"+month+"/"+day);
                        userbirth = new SimpleDateFormat("yyyy-MM-dd").format(new Date(year,monthOfYear+1,dayOfMonth));
//                        RequestBody body = new FormBody.Builder().add("id",LoginUtils.userInfo.getId()+"")
//                                .add("truename",userName).build();
                        //请求服务器更改用户信息
                        HttpUtils.request("user/update?id="+LoginUtils.userInfo.getId()+"&birth="+userbirth, null, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showShortMessage(UpdateActivity.this,"当前网络不给力");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if(response.code()==200){
                                    mHandler.sendEmptyMessage(2);
                                }
                            }
                        });

                    }},1990,9,29).show();
                break;
            case R.id.telephone:
                final EditText et1 = new EditText(UpdateActivity.this);
                AlertDialog.Builder builder2 = new AlertDialog.Builder(UpdateActivity.this);
                builder2.setTitle("更改电话号码").setView(et1).setNegativeButton("取消",null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if("".equals(et1.getText().toString().trim())){
                            ToastUtils.showShortMessage(UpdateActivity.this,"修改失败,电话号码不能为空");
                            return;
                        }
                        phone.setText(et1.getText().toString().trim());
                        //请求服务器更改用户信息
                        HttpUtils.request("user/update?id="+LoginUtils.userInfo.getId()+"&tel="+et1.getText().toString().trim(), null, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showShortMessage(UpdateActivity.this,"当前网络不给力");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if(response.code()==200){
                                    Message msg = Message.obtain();
                                    msg.obj = phone.getText().toString().trim();
                                    msg.what=3;
                                    mHandler.sendMessage(msg);
                                }
                            }
                        });
                    }
                }).show();
                break;
            case R.id.updatepass:
                final EditText et2 = new EditText(UpdateActivity.this);
                AlertDialog.Builder builder3 = new AlertDialog.Builder(UpdateActivity.this);
                builder3.setTitle("更改密码").setView(et2).setNegativeButton("取消",null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if("".equals(et2.getText().toString().trim())){
                            //if(et2.getText().toString().trim()=="")
                            ToastUtils.showShortMessage(UpdateActivity.this,"修改失败,密码不能为空");
                            return;
                        }
                        pass.setText(et2.getText().toString().trim());
                        //请求服务器更改用户信息
                        HttpUtils.request("user/update?id="+LoginUtils.userInfo.getId()+"&tel="+pass.getText().toString().trim(), null, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showShortMessage(UpdateActivity.this,"当前网络不给力");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if(response.code()==200){
                                    Message msg = Message.obtain();
                                    msg.obj = pass.getText().toString().trim();
                                    msg.what=4;
                                    mHandler.sendMessage(msg);
                                }
                            }
                        });
                    }
                }).show();
                break;
            case R.id.back:finish();break;
            case R.id.delete:
                    HttpUtils.request("user/get?uid=0&id=" + LoginUtils.userInfo.getId(), null, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showShortMessage(UpdateActivity.this,"当前网络不给力");
                                }
                            });
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            DataRoot<UserInfo> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<UserInfo>>(){});
                            UserInfo u = root.getData();
                            Message msg = Message.obtain();
                            msg.obj=u;
                            msg.what=6;
                            mHandler.sendMessage(msg);
                        }
                    });
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.doStatueImmersive(this, Color.BLACK,R.color.colorStatusBar);
        name.setText(LoginUtils.userInfo.getTruename());
        sex.setText(LoginUtils.userInfo.getSex()==0?"男":"女");
        birth.setText(LoginUtils.userInfo.getBirth());
        phone.setText(LoginUtils.userInfo.getTel());
        pass.setText(LoginUtils.userInfo.getPassword());
    }
}
