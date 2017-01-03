package limeng.com.findyou.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.reflect.TypeToken;
import com.tt.findyou.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import limeng.com.findyou.Index;
import model.dto.DataRoot;
import model.pojo.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utils.GsonManager;
import utils.HttpUtils;
import utils.LocationUtils;
import utils.LoginUtils;
import utils.ScreenUtils;
import utils.ToastUtils;

public class ResetPwd extends AppCompatActivity {
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    public String province;
    private String city;
    private EditText et;
    private TextView tx;
    private Button btn;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoginUtils.userInfo = (UserInfo) msg.obj;
            ToastUtils.showShortMessage(ResetPwd.this,"重置成功，请登录");
            startActivity(new Intent(ResetPwd.this, LoginActivity.class));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        et = (EditText) findViewById(R.id.repwd);
        tx = (TextView) findViewById(R.id.back);
        btn = (Button) findViewById(R.id.login_commit_btn);
        mLocationClient = new AMapLocationClient(this);
        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et.getText().toString()==""){
                    ToastUtils.showShortMessage(ResetPwd.this,"密码不能为空");
                }else{
                    //修改密码
                    HttpUtils.request("user/update?password=" + et.getText().toString(), null, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showShortMessage(ResetPwd.this,"当前网络不给力");
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if(response.code()==200){
                                DataRoot<UserInfo> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<UserInfo>>(){});
                                if(root.getData()!=null){
                                    UserInfo u = root.getData();
                                    Message m = Message.obtain();
                                    m.obj = u;
                                    mHandler.sendMessage(m);
                                }


                            }
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.doStatueImmersive(this, Color.BLACK,R.color.colorStatusBar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
