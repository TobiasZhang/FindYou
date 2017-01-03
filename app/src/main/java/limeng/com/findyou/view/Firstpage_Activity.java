
package limeng.com.findyou.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.tt.findyou.R;

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
import utils.ToastUtils;

public class Firstpage_Activity extends AppCompatActivity {
    private Button btn,btn1;
    SharedPreferences sp;
    int id;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    break;
                case 1:
                    LoginUtils.userInfo=null;
                    LoginUtils.userInfo = (UserInfo) msg.obj;
                   Intent intent = new Intent(Firstpage_Activity.this, Index.class);
//                   Intent intent = new Intent(Firstpage_Activity.this, UpdateHbyActivity.class);
//                   intent.putExtra("hid",1);
                    startActivity(intent);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage_);
        btn = (Button) findViewById(R.id.login);
        btn1 = (Button) findViewById(R.id.register);
        sp = getSharedPreferences("loginsp",MODE_PRIVATE);
        if(sp.getInt("uid",-1)>0){
            HttpUtils.request("user/get?uid=1&id="+sp.getInt("uid",0), null, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showShortMessage(Firstpage_Activity.this,"当前网络不给力");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.code()==200){
                        DataRoot<UserInfo> dataRoot = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<UserInfo>>(){});
                       if(dataRoot.getData()==null){
                           mHandler.sendEmptyMessage(0);
                       }else{
                           UserInfo u = dataRoot.getData();
                           Message msg = Message.obtain();
                           msg.what=1;
                           msg.obj = u;
                           mHandler.sendMessage(msg);
                       }

                    }

                }
            });
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Firstpage_Activity.this,LoginActivity.class));
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Firstpage_Activity.this,Register_Activity.class));
            }
        });
    }
}
